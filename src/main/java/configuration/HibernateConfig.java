package configuration;

public record HibernateConfig(
        String hbm2ddlAuto,
        String dialect,
        boolean showSql
) {
}
