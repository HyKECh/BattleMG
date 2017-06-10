import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TaskScheduler implements Runnable {
    private static SingleTask[] tasks;
    private static int number;
    private static BlockingQueue<Battle> queue;
    private static ReentrantLock dbLock;
    private static ReentrantLock taskLock = new ReentrantLock();
    private static Condition condition = taskLock.newCondition();
    private static boolean isReady;
    private static AtomicInteger tasksInQueue = new AtomicInteger();
    private static ReentrantLock isReadyLock = new ReentrantLock();
    private static Condition isReadyCondition = isReadyLock.newCondition();

    private TaskScheduler() {
    }

    static TaskScheduler get(int number, ReentrantLock reentrantLock) {
        TaskScheduler.dbLock = reentrantLock;
        TaskScheduler taskScheduler = new TaskScheduler();
        Thread thread = new Thread(taskScheduler, "TaskScheduler");
        thread.start();
        TaskScheduler.number = number;
        init();
        return taskScheduler;
    }

    public void run() {
        isReadyLock.lock();
        while (!isReady) {
            try {
                isReadyCondition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        isReadyLock.unlock();
        while (tasksInQueue.get() > 0) {
            while (tasksInQueue.get() == 0) {
                isReadyLock.lock();
                try {
                    isReadyCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                isReadyLock.unlock();
            }
            for (int i = 0; i < number; i++) {
                taskLock.lock();
                if (!tasks[i].isBusy()) {
                    taskLock.lock();
                    try {
                        if (tasksInQueue.get() > 0) {
                            tasks[i].add(queue.take());
                            tasksInQueue.decrementAndGet();
                            condition.signal();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                taskLock.unlock();
            }
        }
    }


    void addTask(Battle battle) {
        try {
            queue.put(battle);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tasksInQueue.incrementAndGet();
        isReadyLock.lock();
        isReadyCondition.signal();
        isReadyLock.unlock();
    }

    private static void init() {
        tasks = new SingleTask[number];
        queue = new ArrayBlockingQueue<>(number + 2);
        for (int i = 0; i < number; i++) {
            tasks[i] = new SingleTask(dbLock, i);
        }
        SingleTask.setRun(number);
        isReadyLock.lock();
        isReady = true;
        System.out.println("=======================");
        isReadyCondition.signal();
        isReadyLock.unlock();
    }
}