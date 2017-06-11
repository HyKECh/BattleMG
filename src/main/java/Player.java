import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Entity
@Table(name = "players")
public class Player implements Serializable {
    private static final Logger log = LogManager.getLogger(Player.class);
    private static final long serialVersionUID = 1333929301315965245L;
    @Column(columnDefinition = "TINYINT UNSIGNED", nullable = false)
    int level;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "MEDIUMINT UNSIGNED")
    private int Id;
    @Column(name = "login", columnDefinition = "VARCHAR(28)", unique = true, nullable = false)
    private String login;
    @Column(name = "squadron", columnDefinition = "VARCHAR(16) CHARACTER SET utf16 COLLATE utf16_general_ci")
    private String squalron;
    @Column(columnDefinition = "DATE")
    private Date registered;
    @Column(name = "arcade_air_perf", columnDefinition = "FLOAT", nullable = false)
    private float arcadeAirPerf;
    @Column(name = "arcade_ground_perf", columnDefinition = "FLOAT", nullable = false)
    private float arcadeGroundPerf;
    @Column(name = "rb_air_perf", columnDefinition = "FLOAT", nullable = false)
    private float rbAirPerf;
    @Column(name = "rb_ground_perf", columnDefinition = "FLOAT", nullable = false)
    private float rbGroundPerf;
    @Column(name = "sb_air_perf", columnDefinition = "FLOAT", nullable = false)
    private float sbAirPerf;
    @Column(name = "sb_ground_perf", columnDefinition = "FLOAT", nullable = false)
    private float sbGroundPerf;
    @Column(name = "is_banned", columnDefinition = "BOOLEAN", nullable = false)
    private boolean isBanned;
    @Column(name = "us_vehicles", columnDefinition = "TINYINT UNSIGNED", nullable = false)
    private int usVehicles;
    @Column(name = "us_elite_vehicles", columnDefinition = "TINYINT UNSIGNED", nullable = false)
    private int usEliteVehicles;
    @Column(name = "us_medals", columnDefinition = "TINYINT UNSIGNED", nullable = false)
    private int usMedals;
    @Column(name = "ussr_vehicles", columnDefinition = "TINYINT UNSIGNED", nullable = false)
    private int ussrVehicles;
    @Column(name = "ussr_elite_vehicles", columnDefinition = "TINYINT UNSIGNED", nullable = false)
    private int ussrEliteVehicles;
    @Column(name = "ussr_medals", columnDefinition = "TINYINT UNSIGNED", nullable = false)
    private int ussrMedals;
    @Column(name = "uk_vehicles", columnDefinition = "TINYINT UNSIGNED", nullable = false)
    private int ukVehicles;
    @Column(name = "uk_elite_vehicles", columnDefinition = "TINYINT UNSIGNED", nullable = false)
    private int ukEliteVehicles;
    @Column(name = "uk_medals", columnDefinition = "TINYINT UNSIGNED", nullable = false)
    private int ukMedals;
    @Column(name = "ge_vehicles", columnDefinition = "TINYINT UNSIGNED", nullable = false)
    private int geVehicles;
    @Column(name = "ge_elite_vehicles", columnDefinition = "TINYINT UNSIGNED", nullable = false)
    private int geEliteVehicles;
    @Column(name = "ge_medals", columnDefinition = "TINYINT UNSIGNED", nullable = false)
    private int geMedals;
    @Column(name = "jp_vehicles", columnDefinition = "TINYINT UNSIGNED", nullable = false)
    private int jpVehicles;
    @Column(name = "jp__elite_vehicles", columnDefinition = "TINYINT UNSIGNED", nullable = false)
    private int jpEliteVehicles;
    @Column(name = "jp_medals", columnDefinition = "TINYINT UNSIGNED", nullable = false)
    private int jpMedals;

    static Player getPlayer(String login, Session session1, ReentrantLock reentrantLock) {
        Player player;

        Session session = HibernateFactory.getSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("from Player where login = :login");

        query.setParameter("login", login);
        reentrantLock.lock();
        List list = query.getResultList();
        if (!list.isEmpty()) {
            player = (Player) query.getSingleResult();

        } else {
            player = new Player();
            player.setLogin(login);
            session.saveOrUpdate(player);
        }
        reentrantLock.unlock();
        transaction.commit();
        session.close();

        return player;
    }

    public String getSqualron() {
        return squalron;
    }

    public void setSqualron(String squalron) {
        this.squalron = squalron;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Date getRegistered() {

        return registered;
    }

    public void setRegistered(Date registered) {
        this.registered = registered;
    }

    public float getArcadeAirPerf() {
        return arcadeAirPerf;
    }

    public void setArcadeAirPerf(float arcadeAirPerf) {
        this.arcadeAirPerf = arcadeAirPerf;
    }

    public float getArcadeGroundPerf() {
        return arcadeGroundPerf;
    }

    public void setArcadeGroundPerf(float arcadeGroundPerf) {
        this.arcadeGroundPerf = arcadeGroundPerf;
    }

    public float getRbAirPerf() {
        return rbAirPerf;
    }

    public void setRbAirPerf(float rbAirPerf) {
        this.rbAirPerf = rbAirPerf;
    }

    public float getRbGroundPerf() {
        return rbGroundPerf;
    }

    public void setRbGroundPerf(float rbGroundPerf) {
        this.rbGroundPerf = rbGroundPerf;
    }

    public float getSbAirPerf() {
        return sbAirPerf;
    }

    public void setSbAirPerf(float sbAirPerf) {
        this.sbAirPerf = sbAirPerf;
    }

    public float getSbGroundPerf() {
        return sbGroundPerf;
    }

    public void setSbGroundPerf(float sbGroundPerf) {
        this.sbGroundPerf = sbGroundPerf;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public int getUsVehicles() {
        return usVehicles;
    }

    public void setUsVehicles(int usVehicles) {
        this.usVehicles = usVehicles;
    }

    public int getUsEliteVehicles() {
        return usEliteVehicles;
    }

    public void setUsEliteVehicles(int usEliteVehicles) {
        this.usEliteVehicles = usEliteVehicles;
    }

    public int getUsMedals() {
        return usMedals;
    }

    public void setUsMedals(int usMedals) {
        this.usMedals = usMedals;
    }

    public int getUssrVehicles() {
        return ussrVehicles;
    }

    public void setUssrVehicles(int ussrVehicles) {
        this.ussrVehicles = ussrVehicles;
    }

    public int getUssrEliteVehicles() {
        return ussrEliteVehicles;
    }

    public void setUssrEliteVehicles(int ussrEliteVehicles) {
        this.ussrEliteVehicles = ussrEliteVehicles;
    }

    public int getUssrMedals() {
        return ussrMedals;
    }

    public void setUssrMedals(int ussrMedals) {
        this.ussrMedals = ussrMedals;
    }

    public int getUkVehicles() {
        return ukVehicles;
    }

    public void setUkVehicles(int ukVehicles) {
        this.ukVehicles = ukVehicles;
    }

    public int getUkEliteVehicles() {
        return ukEliteVehicles;
    }

    public void setUkEliteVehicles(int ukEliteVehicles) {
        this.ukEliteVehicles = ukEliteVehicles;
    }

    public int getUkMedals() {
        return ukMedals;
    }

    public void setUkMedals(int ukMedals) {
        this.ukMedals = ukMedals;
    }

    public int getGeVehicles() {
        return geVehicles;
    }

    public void setGeVehicles(int geVehicles) {
        this.geVehicles = geVehicles;
    }

    public int getGeEliteVehicles() {
        return geEliteVehicles;
    }

    public void setGeEliteVehicles(int geEliteVehicles) {
        this.geEliteVehicles = geEliteVehicles;
    }

    public int getGeMedals() {
        return geMedals;
    }

    public void setGeMedals(int geMedals) {
        this.geMedals = geMedals;
    }

    public int getJpVehicles() {
        return jpVehicles;
    }

    public void setJpVehicles(int jpVehicles) {
        this.jpVehicles = jpVehicles;
    }

    public int getJpEliteVehicles() {
        return jpEliteVehicles;
    }

    public void setJpEliteVehicles(int jpEliteVehicles) {
        this.jpEliteVehicles = jpEliteVehicles;
    }

    public int getJpMedals() {
        return jpMedals;
    }

    public void setJpMedals(int jpMedals) {
        this.jpMedals = jpMedals;
    }

    @Override
    public int hashCode() {
        return login.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return login.equals(player.login);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
