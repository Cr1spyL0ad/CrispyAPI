package com.crispy.crispyapi.service;

import com.crispy.crispyapi.dto.SignUpRequest;
import com.crispy.crispyapi.dto.UserDto;
import com.crispy.crispyapi.model.User;
import com.crispy.crispyapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class UserService implements ServiceInterface<User>, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean create(User user) {
        try {
            userRepository.save(user);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    public boolean create(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setName(signUpRequest.getName());
        try {
            userRepository.save(user);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    public User read(Long id) throws Exception {
        return userRepository.findUserById(id).orElseThrow(() -> new Exception("User not found"));
    }

    @Override
    public List<User> readAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean update(User newUser, Long id) {
        newUser.setId(id);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        try {
            userRepository.save(newUser);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public boolean delete(Long id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteAll() {
        try {
            userRepository.deleteAll();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public UserDto convertToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                new ArrayList<>(user.getWorkspaces())
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", username)));
        return user;
    }
}
