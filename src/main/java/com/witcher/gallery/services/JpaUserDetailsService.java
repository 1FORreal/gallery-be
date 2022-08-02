package com.witcher.gallery.services;

import com.witcher.gallery.entities.User;
import com.witcher.gallery.repositories.UserRepository;
import com.witcher.gallery.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JpaUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> userList = this.userRepository.findAll();

        Optional<User> optionalUser = this.userRepository.findUserByUsername(username);

        if(optionalUser.isPresent()) {
            SecurityUser securityUser = new SecurityUser();
            securityUser.setUser(optionalUser.get());

            return securityUser;
        }

        throw new UsernameNotFoundException("Username not found " + username);
    }

    public User createUser(User user) {
        User savedUser = this.userRepository.save(user);

        return savedUser;
    }
}
