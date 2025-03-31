package com.lookbook.user.infrastructure.adapters.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lookbook.base.domain.exceptions.EntityNotFoundException;
import com.lookbook.user.application.ports.services.UserService;
import com.lookbook.user.domain.aggregates.User;
import com.lookbook.user.domain.repositories.UserRepository;
import com.lookbook.user.domain.valueobjects.Email;
import com.lookbook.user.domain.valueobjects.Username;

/**
 * Implementation of the UserService interface.
 */
@Service
@Transactional
public class UserServiceAdapter implements UserService {

    private final UserRepository userRepository;

    /**
     * Constructor injection of dependencies.
     * 
     * @param userRepository the user repository
     */
    public UserServiceAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(Username.of(username));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(Email.of(email));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User activate(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));

        user.activate();
        return userRepository.save(user);
    }

    @Override
    public User deactivate(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));

        user.deactivate();
        return userRepository.save(user);
    }

    @Override
    public User updateEmail(UUID id, String newEmail) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));

        user.updateEmail(newEmail);
        return userRepository.save(user);
    }

    @Override
    public User changePassword(UUID id, String currentPassword, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));

        user.changePassword(currentPassword, newPassword);
        return userRepository.save(user);
    }

}