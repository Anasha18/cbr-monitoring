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

    public User findById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));
    }

    public User findByTelegramId(Long telegramId) {
        return userRepository.findByTelegramId(telegramId).orElseThrow(() -> {
            log.atError().addKeyValue("telegramId", telegramId).log("User with telegramId not found");
            return new RuntimeException("User with telegramId " + telegramId + " not found");
        });
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User getOrCreateUserByTelegramId(String username, Long telegramId) {
        var foundedUser = findByTelegramId(telegramId);

        if (foundedUser != null) {
            return foundedUser;
        }

        var newUser = User.builder()
                .telegramId(telegramId)
                .username(username)
                .createdAt(LocalDateTime.now())
                .build();

        save(newUser);

        return newUser;
    }
}
