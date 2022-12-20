package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping
    public Collection<User> findAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User getByIdUser(@PathVariable String id) {
        return userService.getByIdUser(id);
    }

    @DeleteMapping("/{id}")
    public User deleteByIdUser(@PathVariable Integer id) {
        return userService.deleteByIdUser(id);
    }

    @PutMapping("/{firstId}/friends/{secondId}")
    public void addFriends(@PathVariable String firstId, @PathVariable String secondId) {
        userService.addFriends(firstId, secondId);
    }

    @DeleteMapping("/{firstId}/friends/{secondId}")
    public void removeFriends(@PathVariable String firstId, @PathVariable String secondId) {
        userService.removeFriends(firstId, secondId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> findFriends(@PathVariable Integer id) {
        return userService.getFriendsListById(String.valueOf(id));
    }

    @GetMapping("/{firstId}/friends/common/{secondId}")
    public Collection<User> getCommonFriendsList(@PathVariable String firstId, @PathVariable String secondId) {
        return userService.getCommonFriendsList(firstId, secondId);
    }
}