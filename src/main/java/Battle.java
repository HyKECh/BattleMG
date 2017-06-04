import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "battles")
public class Battle implements Serializable {
    private static final Logger log = LogManager.getLogger(Battle.class);

    private static final long serialVersionUID = 9106318808349991105L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "MEDIUMINT UNSIGNED")
    private int id;

    @Column(name = "team_a", columnDefinition = "TINYINT UNSIGNED", nullable = false)
    private int teamA;
    @Column(name = "team_b", columnDefinition = "TINYINT UNSIGNED", nullable = false)
    private int teamB;
    @Column(name = "battle_id", unique = true, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private long battleId;
    @Column(columnDefinition = "TIME", nullable = false)
    private Time duration = Time.valueOf("00:00:00");
    @Column(nullable = false, columnDefinition = "VARCHAR(15)")
    private String mission = "";
    @Column(nullable = false, columnDefinition = "VARCHAR(100)")
    private String map = "";
    @Column(columnDefinition = "TIMESTAMP")
    private Timestamp start;
    @Column(columnDefinition = "TIMESTAMP")
    private Timestamp stop;
    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    @Enumerated(EnumType.STRING)
    private GameMode gameMode = GameMode.NULL;
    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    @Enumerated(EnumType.STRING)
    private GameType gameType = GameType.NULL;
    @Column(columnDefinition = "VARCHAR(14)", nullable = false)
    @Enumerated(EnumType.STRING)
    private Vehicles vehicles = Vehicles.NULL;
    @Column(name = "players", columnDefinition = "TINYINT UNSIGNED")
    private int players;

    static Battle getBattle(long battleId) {
        Battle battle;
        Session session = HibernateFactory.getSession();


        Query query = session.createQuery("from Battle where battleId = :battleId");
        query.setParameter("battleId", battleId);
        List list = query.getResultList();
        if (!list.isEmpty()) {
            battle = (Battle) query.getSingleResult();
        } else {
            Transaction transaction = session.beginTransaction();
            battle = new Battle();
            battle.setBattleId(battleId);
            session.saveOrUpdate(battle);
            transaction.commit();
        }
        session.close();
        return battle;
    }

    public Timestamp getStop() {
        return stop;
    }

    public void setStop(Timestamp stop) {
        this.stop = stop;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public int getTeamA() {
        return teamA;
    }

    public void setTeamA(int teamA) {
        this.teamA = teamA;
    }

    public int getTeamB() {
        return teamB;
    }

    public void setTeamB(int teamB) {
        this.teamB = teamB;
    }

    public String getMission() {
        return mission;
    }

    void setMission(String mission) {
        this.mission = mission;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public Vehicles getVehicles() {
        return vehicles;
    }

    public void setVehicles(Vehicles vehicles) {
        this.vehicles = vehicles;
    }

    public Time getDuration() {
        return duration;
    }

    public void setDuration(Time duration) {
        this.duration = duration;
    }


    public long getBattleId() {
        return battleId;
    }

    public void setBattleId(long battleId) {
        this.battleId = battleId;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    @Override
    public int hashCode() {
        return (int) (battleId ^ (battleId >>> 32));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Battle battle = (Battle) o;

        return battleId == battle.battleId;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp date) {
        this.start = date;
    }

    public void calc() {

    }
}
