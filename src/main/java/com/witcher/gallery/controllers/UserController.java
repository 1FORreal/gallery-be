package com.witcher.gallery.controllers;

import com.witcher.gallery.models.dtos.UserDto;
import com.witcher.gallery.models.entities.User;
import com.witcher.gallery.services.JpaUserDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final JpaUserDetailsService jpaUserDetailsService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(
            JpaUserDetailsService jpaUserDetailsService,
            ModelMapper modelMapper
    ) {
        this.jpaUserDetailsService = jpaUserDetailsService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createUser(@RequestBody UserDto userDTO) {
        User user = this.modelMapper.map(userDTO, User.class);

        User savedUser = this.jpaUserDetailsService.createUser(user);
        UserDto savedUserDto = this.modelMapper.map(savedUser, UserDto.class);

        ResponseEntity<HttpStatus> responseEntity = new ResponseEntity(savedUserDto, HttpStatus.OK);
        return responseEntity;
    }
}
