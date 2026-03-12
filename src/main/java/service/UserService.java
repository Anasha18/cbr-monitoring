package service;

import domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import repository.UserRepository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public User findByTelegramId(Long telegramId) {
        return userRepository.findByTelegramId(telegramId).orElseGet(() -> {
            log.atError().addKeyValue("telegramId", telegramId).log("User with telegramId not found");
            return null;
        });
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void getOrCreateUserByTelegramId(String username, Long telegramId) {
        var foundedUser = findByTelegramId(telegramId);

        if (foundedUser != null) {
            return;
        }

        var newUser = User.builder()
                .telegramId(telegramId)
                .username(username)
                .createdAt(LocalDateTime.now())
                .build();

        save(newUser);
    }
}
