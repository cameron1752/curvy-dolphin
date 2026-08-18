package com.biy.social.curvydolphin.controller;

import com.biy.social.curvydolphin.model.User;
import com.biy.social.curvydolphin.service.AuthorizationService;
import com.biy.social.curvydolphin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AuthorizationService authorizationService;

    @GetMapping
    public ResponseEntity<User> getUser(@RequestHeader(value = "traceId") String traceId,
                                         @RequestHeader(value = "user_id", required = false) long id){
        if (id == 0){
            return ResponseEntity.ok(authorizationService.getCurrentAccount());
        } else {
            return ResponseEntity.ok(userService.getUserById(id));
        }
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestHeader(value = "traceId") String traceId,
                                            @RequestBody User user){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @PatchMapping
    public ResponseEntity<User> updateUser(@RequestHeader(value = "traceId") String traceId,
                                           @RequestHeader(value = "user_id") long id,
                                           @RequestBody User user){
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteUser(@RequestHeader(value = "traceId") String traceId,
                                                          @RequestHeader(value = "user_id") long id){
        userService.deleteUser(id);
        Map<String, String> response = new HashMap<String, String>();
        response.put("message", "User deleted with ID: " + id);
        return ResponseEntity.ok(response);
    }

}
