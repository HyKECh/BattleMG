import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.persistence.Query;
import java.util.List;
import java.util.regex.Pattern;


class GetAdvancedStatistics implements Runnable {
    private static final Pattern fistDig;
    private static final Pattern allDig;
    private final String login;
    private final Document document;
    private ArcadeBattleStatistics arcade;
    private RealBattleStatistics rb;
    private SimulatorBattleStatistics sb;

    static {
        fistDig = Pattern.compile("[0-9].*");
        allDig = Pattern.compile("[0-9]+");
    }

    GetAdvancedStatistics(String login, Document document) {
        this.document = document;
        this.login = login;
    }

    void prepare() {

        Session session;
        session = HibernateFactory.getSession();
        Query query;
        List list;
        query = session.createQuery("from ArcadeBattleStatistics where login = :login");
        query.setParameter("login", login);
        list = query.getResultList();
        if (!list.isEmpty()) {
            arcade = (ArcadeBattleStatistics) list.get(0);
        } else {
            arcade = new ArcadeBattleStatistics();
            arcade.setLogin(login);
        }

        query = session.createQuery("from RealBattleStatistics where login = :login");
        query.setParameter("login", login);
        list = query.getResultList();
        if (!list.isEmpty()) {
            rb = (RealBattleStatistics) list.get(0);
        } else {
            rb = new RealBattleStatistics();
            rb.setLogin(login);
        }
        query = session.createQuery("from SimulatorBattleStatistics where login = :login");
        query.setParameter("login", login);
        list = query.getResultList();
        if (!list.isEmpty()) {
            sb = (SimulatorBattleStatistics) list.get(0);
        } else {
            sb = new SimulatorBattleStatistics();
            sb.setLogin(login);
        }
        session.close();
    }


    private int stringToInt(String string) {
        int res = 0;
        String days = "0";
        String hours = "0";
        String minutes = "0";
        if (fistDig.matcher(string).matches()) {
            if (allDig.matcher(string).matches()) {
                res = Integer.parseInt(string);
            } else {
                if (string.indexOf('.') < 0) {
                    int day = string.indexOf('д');
                    int hour = string.indexOf('ч');
                    int min = string.indexOf('м');
                    if (day > 0) days = string.substring(0, day);
                    else day = -2;
                    if (hour > 0) hours = string.substring(day + 2, hour);
                    else hour = -2;
                    if (min > 0) minutes = string.substring(hour + 2, min);
                    res = Integer.parseInt(days) * 24 * 60 + Integer.parseInt(hours) * 60 + Integer.parseInt(minutes);
                } else {
                    String month = string.substring(0, string.indexOf('м') - 1);
                    res = (int) (Float.parseFloat(month) * 30 * 24 * 60);
                }
            }
        }
        return res;
    }


    void parse(Element data) {
        String type = data.select("td:eq(" + "1" + ")").text();
        if (type.equals("Победы")) {
            arcade.setMission_victories(stringToInt(data.select("td:eq(" + "2" + ")").text()));
            rb.setMission_victories(stringToInt(data.select("td:eq(" + "3" + ")").text()));
            sb.setMission_victories(stringToInt(data.select("td:eq(" + "4" + ")").text()));
        } else if (type.equals("Законченные миссии")) {
            arcade.setMission_finished(stringToInt(data.select("td:eq(" + "2" + ")").text()));
            rb.setMission_finished(stringToInt(data.select("td:eq(" + "3" + ")").text()));
            sb.setMission_finished(stringToInt(data.select("td:eq(" + "4" + ")").text()));
        } else if (type.equals("Количество сессий")) {
            arcade.setRandom_session(stringToInt(data.select("td:eq(" + "2" + ")").text()));
            rb.setRandom_session(stringToInt(data.select("td:eq(" + "3" + ")").text()));
            sb.setRandom_session(stringToInt(data.select("td:eq(" + "4" + ")").text()));
        } else if (type.equals("Смертей")) {
            arcade.setDeaths_value(stringToInt(data.select("td:eq(" + "2" + ")").text()));
            rb.setDeaths_value(stringToInt(data.select("td:eq(" + "3" + ")").text()));
            sb.setDeaths_value(stringToInt(data.select("td:eq(" + "4" + ")").text()));
        } else if (type.equals("Время игры на истребителях")) {
            arcade.setRandom_time_fighter(stringToInt(data.select("td:eq(" + "2" + ")").text()));
            rb.setRandom_time_fighter(stringToInt(data.select("td:eq(" + "3" + ")").text()));
            sb.setRandom_time_fighter(stringToInt(data.select("td:eq(" + "4" + ")").text()));
        } else if (type.equals("Время игры на бомбардировщиках")) {
            arcade.setRandom_time_bomber(stringToInt(data.select("td:eq(" + "2" + ")").text()));
            rb.setRandom_time_bomber(stringToInt(data.select("td:eq(" + "3" + ")").text()));
            sb.setRandom_time_bomber(stringToInt(data.select("td:eq(" + "4" + ")").text()));
        } else if (type.equals("Время игры на штурмовиках")) {
            arcade.setRandom_time_attacker(stringToInt(data.select("td:eq(" + "2" + ")").text()));
            rb.setRandom_time_attacker(stringToInt(data.select("td:eq(" + "3" + ")").text()));
            sb.setRandom_time_attacker(stringToInt(data.select("td:eq(" + "4" + ")").text()));
        } else if (type.equals("Время игры на танках")) {
            arcade.setRandom_time_tank(stringToInt(data.select("td:eq(" + "2" + ")").text()));
            rb.setRandom_time_tank(stringToInt(data.select("td:eq(" + "3" + ")").text()));
            sb.setRandom_time_tank(stringToInt(data.select("td:eq(" + "4" + ")").text()));
        } else if (type.equals("Время игры на САУ")) {
            arcade.setRandom_time_tank_destroyer(stringToInt(data.select("td:eq(" + "2" + ")").text()));
            rb.setRandom_time_tank_destroyer(stringToInt(data.select("td:eq(" + "3" + ")").text()));
            sb.setRandom_time_tank_destroyer(stringToInt(data.select("td:eq(" + "4" + ")").text()));
        } else if (type.equals("Время игры на тяжелых танках")) {
            arcade.setRandom_time_tank_heavy(stringToInt(data.select("td:eq(" + "2" + ")").text()));
            rb.setRandom_time_tank_heavy(stringToInt(data.select("td:eq(" + "3" + ")").text()));
            sb.setRandom_time_tank_heavy(stringToInt(data.select("td:eq(" + "4" + ")").text()));
        } else if (type.equals("Время игры на ЗСУ")) {
            arcade.setRandom_time_SPAA(stringToInt(data.select("td:eq(" + "2" + ")").text()));
            rb.setRandom_time_SPAA(stringToInt(data.select("td:eq(" + "3" + ")").text()));
            sb.setRandom_time_SPAA(stringToInt(data.select("td:eq(" + "4" + ")").text()));
        } else if (type.equals("Воздушных целей уничтожено")) {
            arcade.setTarget_air_value(stringToInt(data.select("td:eq(" + "2" + ")").text()));
            rb.setTarget_air_value(stringToInt(data.select("td:eq(" + "3" + ")").text()));
            sb.setTarget_air_value(stringToInt(data.select("td:eq(" + "4" + ")").text()));
        } else if (type.equals("Наземных целей уничтожено")) {
            arcade.setTarget_ground_value(stringToInt(data.select("td:eq(" + "2" + ")").text()));
            rb.setTarget_ground_value(stringToInt(data.select("td:eq(" + "3" + ")").text()));
            sb.setTarget_ground_value(stringToInt(data.select("td:eq(" + "4" + ")").text()));
        }
    }


    void set() {
        Elements roles = document.select("table[class=user-stat]").select("tr");
        for (Element role : roles) {
            parse(role);
        }
    }


    void save() {
        Session session;
        Transaction t;
        arcade.recalculate();
        rb.recalculate();
        sb.recalculate();
        session = HibernateFactory.getSession();
        t = session.beginTransaction();
        session.saveOrUpdate(arcade);
        session.saveOrUpdate(rb);
        session.saveOrUpdate(sb);
        t.commit();
        session.close();
    }

    @Override
    public void run() {
        prepare();
        set();
        save();
    }
}
