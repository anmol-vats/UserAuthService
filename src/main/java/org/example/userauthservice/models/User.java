package org.example.userauthservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name="users") //user is reserved in mysql, so specified the table name as "users"
public class User extends BaseModel {
    private String name;
    private String emailId;
    private String password;
    private String phoneNumber;
    @ManyToMany
    private List<Role> roles =  new ArrayList<>();
}
