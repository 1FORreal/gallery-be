package com.witcher.gallery.services;

import com.witcher.gallery.models.entities.User;
import com.witcher.gallery.repositories.UserRepository;
import com.witcher.gallery.security.SecurityUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsManager implements UserDetailsManager {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CustomUserDetailsManager(
            UserRepository userRepository,
            ModelMapper modelMapper
    ) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public List<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    private User findUserByUsername(String username) {
        Optional<User> optionalUser = this.userRepository.findUserByUsername(username);

        if(optionalUser.isPresent())
            return optionalUser.get();

        throw new RuntimeException();
    }

    @Override
    public void createUser(UserDetails user) {
        this.userRepository.save(this.modelMapper.map(user, User.class));
    }

    @Override
    public void updateUser(UserDetails user) {
        this.userRepository.save(this.modelMapper.map(user, User.class));
    }

    @Override
    public void deleteUser(String username) {
        Optional<User> optionalUser = this.userRepository.deleteUserByUsername(username);

        if(optionalUser.isEmpty())
            throw new RuntimeException();
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
    }

    @Override
    public boolean userExists(String username) {
        return this.userRepository.findUserByUsername(username).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = this.userRepository.findUserByUsername(username);

        if(optionalUser.isPresent()) {
            SecurityUser securityUser = new SecurityUser();
            securityUser.setUser(optionalUser.get());

            return securityUser;
        }

        throw new UsernameNotFoundException("Username not found " + username);
    }
}
