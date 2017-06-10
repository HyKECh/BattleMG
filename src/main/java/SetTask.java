import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

class SetTask implements Runnable {
    private static final Logger log = LogManager.getLogger(SetTask.class);
    private final static String BASE_URL = "http://warthunder.ru/ru/community/userinfo/?nick=";
    private Document document;
    private Player player;

    SetTask(Player player) {
        this.player = player;
    }


    void prepare() {
        try {
            document = Jsoup.connect(BASE_URL + player.getLogin()).timeout(10000).get();
        } catch (Exception e) {
            log.error("error during opening url " + e);
        }
    }

    @Override
    public void run() {
        prepare();
        Thread basic = new Thread(new GetStatistics(player, document));
        Thread advanced = new Thread(new GetAdvancedStatistics(player.getLogin(), document));
        basic.start();
        advanced.start();
    }
}
