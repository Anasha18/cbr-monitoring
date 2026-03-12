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
import repository.CurrencyRepository;
import repository.ExchangeRateRepository;
import repository.SubscriptionRepository;
import repository.UserRepository;
import service.CurrencyService;
import service.ExchangeRateService;
import service.SubscriptionService;
import service.UserService;
import sheduler.NotificationScheduler;

void main() {
    AppConfig appConfig = AppConfig.load();
    ObjectMapper objectMapper = ObjectMapperConfiguration.initJackson();
    SessionFactory sessionFactory = HibernateSessionFactoryProvider.build(appConfig);
    TransactionSessionManager transactionManager = new TransactionSessionManager(sessionFactory);


    CbrHttpClient cbrHttpClient = new CbrHttpClient(objectMapper, appConfig);

    UserRepository userRepository = new UserRepository(transactionManager);
    SubscriptionRepository subscriptionRepo = new SubscriptionRepository(transactionManager);
    CurrencyRepository currencyRepository = new CurrencyRepository(transactionManager);
    ExchangeRateRepository exchangeRateRepository = new ExchangeRateRepository(transactionManager);

    ExchangeRateService exchangeRateService = new ExchangeRateService(exchangeRateRepository);
    UserService userService = new UserService(userRepository);
    CurrencyService currencyService = new CurrencyService(currencyRepository, exchangeRateService, cbrHttpClient);
    SubscriptionService subscriptionService = new SubscriptionService(subscriptionRepo, userService, currencyService);


    CommandResolver commandResolver = new CommandResolver(currencyService, subscriptionService);

    CbrMonitoringBot bot = new CbrMonitoringBot(appConfig, userService, commandResolver);

    NotificationScheduler scheduler = new NotificationScheduler(subscriptionService, exchangeRateService, bot);
    scheduler.start();

    try {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(bot);
        new CountDownLatch(1).await();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
