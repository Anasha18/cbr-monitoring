package configuration;

public record HibernateConfig(
        Boolean hibernateShowSql,
        String hibernateDialect,
        String hibernateHbm2Ddl
) {
}
