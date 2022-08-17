package com.witcher.gallery.controllers;

import com.witcher.gallery.models.dtos.UserDto;
import com.witcher.gallery.models.entities.User;
import com.witcher.gallery.services.CustomUserDetailsManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final CustomUserDetailsManager customUserDetailsManager;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(
            CustomUserDetailsManager customUserDetailsManager,
            ModelMapper modelMapper
    ) {
        this.customUserDetailsManager = customUserDetailsManager;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createUser(@RequestBody UserDto userDTO) {
        UserDetails user = this.modelMapper.map(userDTO, UserDetails.class);

        this.customUserDetailsManager.createUser(user);

        ResponseEntity<HttpStatus> responseEntity = new ResponseEntity( HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping
    public ResponseEntity<HttpStatus> getUser() {
        List<User> users = this.customUserDetailsManager.findAllUsers();
        List<UserDto> userDtos = new LinkedList<>();

        users.forEach(user -> userDtos.add(this.modelMapper.map(user, UserDto.class)));

        return new ResponseEntity(userDtos, HttpStatus.OK);
    }
}
