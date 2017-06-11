import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "players_in_battles")
public class PlayersInBattle implements Serializable {

    private static final long serialVersionUID = 1451007323850679900L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "INT UNSIGNED")
    private long id;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "player_id", referencedColumnName = "id", nullable = false)
    private Player player;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "battle_id", referencedColumnName = "id", nullable = false)
    private Battle battle;

    @Column(columnDefinition = "TINYINT UNSIGNED", nullable = false)
    private int team = 0;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

}
