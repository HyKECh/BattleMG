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

        int threadCount = Integer.parseInt(args[0]);
        ExecutorService service = Executors.newFixedThreadPool(threadCount);
        ScrollableResults scResults = session.createQuery("from Player WHERE geVehicles = 0")
                .setFetchSize(1)
                .scroll(ScrollMode.FORWARD_ONLY);

        for (int i = 0; i < threadCount; i++) {
            scResults.next();
            Player player = (Player) scResults.get(0);
            String playerLogin = player.getLogin();
            if (playerLogin.contains("/")) {
                log.error(playerLogin + " is bot");
            } else {
                service.submit(new SetTask(player));
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        while (scResults.next()) {
            Player player = (Player) scResults.get(0);
            String playerLogin = player.getLogin();
            if (playerLogin.contains("/")) {
                log.error(playerLogin + " is bot");
            } else {
                service.submit(new SetTask(player));
            }
        }

        service.shutdown();
        try {
            while (!service.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS)) {
                log.info("Awaiting completion of threads.");
            }
        } catch (InterruptedException e) {
            log.error("Thread problem");
        }

    }
}
