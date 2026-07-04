package org.example.userauthservice.dtos;

import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;
import org.example.userauthservice.models.Role;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class SignupRequestDto {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
}
