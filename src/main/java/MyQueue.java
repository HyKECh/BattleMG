import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantLock;

public class MyQueue implements Runnable {
    private static int size;
    private static MyQueue myQueue;
    private static MyThread[] threads;
    private static BlockingQueue<Battle> queue;
    private ReentrantLock reentrantLock;
    Thread thread;
    static boolean ready;

    MyQueue(ReentrantLock reentrantLock) {
        this.reentrantLock = reentrantLock;
        Thread thread = new Thread(this, "TaskManager");
        thread.start();
    }

    void stop() {
        for (int i = 0; i < size * 2; i++) {
            Battle battle = new Battle();
            battle.setBattleId(0);
            try {
                queue.put(battle);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        thread.interrupt();
    }


    @Override
    public void run() {
        System.out.println("AAAA");
        take();
    }

    void take() {
        System.out.println("qu take");
        while (true) {
            if (ready) {
                for (int i = 0; i < size; i++) {
                    if (!threads[i].isBusy().get()) {
                        System.out.println("qu take");
                        try {
                            System.out.println("CCC");
                            Battle battle = queue.take();
                            System.out.println("BBB");
                            threads[i].setBattle(battle);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    static MyQueue get(int size, ReentrantLock reentrantLock, CyclicBarrier externalStop) {
        reentrantLock = reentrantLock;
        reentrantLock.lock();
        System.out.println("start may");
        MyQueue.size = size;
        if (myQueue == null) {
            myQueue = new MyQueue(reentrantLock);

            threads = new MyThread[MyQueue.size];
            for (int i = 0; i < MyQueue.size; i++) {
                threads[i] = new MyThread(reentrantLock, externalStop);
                System.out.println(i);
            }
            queue = new ArrayBlockingQueue<>(MyQueue.size + 2);
        }
        ready = true;
        reentrantLock.unlock();
        return myQueue;
    }

    public void add(Battle battle) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("battke " + battle.getBattleId() + " was added");
            queue.put(battle);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}