import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TaskScheduler implements Runnable {
    static boolean shedulerStop;
    static SingleTask[] tasks;
    static int number;
    static AtomicBoolean anyReady = new AtomicBoolean(true);
    static BlockingQueue<Battle> queue;
    static ReentrantLock dbLock;
    static ReentrantLock taskLock = new ReentrantLock();
    static Condition condition = taskLock.newCondition();
    static boolean isReady;
    static AtomicInteger activeTasks = new AtomicInteger();
    static AtomicInteger queuSize = new AtomicInteger();
    static AtomicInteger tasksInQueue = new AtomicInteger();
    static boolean stop;
    static ReentrantLock isReadyLock = new ReentrantLock();
    static Condition isReadyCondition = isReadyLock.newCondition();

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
        while ((!stop) || (tasksInQueue.get() > 0)) {
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
                    } finally {

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

    void stop() {
        long startt = System.currentTimeMillis();
        while (tasksInQueue.get() > 0) {
            taskLock.lock();
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                taskLock.unlock();
            }

        }
        long stopt = System.currentTimeMillis();
        System.out.println((stopt - startt) / 1000.0f);
        for (int i = 0; i < number; i++) {
            tasks[i].stop();
        }
        stop = true;
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
