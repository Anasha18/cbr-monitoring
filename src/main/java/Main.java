import bot.CbrMonitoringBot;
import bot.CommandResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import configuration.AppConfig;
import configuration.HibernateSessionFactoryProvider;
import configuration.ObjectMapperConfiguration;
import configuration.TransactionSessionManager;
import integration.CbrHttpClient;
import org.hibernate.SessionFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import repository.UserRepository;
import service.UserService;

void main() {
    AppConfig appConfig = AppConfig.load();
    SessionFactory sessionFactory = HibernateSessionFactoryProvider.build(appConfig);
    TransactionSessionManager transactionManager = new TransactionSessionManager(sessionFactory);
    UserRepository userRepository = new UserRepository(transactionManager);
    UserService userService = new UserService(userRepository);
    ObjectMapper objectMapper = ObjectMapperConfiguration.initJackson();
    CbrHttpClient cbrHttpClient = new CbrHttpClient(objectMapper, appConfig);
    CommandResolver commandResolver = new CommandResolver(cbrHttpClient);
    CbrMonitoringBot bot = new CbrMonitoringBot(appConfig, userService, commandResolver);

    try {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(bot);
        new CountDownLatch(1).await();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
