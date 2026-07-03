package org.example.userauthservice.services;

import org.example.userauthservice.models.User;
import org.example.userauthservice.repos.RoleRepo;
import org.example.userauthservice.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User signup(String name,
        String emailId,
        String password,
        String phoneNumber
    ) {
        Optional<User> userOptional = userRepo.findByEmailId(emailId);
        if(userOptional.isPresent()) {
            throw new RuntimeException("User with email id " + emailId + " already exists");
        }

        User user = new User();
        user.setEmailId(emailId);
        user.setCreatedAt(new Date());
        user.setName(name);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setPhoneNumber(phoneNumber);

        return  userRepo.save(user);
    }

    public User login(String email,  String password) {
        Optional<User> userOptional = userRepo.findByEmailId(email);
        if(userOptional.isEmpty()) {
            throw new RuntimeException("Please Signup first");
        }

        User user = userOptional.get();

        if(!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        return  user;
    }
}
