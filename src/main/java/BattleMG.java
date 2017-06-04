import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BattleMG {

    private static final Logger log = LogManager.getLogger(BattleMG.class);
    static ReentrantLock dbLock;

    static void magic(int playersThreads, int battlesThreads, int pageCount, ReentrantLock dbLock) {

        TaskScheduler taskScheduler = TaskScheduler.get(playersThreads, dbLock);
        ExecutorService service = Executors.newFixedThreadPool(battlesThreads);
        ReentrantLock restart = new ReentrantLock();
        Condition restartCondition = restart.newCondition();
        AtomicInteger ai = new AtomicInteger(0);
        boolean readyToRestart = false;

        for (int pp = 0; pp < 5_000_000; pp++) {
            for (int z = pageCount; z > 0; z--) {
                synchronized (ai) {
                    while (ai.get() > playersThreads) {
                        try {
                            ai.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Thread thd = new Thread(new GetBattles(ai, z, taskScheduler, dbLock, restart, restartCondition, readyToRestart));
                    thd.start();
                    ai.incrementAndGet();
                }
            }
        }
    }


    public static void main(String[] args) {
        Properties prop = GetProperties.get();
        String proxy_ip = prop.getProperty("proxy_ip");
        String proxy_port = prop.getProperty("proxy_port");
        System.setProperty("http.proxyHost", proxy_ip);
        System.setProperty("http.proxyPort", proxy_port);
        System.getProperties().remove("sun.stdout.encoding");
        System.getProperties().remove("sun.stderr.encoding");
        int playersThreads = 2;
        int battlesThreads = 2;
        int pageCount = 10;
        if (args.length == 3) {
            playersThreads = Integer.parseInt(args[0]);
            battlesThreads = Integer.parseInt(args[1]);
            pageCount = Integer.parseInt(args[2]);
        } else {
            log.error("Set parameters to default");
        }
        log.error("battles threads count = " + battlesThreads);
        log.error("players threads count = " + playersThreads);
        log.error("page to parse = " + pageCount);
        ReentrantLock reentrantLock = new ReentrantLock(true);
        magic(playersThreads, battlesThreads, pageCount, reentrantLock);
    }
}
