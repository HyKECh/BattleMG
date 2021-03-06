import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class AbstractStatistics implements Serializable {
    private static final long serialVersionUID = 3535126303617673639L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "MEDIUMINT UNSIGNED UNSIGNED")
    private int id;
    @Column(columnDefinition = "VARCHAR(25)", unique = true, nullable = false)
    private String login;


    @Column(name = "deaths_value", columnDefinition = "MEDIUMINT UNSIGNED", nullable = false)
    private int deaths_value;
    @Column(name = "win_rate", columnDefinition = "FLOAT", nullable = false)
    private float win_rate;
    @Column(name = "vehicles_in_battle", columnDefinition = "FLOAT", nullable = false)
    private float vehicles_in_battle;

    @Column(name = "air_frags_by_mission", columnDefinition = "FLOAT", nullable = false)
    private float air_frag_by_mission;

    @Column(name = "ground_frags_by_mission", columnDefinition = "FLOAT", nullable = false)
    private float ground_frag_by_mission;

    @Column(name = "air_frags_by_session", columnDefinition = "FLOAT", nullable = false)
    private float air_frag_by_session;

    @Column(name = "ground_frags_by_session", columnDefinition = "FLOAT", nullable = false)
    private float ground_frag_by_session;


    @Column(name = "mission_finished", columnDefinition = "MEDIUMINT UNSIGNED", nullable = false)
    private int mission_finished;

    @Column(name = "mission_victories", columnDefinition = "MEDIUMINT UNSIGNED", nullable = false)
    private int mission_victories;

    @Column(name = "random_session", columnDefinition = "MEDIUMINT UNSIGNED", nullable = false)
    private int random_session;
    @Column(name = "random_time_SPAA", columnDefinition = "MEDIUMINT UNSIGNED", nullable = false)
    private int random_time_SPAA;
    @Column(name = "random_time_attacker", columnDefinition = "MEDIUMINT UNSIGNED", nullable = false)
    private int random_time_attacker;
    @Column(name = "random_time_bomber", columnDefinition = "MEDIUMINT UNSIGNED", nullable = false)
    private int random_time_bomber;
    @Column(name = "random_time_fighter", columnDefinition = "MEDIUMINT UNSIGNED", nullable = false)
    private int random_time_fighter;
    @Column(name = "random_time_tank", columnDefinition = "MEDIUMINT UNSIGNED", nullable = false)
    private int random_time_tank;
    @Column(name = "random_time_tank_destroyer", columnDefinition = "MEDIUMINT UNSIGNED", nullable = false)
    private int random_time_tank_destroyer;
    @Column(name = "random_time_tank_heavy", columnDefinition = "MEDIUMINT UNSIGNED", nullable = false)
    private int random_time_tank_heavy;
    @Column(name = "random_victories", columnDefinition = "MEDIUMINT UNSIGNED", nullable = false)
    private int random_victories;

    @Column(name = "target_air_value", columnDefinition = "MEDIUMINT UNSIGNED", nullable = false)
    private int target_air_value;

    @Column(name = "target_ground_value", columnDefinition = "MEDIUMINT UNSIGNED", nullable = false)
    private int target_ground_value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    public int getDeaths_value() {
        return deaths_value;
    }

    public void setDeaths_value(int deaths_value) {
        this.deaths_value = deaths_value;
    }

    public int getMission_finished() {
        return mission_finished;
    }

    public void setMission_finished(int mission_finished) {
        this.mission_finished = mission_finished;
    }

    public int getMission_victories() {
        return mission_victories;
    }

    public void setMission_victories(int mission_victories) {
        this.mission_victories = mission_victories;
    }

    public int getRandom_session() {
        return random_session;
    }

    public void setRandom_session(int random_session) {
        this.random_session = random_session;
    }

    public int getRandom_time_SPAA() {
        return random_time_SPAA;
    }

    public void setRandom_time_SPAA(int random_time_SPAA) {
        this.random_time_SPAA = random_time_SPAA;
    }

    public int getRandom_time_attacker() {
        return random_time_attacker;
    }

    public void setRandom_time_attacker(int random_time_attacker) {
        this.random_time_attacker = random_time_attacker;
    }

    public int getRandom_time_bomber() {
        return random_time_bomber;
    }

    public void setRandom_time_bomber(int random_time_bomber) {
        this.random_time_bomber = random_time_bomber;
    }

    public int getRandom_time_fighter() {
        return random_time_fighter;
    }

    public void setRandom_time_fighter(int random_time_fighter) {
        this.random_time_fighter = random_time_fighter;
    }

    public int getRandom_time_tank() {
        return random_time_tank;
    }

    public void setRandom_time_tank(int random_time_tank) {
        this.random_time_tank = random_time_tank;
    }

    public int getRandom_time_tank_destroyer() {
        return random_time_tank_destroyer;
    }

    public void setRandom_time_tank_destroyer(int random_time_tank_destroyer) {
        this.random_time_tank_destroyer = random_time_tank_destroyer;
    }

    public int getRandom_time_tank_heavy() {
        return random_time_tank_heavy;
    }

    public void setRandom_time_tank_heavy(int random_time_tank_heavy) {
        this.random_time_tank_heavy = random_time_tank_heavy;
    }

    public int getRandom_victories() {
        return random_victories;
    }

    public void setRandom_victories(int random_victories) {
        this.random_victories = random_victories;
    }

    public int getTarget_air_value() {
        return target_air_value;
    }

    public void setTarget_air_value(int target_air_value) {
        this.target_air_value = target_air_value;
    }

    public int getTarget_ground_value() {
        return target_ground_value;
    }

    public void setTarget_ground_value(int target_ground_value) {
        this.target_ground_value = target_ground_value;
    }


    @Override
    public int hashCode() {
        return login.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractStatistics that = (AbstractStatistics) o;

        return login.equals(that.login);
    }

    public float getWin_rate() {
        return win_rate;
    }

    public void setWin_rate(float win_rate) {
        this.win_rate = win_rate;
    }

    public float getVehicles_in_battle() {
        return vehicles_in_battle;
    }

    public void setVehicles_in_battle(float vehicles_in_battle) {
        this.vehicles_in_battle = vehicles_in_battle;
    }

    public float getAir_frag_by_mission() {
        return air_frag_by_mission;
    }

    public void setAir_frag_by_mission(float air_frag_by_mission) {
        this.air_frag_by_mission = air_frag_by_mission;
    }

    public float getGround_frag_by_mission() {
        return ground_frag_by_mission;
    }

    public void setGround_frag_by_mission(float groun_frag_by_mission) {
        this.ground_frag_by_mission = groun_frag_by_mission;
    }

    public float getAir_frag_by_session() {
        return air_frag_by_session;
    }

    public void setAir_frag_by_session(float air_frag_by_session) {
        this.air_frag_by_session = air_frag_by_session;
    }

    public float getGround_frag_by_session() {
        return ground_frag_by_session;
    }

    public void setGround_frag_by_session(float ground_frag_by_session) {
        this.ground_frag_by_session = ground_frag_by_session;
    }

    public void recalculate() {

        if (mission_finished > 0) {
            vehicles_in_battle = (float) random_session / (float) mission_finished;
        }


        if (mission_finished > 0) {
            win_rate = (float) mission_victories / (float) mission_finished;
        }

        if (random_session > 0) {
            air_frag_by_session = (float) target_air_value / (float) random_session;
        }
        if (random_session > 0) {
            ground_frag_by_session = (float) target_ground_value / (float) random_session;
        }
        if (mission_finished > 0) {
            air_frag_by_mission = (float) target_air_value / (float) mission_finished;
        }
        if (mission_finished > 0) {
            ground_frag_by_mission = (float) target_ground_value / (float) mission_finished;
        }

    }
}
