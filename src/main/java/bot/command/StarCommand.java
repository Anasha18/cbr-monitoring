package bot.command;

public class StarCommand implements Command {
    @Override
    public String execute(String... args) {
        return """
                Привет! Я бот помощник получения курса валюты
                
                Доступные команды:
                /start - показать данное собщение
                /currency <название валюты>
                """;
    }
}
