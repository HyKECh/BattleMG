import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class GetProperties {
    static Properties get() {
        Properties prop = new Properties();
        try {
            InputStream input = new FileInputStream("config.properties");
            prop.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }
}
