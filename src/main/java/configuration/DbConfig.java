package configuration;

public record DbConfig(
        String dbUrl,
        String dbUsername,
        String dbPassword,
        String dbDriverClassName
) {
}
