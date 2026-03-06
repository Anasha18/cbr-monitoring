package configuration;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.HashMap;
import java.util.Map;

public class HibernateSessionFactoryProvider {
    private HibernateSessionFactoryProvider() {
    }

    public static SessionFactory build(AppConfig config) {
        Map<String, Object> settings = new HashMap<>();

        settings.put("hibernate.connection.driver_class", "org.postgresql.Driver");
        settings.put("hibernate.connection.url", config.getDbConfig().dbUrl());
        settings.put("hibernate.connection.username", config.getDbConfig().dbUsername());
        settings.put("hibernate.connection.password", config.getDbConfig().dbPassword());
        settings.put("hibernate.dialect", config.getHibernateConfig().hibernateDialect());
        settings.put("hibernate.show_sql", Boolean.toString(config.getHibernateConfig().hibernateShowSql()));
        settings.put("hibernate.hbm2ddl.auto", config.getHibernateConfig().hibernateHbm2Ddl());

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(settings)
                .build();

        try {
            return new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw new RuntimeException("Ошибка загрузки свойст с конфига: " + e);
        }
    }
}
