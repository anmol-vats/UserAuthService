package org.example.userauthservice.controllers;

import org.example.userauthservice.dtos.UserDto;
import org.example.userauthservice.models.Role;
import org.example.userauthservice.models.User;
import org.example.userauthservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public UserDto getUserDetails(@PathVariable Long id) {
        User user = userService.getUserDetails(id);
        if(user == null) return null;
        return from(user);
    }

    public UserDto from(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmailId(user.getEmailId());
        userDto.setName(user.getName());
        userDto.setName(user.getName());

        return userDto;
    }
}
