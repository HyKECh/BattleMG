import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

public class MyThread implements Runnable {
    private static DesiredCapabilities caps;
    private static final Logger log = LogManager.getLogger(MyThread.caps);
    static private String email;
    static private String pass;

    Pattern pattern = Pattern.compile("\n");
    CountDownLatch latch = new CountDownLatch(1);
    Thread thread;
    boolean emptyTask = false;
    private ReentrantLock reentrantLock;
    private AtomicBoolean isBusy = new AtomicBoolean(true);
    private WebDriver driver;
    private Battle battle;
    private AtomicBoolean isReady = new AtomicBoolean(false);
    private CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
    private CyclicBarrier externalStop;
    private ReentrantLock lock = new ReentrantLock();
    Condition condition = lock.newCondition();


    MyThread(ReentrantLock reentrantLock, CyclicBarrier externalStop) {
        this.externalStop = externalStop;
        thread = new Thread(this);
        thread.start();
        this.reentrantLock = reentrantLock;
    }

    synchronized AtomicBoolean isBusy() {
        return isBusy;
    }



    @Override
    public void run() {

    }


    void setBattle(Battle battle) {
    }
}
