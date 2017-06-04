import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jsoup.nodes.Element;

import javax.persistence.Query;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.locks.ReentrantLock;

public class ReplayParser implements Runnable {
    private static final Logger log = LogManager.getLogger(ReplayParser.class);
    private Element element;
    private Battle battle;
    private boolean isExist;
    private MyQueue myQueue;
    private ReentrantLock reentrantLock;
    TaskScheduler taskScheduler;
    int pageNumber;

    ReplayParser(int pageNumber, Element element, TaskScheduler taskScheduler, ReentrantLock reentrantLock) {
        this.pageNumber = pageNumber;
        this.element = element;
        this.taskScheduler = taskScheduler;
        this.reentrantLock = reentrantLock;
    }

    @Override
    public void run() {
        long battleId = Long.parseLong(element.attr("data-replay"));
        Session session = HibernateFactory.getSession();
        Query query;
        List list;
        query = session.createQuery("from Battle where battleId = :battleId");

        query.setParameter("battleId", battleId);
        reentrantLock.lock();
        list = query.getResultList();
        if (list.isEmpty()) {
            Transaction transaction = session.beginTransaction();
            battle = new Battle();
            battle.setBattleId(battleId);
            session.saveOrUpdate(battle);
            transaction.commit();
        } else {
            isExist = true;
        }
        reentrantLock.unlock();
        session.close();
        if (!isExist) {
            String raw = element.select("div[class=col-9 replay__title]").text();
            int index = raw.indexOf(']');
            String mission = raw.substring(1, index);
            switch (mission) {
                case "Conquest #1":
                    mission = "C1";
                    break;
                case "Conquest #2":
                    mission = "C2";
                    break;
                case "Conquest #3":
                    mission = "C3";
                    break;
                case "Conquest #4":
                    mission = "C4";
                    break;
                case "Ground Strike":
                    mission = "GS";
                    break;
                case "Domination":
                    mission = "Dom";
                    break;
                case "Military Exercise":
                    mission = "ME";
                    break;
                case "Alternate History":
                    mission = "AH";
                    break;
                case "Air Domination":
                    mission = "AD";
                    break;
                case "Front line":
                    mission = "FL";
                    break;
                case "Defence #1":
                    mission = "D1";
                    break;
                case "Defence #2":
                    mission = "D2";
                    break;
                case "Operation":
                    mission = "Op";
                    break;
                case "Tournament":
                    mission = "Tour";
                    break;
                case "Airfield Capture":
                    mission = "AC";
                    break;

                default:
                    break;
            }
            battle.setMission(mission);
            battle.setMap(raw.substring(index + 2, raw.length()));
            String dateString = element.select("span[class=date__text]").text();
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.ENGLISH);
            try {
                Date parsed = formatter.parse(dateString);
                java.sql.Timestamp sql = new java.sql.Timestamp(parsed.getTime());
                battle.setStart(sql);
            } catch (ParseException e) {
                log.error("Parse error " + e);
                e.printStackTrace();
            }
            String gametypes = element.select("div[class=col-6]").select("span[class=stat__value]").first().text();
            switch (gametypes) {
                case "Random battle":
                    battle.setGameType(GameType.Random);
                    break;
                case "Squadrons":
                    battle.setGameType(GameType.Squadrons);
                    break;
                case "Firing field":
                    battle.setGameType(GameType.FF);
                    break;
                case "Tournament":
                    battle.setGameType(GameType.Tournament);
                    break;
                case "Squadron tournament":
                    battle.setGameType(GameType.ST);
                    break;
                default:
                    battle.setGameType(GameType.N);
                    log.error("Error GameType " + gametypes);
                    break;
            }
            String type = element.select("div[class=row]").get(2).select("span[class=stat__value]").first().text();
            switch (type) {
                case "Arcade battles":
                    battle.setGameMode(GameMode.Arcade);
                    break;
                case "Realistic battles":
                    battle.setGameMode(GameMode.Realistic);
                    break;
                case "Simulation battles":
                    battle.setGameMode(GameMode.Simulation);
                    break;
                default:
                    battle.setGameMode(GameMode.N);
                    log.error("Error set GameMode " + type);
                    break;
            }
            String vehicles = element.select("div[class=col-6]").select("span[class=stat__value]").get(2).text();
            switch (vehicles) {
                case "Aircraft":
                    battle.setVehicles(Vehicles.Aircraft);
                    break;
                case "Tank":
                    battle.setVehicles(Vehicles.Tank);
                    break;
                case "Tanks and planes":
                    battle.setVehicles(Vehicles.TanksAndPlanes);
                    break;
                case "Tournament":
                    battle.setVehicles(Vehicles.Tournament);
                    break;
                default:
                    battle.setVehicles(Vehicles.N);
                    log.error("Error in vehicles " + vehicles);
                    break;
            }

            try {
                battle.setDuration(Time.valueOf(element.select("div[class=col-2 pr0 stat-column]")
                        .select("span[class=inlined text-left]")
                        .text()));
            } catch (Exception e) {
                log.error("SET DURATION  = NULL");
                e.printStackTrace();
            }

            taskScheduler.addTask(battle);

        }
    }
}
