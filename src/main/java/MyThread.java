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


    private void init() {
       /* driver = new PhantomJSDriver(caps);
        //driver = new FirefoxDriver();
        driver.navigate().to(BASE_URL);
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys(pass);
        driver.findElement(By.xpath("//input[@value='Authorization']")).submit();
        driver.navigate().to(BASE_URL);
        isBusy.set(false);*/
    }

    void aw() {

    }

    @Override
    public void run() {
//        log.error("Thread" + Thread.currentThread().getName());
//        init();
//        while (true) {
//
//            System.out.println("before lock");
//            lock.lock();
//
//
//            try {
//                condition.await();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            lock.unlock();
//
//            System.out.println("after lock");
//            System.out.println("pocess" + battle.getBattleId());
//
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            isBusy().set(false);
//            emptyTask = true;

//            if (battle.getBattleId() == 0) {
//                isBusy.set(true);
//                System.out.println("EXIT");
//                try {
//                    externalStop.await();
//                } catch (InterruptedException | BrokenBarrierException e) {
//                    e.printStackTrace();
//                }
//                thread.interrupt();
//                return;
//            }
           /* String url = BASE_URL + battle.getBattleId();
            long start = System.currentTimeMillis();
            driver.navigate().to(url);
            long stop = System.currentTimeMillis();
            StringBuilder message = new StringBuilder();
            message.append("Loaded in ");
            message.append((stop - start) / 1000.0f);
            message.append("\t");
            message.append(driver.getCurrentUrl());
            message.append("\t");
            start = System.currentTimeMillis();
            String rawTeamA = "";
            try {
                rawTeamA = driver.findElements(By.className("team-players")).get(0).getText();
            } catch (Exception e) {
                log.error("Error parsing Team A");
            }
            Session session = HibernateFactory.getSession();
            Transaction transaction = session.beginTransaction();
            //   start = System.currentTimeMillis();
            String[] teamA = new String[0];
            if (!rawTeamA.isEmpty()) {
                teamA = pattern.split(rawTeamA);
                if (teamA.length >= 1) {
                    if (teamA.length == 1) {
                        log.error("Single player in team A " + teamA[0]);
                    }
                    for (String login : teamA) {
                        PlayersInBattle playersInBattle = new PlayersInBattle();
                        playersInBattle.setBattle(battle);
                        battle.setTeamA(teamA.length);

                        Player player = Player.getPlayer(login, session, reentrantLock);

                        playersInBattle.setPlayer(player);
                        playersInBattle.setTeam(0);
                        session.saveOrUpdate(playersInBattle);
                    }
                }
            }
            message.append("Team A = ");
            message.append(teamA.length);
            String rawTeamB = "";
            try {
                rawTeamB = driver.findElements(By.className("team-players")).get(1).getText();
            } catch (Exception e) {
                log.error("Error parsing Team A");
            }
            String[] teamB = new String[0];
            if (!rawTeamB.isEmpty()) {
                teamB = pattern.split(rawTeamB);
                if (teamB.length >= 1) {
                    if (teamB.length == 1) {
                        log.error("Single player in team B " + teamB[0]);
                    }
                    for (String login : teamB) {
                        PlayersInBattle playersInBattle = new PlayersInBattle();
                        playersInBattle.setBattle(battle);
                        battle.setTeamB(teamB.length);
                        Player player = Player.getPlayer(login, session, reentrantLock);
                        playersInBattle.setPlayer(player);
                        playersInBattle.setTeam(1);
                        session.saveOrUpdate(playersInBattle);
                    }
                }
            }
            battle.setPlayers(teamA.length + teamB.length);
            message.append("\tTeam B = ");
            message.append(teamB.length);
            session.saveOrUpdate(battle);
            transaction.commit();
            session.close();
            stop = System.currentTimeMillis();
            message.append("\tprocessed in ");
            message.append((stop - start) / 1000.0f);
            log.error(message.toString());
            isBusy().set(false);
*/
    }


    void setBattle(Battle battle) {
//        System.out.println("set battle" + battle.getBattleId());
//        lock.lock();
//        isBusy().set(true);
//        this.battle = battle;
//        emptyTask = false;
//        condition.signalAll();
//        lock.unlock();
       /* isBusy().set(true);
        this.battle = battle;
        try {
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
*/
    }
}
