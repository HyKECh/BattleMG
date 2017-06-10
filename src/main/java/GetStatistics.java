import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


class GetStatistics implements Runnable {
    private final static Logger log = LogManager.getLogger(GetStatistics.class);
    private final String login;
    private AbstractStatistics stata;
    private Player player;
    private Document document;

    GetStatistics(Player player, Document document) {
        this.document = document;
        this.player = player;
        this.login = this.player.getLogin();
    }


    private void saveData() {
        Session session;
        Transaction t;
        session = HibernateFactory.getSession();
        t = session.beginTransaction();
        session.saveOrUpdate(player);
        t.commit();
        session.close();
    }

    void pase() {

        Element us;
        Element ussr;
        Element uk;
        Element ge;
        Element jp;
        Element air;
        Element ground;
        String airArcadePerf;
        String airRbPerf;
        String airSbPerf;
        String groundArcadePerf;
        String groundRbPerf;
        String groundSbPerf;

        String level = document.select("div[style=color:#ccc;font-size: 18px;margin:3px;]").text();
        try {
            player.setLevel(Integer.parseInt(level));
        } catch (Exception e) {
            log.error(player.getLogin() + " level " + level);
            log.error("Level parse exception");
        }
        String dateString = document.select("div[class=fleft small blue]").text();
        dateString = dateString.substring(dateString.indexOf(':') + 1);
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            log.error(player.getLogin() + " date " + dateString);
            log.error("Date parse exception");
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());
        player.setRegistered(sqlDate);

        if (!document.select("span[class=username--is-banned-icn]").isEmpty()) {
            player.setBanned(true);
        }

        us = document.select("span[class=user-info-sprite user-info-sprite-us_flag]").parents().parents().first();
        ussr = document.select("span[class=user-info-sprite user-info-sprite-ussr_flag]").parents().parents().first();
        uk = document.select("span[class=user-info-sprite user-info-sprite-uk_flag]").parents().parents().first();
        ge = document.select("span[class=user-info-sprite user-info-sprite-ge_flag]").parents().parents().first();
        jp = document.select("span[class=user-info-sprite user-info-sprite-ge_flag]").parents().parents().first();


        System.out.println(player.getLogin());
        air = document.select("span[class=js-air_force]").first();
        //   System.out.println(air.html());
        airArcadePerf = air.select("span[class=battle-score arc_combat]").text();
        //   System.out.println("-" + airArcadePerf + "-");
        airRbPerf = air.select("span[class=battle-score real_combat hide]").text();
        //   System.out.println("-" + airRbPerf + "-");
        airSbPerf = air.select("span[class=battle-score sim_combat hide]").text();
        //     System.out.println(airSbPerf);
        player.setArcadeAirPerf(Float.parseFloat(airArcadePerf.substring(0, airArcadePerf.length() - 1)));
        player.setRbAirPerf(Float.parseFloat(airRbPerf.substring(0, airRbPerf.length() - 1)));
        player.setSbAirPerf(Float.parseFloat(airSbPerf.substring(0, airSbPerf.length() - 1)));


        ground = document.select("span[class=js-ground_force hide]").first();

        groundArcadePerf = ground.select("span[class=battle-score arc_combat]").text();
        groundRbPerf = ground.select("span[class=battle-score real_combat hide]").text();
        groundSbPerf = ground.select("span[class=battle-score sim_combat hide]").text();
        player.setArcadeGroundPerf(Float.parseFloat(groundArcadePerf.substring(0, groundArcadePerf.length() - 1)));
        player.setRbGroundPerf(Float.parseFloat(groundRbPerf.substring(0, groundRbPerf.length() - 1)));
        player.setSbGroundPerf(Float.parseFloat(groundSbPerf.substring(0, groundSbPerf.length() - 1)));

        document.select("span[class=username--is-banned-icn]").empty();
        if (!document.select("span[class=username--is-banned-icn]").empty().isEmpty()) {
            player.setBanned(true);
            log.error("BANNED");
        }


        player.setUsVehicles(Integer.parseInt(us.select("td:eq(" + "2" + ")").text()));
        player.setUsEliteVehicles(Integer.parseInt(us.select("td:eq(" + "3" + ")").text()));
        player.setUsMedals(Integer.parseInt(us.select("td:eq(" + "4" + ")").text()));

        player.setUssrVehicles(Integer.parseInt(ussr.select("td:eq(" + "2" + ")").text()));
        player.setUssrEliteVehicles(Integer.parseInt(ussr.select("td:eq(" + "3" + ")").text()));
        player.setUssrMedals(Integer.parseInt(ussr.select("td:eq(" + "4" + ")").text()));

        player.setUkVehicles(Integer.parseInt(uk.select("td:eq(" + "2" + ")").text()));
        player.setUkEliteVehicles(Integer.parseInt(uk.select("td:eq(" + "3" + ")").text()));
        player.setUkMedals(Integer.parseInt(uk.select("td:eq(" + "4" + ")").text()));

        player.setGeVehicles(Integer.parseInt(ge.select("td:eq(" + "2" + ")").text()));
        player.setGeEliteVehicles(Integer.parseInt(ge.select("td:eq(" + "3" + ")").text()));
        player.setGeMedals(Integer.parseInt(ge.select("td:eq(" + "4" + ")").text()));

        player.setJpVehicles(Integer.parseInt(jp.select("td:eq(" + "2" + ")").text()));
        player.setJpEliteVehicles(Integer.parseInt(jp.select("td:eq(" + "3" + ")").text()));
        player.setJpMedals(Integer.parseInt(jp.select("td:eq(" + "4" + ")").text()));
    }

    @Override
    public void run() {
        pase();
        saveData();
    }
}
