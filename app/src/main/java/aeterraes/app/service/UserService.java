package aeterraes.app.service;

import aeterraes.app.aspect.annotation.ExceptionHandling;
import aeterraes.app.aspect.annotation.LogTimeTracking;
import aeterraes.app.aspect.annotation.Loggable;
import aeterraes.app.aspect.annotation.ResultHandling;
import aeterraes.app.dataaccess.entity.User;
import aeterraes.app.dataaccess.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Setter
@Getter
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Loggable
    @ExceptionHandling
    @LogTimeTracking
    public void createUser(User user) {
        userRepository.save(user);
    }

    @Loggable
    @ExceptionHandling
    @LogTimeTracking
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    @Loggable
    @ResultHandling
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Loggable
    @ExceptionHandling
    @ResultHandling
    @LogTimeTracking
    public Optional<User> updateUser(int id, User user) {
        return userRepository.findById(id)
                .map(currentUser -> {
                    currentUser.setName(user.getName());
                    return userRepository.save(currentUser);
                });
    }

    @Loggable
    @ExceptionHandling
    @ResultHandling
    public boolean deleteUserById(int id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
