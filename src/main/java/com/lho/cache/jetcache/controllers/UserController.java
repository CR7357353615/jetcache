package com.lho.cache.jetcache.controllers;

import com.lho.cache.jetcache.models.User;
import com.lho.cache.jetcache.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by lihao on 2018/9/20.
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> users() {
       return userService.users();
    }

    @GetMapping("/user")
    public User user(@RequestParam Integer userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/user")
    public void updateUser(@RequestBody User user) {
        userService.updateUser(user);
    }
}
