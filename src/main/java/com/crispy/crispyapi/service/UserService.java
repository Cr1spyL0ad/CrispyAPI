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
    public boolean update(User user) {
        try {
            userRepository.save(user);
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

    public UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setWorkspaces(new ArrayList<>());
        user.getWorkspaces().forEach(workspace -> {
            UserDto.UserWorkspaceDto userWorkspaceDto = new UserDto.UserWorkspaceDto(workspace.getId(), workspace.getName());
            userDto.getWorkspaces().add(userWorkspaceDto);
        });
        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", username)));
    }
}
