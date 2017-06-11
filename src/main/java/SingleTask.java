import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

public class SingleTask implements Runnable {
    private static final Logger log = LogManager.getLogger(SingleTask.class);
    private static String BASE_URL;
    private static int taskNumber;
    private static DesiredCapabilities caps;
    static private String email;
    static private String pass;
    private static int[] wasRun;

    static {
        ArrayList<String> cliArgsCap = new ArrayList<>();
        Properties prop = GetProperties.get();
        String proxy_ip = prop.getProperty("proxy_ip");
        String proxy_port = prop.getProperty("proxy_port");
        BASE_URL = prop.getProperty("base_url");
        cliArgsCap.add("--proxy=" + proxy_ip + ":" + proxy_port);
        caps = new DesiredCapabilities();
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "loadImages", false);
        System.setProperty("phantomjs.binary.path", "phantomjs.exe");
        email = prop.getProperty("email");
        pass = prop.getProperty("pass");
    }


    private Pattern pattern = Pattern.compile("\n");


    private int currNumber;
    private WebDriver driver;
    private ReentrantLock internal = new ReentrantLock();
    private Condition internalCondition = internal.newCondition();
    private boolean isBusy;
    private Battle battle;
    private boolean isReady;
    private ReentrantLock reentrantLock;
    private String rawTeamA;
    private String rawTeamB;
    private String[] teamA;
    private String[] teamB;
    private StringBuilder message;
    private long tStart;
    private long tStop;

    SingleTask(ReentrantLock dbLock, int l) {
        taskNumber++;
        reentrantLock = dbLock;
        currNumber = l;
        Thread thread = new Thread(this, "SingleTask-" + taskNumber);
        thread.start();
    }

    static void setRun(int number) {
        wasRun = new int[number];
        for (int i = 0; i < number; i++) {
            wasRun[i] = 0;
        }
    }

    boolean isBusy() {
        return isBusy;
    }


    void add(Battle battle) {
        internal.lock();
        isBusy = true;
        this.battle = battle;
        isReady = true;
        internalCondition.signal();
        internal.unlock();
    }


    private void parceData() {
        message = new StringBuilder();
        String url = BASE_URL + battle.getBattleId();
        tStart = System.currentTimeMillis();
        driver.navigate().to(url);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tStop = System.currentTimeMillis();
        message.append("Loaded in ");
        message.append((tStop - tStart) / 1000.0f);
        message.append("\t");
        message.append(driver.getCurrentUrl());
        message.append("\t");
        tStart = System.currentTimeMillis();
        List<WebElement> webElements = driver.findElements(By.className("team-players"));
        if (webElements.isEmpty()) {
            log.error("REPARSE PAGE");
            driver.navigate().to(url);
            webElements = driver.findElements(By.className("team-players"));
        }
        rawTeamA = webElements.get(0).getText();
        rawTeamB = webElements.get(1).getText();
        teamA = new String[0];
        teamA = pattern.split(rawTeamA);
        teamB = new String[0];
        teamB = pattern.split(rawTeamB);
    }

    private void process() {
        internal.lock();
        isBusy = false;
        isReady = false;

        Session session = HibernateFactory.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            parceData();
            if ((rawTeamA.isEmpty()) || (rawTeamB.isEmpty())) {
                log.error("REPARSE " + battle.getBattleId());
            }
        } catch (Exception e) {
            log.error("               ================================ CRASHED at BattleId " + battle.getBattleId() + " =====");
            e.printStackTrace();
            try {
                driver.quit();
            } catch (Exception p) {
                e.printStackTrace();
            }
            internal.unlock();
            try {
                driver.quit();
            } catch (Exception e1) {
                log.error("++++++++++++ ERROR DURING CLOSE ++++++++");
                e1.printStackTrace();
            }
            init();
            internal.lock();
        }

        if (!rawTeamA.isEmpty()) {
            if (teamA.length > 0) {
                for (String login : teamA) {
                    PlayersInBattle playersInBattle = new PlayersInBattle();
                    playersInBattle.setBattle(battle);
                    battle.setTeamA(teamA.length);

                    int spacePosition = login.indexOf(" ");
                    String squadron = "";
                    if (spacePosition > 0) {
                        squadron = login.substring(0, spacePosition);
                        login = login.substring(login.indexOf(" ") + 1, login.length());
                    }
                    Player player = Player.getPlayer(login, session, reentrantLock);
                    if (spacePosition > 0) {
                        player.setSqualron(squadron);
                    }
                    playersInBattle.setPlayer(player);
                    playersInBattle.setTeam(0);
                    session.saveOrUpdate(playersInBattle);
                }
            }
        }
        message.append("Team A = ");
        message.append(teamA.length);


        if (!rawTeamB.isEmpty()) {
            teamB = pattern.split(rawTeamB);
            if (teamB.length > 0) {

                for (String login : teamB) {
                    PlayersInBattle playersInBattle = new PlayersInBattle();
                    playersInBattle.setBattle(battle);
                    battle.setTeamB(teamB.length);
                    int spacePosition = login.indexOf(" ");
                    String squadron = "";
                    if (spacePosition > 0) {
                        squadron = login.substring(0, spacePosition);
                        login = login.substring(login.indexOf(" ") + 1, login.length());
                    }
                    Player player = Player.getPlayer(login, session, reentrantLock);
                    if (spacePosition > 0) {
                        player.setSqualron(squadron);
                    }
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
        tStop = System.currentTimeMillis();
        message.append("\tprocessed in ");
        message.append((tStop - tStart) / 1000.0f);
        log.error(message.toString());


        wasRun[currNumber]++;
        if (wasRun[currNumber] >= 100) {
            wasRun[currNumber] = 0;
            log.error("                  ================================ browser restarted =======================");
            internal.unlock();
            driver.quit();
            init();
            internal.lock();
        }

        rawTeamA = "";
        rawTeamB = "";

        internalCondition.signal();
        internal.unlock();
    }

    public void run() {
        init();
        while (true) {
            internal.lock();
            while (!isBusy) {
                try {
                    internalCondition.await();
                } catch (InterruptedException e) {
                    System.out.println("Error");
                }
            }
            if (isReady) {
                process();
            }
            internal.unlock();
        }
    }


    private void init() {
        driver = new PhantomJSDriver(caps);
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(15, TimeUnit.SECONDS);
        driver.navigate().to(BASE_URL);
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys(pass);
        driver.findElement(By.xpath("//input[@value='Authorization']")).submit();
    }
}
