import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GetAllPlayersStatistics {
    private static final Logger log = LogManager.getLogger(GetAllPlayersStatistics.class);

    public static void main(String[] args) {


        Session session;
        session = HibernateFactory.getSession();


      /*  Query query = session.createQuery("from Player");
        List<Player> list = query.getResultList();
        log.error(list.size());*/


        int threadCount = Integer.parseInt(args[0]);
        ExecutorService service = Executors.newFixedThreadPool(threadCount);
        ScrollableResults scResults = session.createQuery("from Player WHERE geVehicles = 0")
                .setFetchSize(1)
                .scroll(ScrollMode.FORWARD_ONLY);

        for (int i = 0; i < threadCount; i++) {
            scResults.next();
            Player player = (Player) scResults.get(0);
            service.submit(new SetTask(player));
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (scResults.next()) {
            Player player = (Player) scResults.get(0);
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                log.error("Interrupted exception" + e);
//            }
            service.submit(new SetTask(player));
        }
        //   session.close();
        service.shutdown();
        try {
            while (!service.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS)) {
                log.info("Awaiting completion of threads.");
            }
        } catch (InterruptedException e) {
            log.error("Thread problem");
        }
        //   HibernateFactory.close();

    }
}
