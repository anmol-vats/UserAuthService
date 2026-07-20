package org.example.userauthservice.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthservice.models.Role;
import org.example.userauthservice.models.Session;
import org.example.userauthservice.models.State;
import org.example.userauthservice.models.User;
import org.example.userauthservice.repos.RoleRepo;
import org.example.userauthservice.repos.SessionRepo;
import org.example.userauthservice.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

@Service
public class AuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SessionRepo sessionRepo;

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
        user.setState(State.ACTIVE);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setPhoneNumber(phoneNumber);

        //check and assign role
        Role role = null;
        Optional<Role> roleOptional = roleRepo.findRoleByValue("NON_ADMIN");
        if(roleOptional.isEmpty()) {
            role = new Role();
            role.setState(State.ACTIVE);
            role.setCreatedAt(new Date());
            role.setValue("NON_ADMIN");
            roleRepo.save(role);
        } else {
            role = roleOptional.get();
        }
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);

        return  userRepo.save(user);
    }

    public Pair<User, String> login(String email, String password) {
        Optional<User> userOptional = userRepo.findByEmailId(email);
        if(userOptional.isEmpty()) {
            throw new RuntimeException("Please Signup first");
        }

        User user = userOptional.get();

        if(!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        //generate token
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", user.getId());
        List<String> roleStrings = new ArrayList<>();
        for (Role role : user.getRoles()) {
            roleStrings.add(role.getValue());
        }
        payload.put("permissions", roleStrings);
        Long currentTimeInMillis = System.currentTimeMillis();
        payload.put("createdAt", currentTimeInMillis);
        payload.put("expire", currentTimeInMillis+10000);
        payload.put("issued_by", "engineer");

        MacAlgorithm macAlgorithm = Jwts.SIG.HS256;
        SecretKey secretKey = macAlgorithm.key().build();

        String token = Jwts.builder().claims(payload).signWith(secretKey).compact();

        Session session = new Session();
        session.setCreatedAt(new Date());
        session.setToken(token);
        session.setState(State.ACTIVE);
        session.setUser(user);
        sessionRepo.save(session);

        return new Pair<>(user, token);
    }

    public Boolean validateToken(String token){

        Optional<Session> optionalSession = sessionRepo.findByToken(token);
        return optionalSession.isPresent();
    }
}
