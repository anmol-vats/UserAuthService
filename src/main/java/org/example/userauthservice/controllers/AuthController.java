package org.example.userauthservice.controllers;

import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthservice.dtos.LoginRequestDto;
import org.example.userauthservice.dtos.SignupRequestDto;
import org.example.userauthservice.dtos.UserDto;
import org.example.userauthservice.dtos.ValidateTokenRequestDto;
import org.example.userauthservice.models.Role;
import org.example.userauthservice.models.User;
import org.example.userauthservice.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupRequestDto signupRequestDto) {
            User user = authService.signup(signupRequestDto.getName(),
                    signupRequestDto.getEmail(),
                    signupRequestDto.getPassword(),
                    signupRequestDto.getPhoneNumber());

//            return from(user);
            UserDto userDto = from(user);
            return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto){
        Pair<User, String> response = authService.login(loginRequestDto.getEmail(),  loginRequestDto.getPassword());
        User user = response.a;
        String token = response.b;
        UserDto userDto = from(user);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.SET_COOKIE, token);
        return new ResponseEntity<>(userDto, headers, HttpStatus.OK);
    }

    @PostMapping
    public Boolean validateToken(@RequestBody ValidateTokenRequestDto validateTokenRequestDto){
        return authService.validateToken(validateTokenRequestDto.getToken());
    }

    public UserDto from(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmailId(user.getEmailId());
        userDto.setName(user.getName());
        userDto.setName(user.getName());
//        List<String> roleStrings = new ArrayList<>();
//        for (Role role : user.getRoles()) {
//            roleStrings.add(role.getValue());
//        }
//
//        userDto.setRoles(roleStrings);
        return userDto;
    }
}
