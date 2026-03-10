package configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Getter
@Setter
public class AppConfig {
    private final TelegramConfig telegramConfig;
    private final CbrApiConfig cbrApiConfig;
    private final DbConfig dbConfig;
    private final HibernateConfig hibernateConfig;

    private AppConfig(
            TelegramConfig telegramConfig,
            CbrApiConfig cbrApiConfig,
            DbConfig dbConfig,
            HibernateConfig hibernateConfig
    ) {
        this.telegramConfig = telegramConfig;
        this.cbrApiConfig = cbrApiConfig;
        this.dbConfig = dbConfig;
        this.hibernateConfig = hibernateConfig;
    }

    public static AppConfig load() {
        Properties prop = new Properties();

        try (InputStream in = AppConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (in != null) prop.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Can't initialize project. " + e);
        }

        TelegramConfig telegramConfig = new TelegramConfig(
                prop.getProperty("telegram.bot.token"),
                prop.getProperty("telegram.bot.username")
        );

        CbrApiConfig cbrApiConfig = new CbrApiConfig(
                prop.getProperty("cbr.api.url")
        );

        DbConfig dbConfig = new DbConfig(
                prop.getProperty("db.url"),
                prop.getProperty("db.username"),
                prop.getProperty("db.password")
        );

        HibernateConfig hibernateConfig = new HibernateConfig(
                prop.getProperty("hibernate.hbm2ddl.auto"),
                prop.getProperty("hibernate.dialect"),
                Boolean.parseBoolean(prop.getProperty("hibernate.show-sql"))
        );


        return new AppConfig(
                telegramConfig,
                cbrApiConfig,
                dbConfig,
                hibernateConfig
        );
    }
}
