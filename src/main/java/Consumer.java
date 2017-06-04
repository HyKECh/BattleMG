import java.util.concurrent.locks.ReentrantLock;

public class Consumer implements Runnable {

    private MyQueue myQueue;
    private ReentrantLock reentrantLock;

    Consumer(MyQueue myQueue, ReentrantLock reentrantLock) {
        this.myQueue = myQueue;
        this.reentrantLock = reentrantLock;
        Thread thread = new Thread(this, "Consumer");
        thread.start();
    }

    @Override
    public void run() {
        System.out.println("Consumer started");
    }
}
