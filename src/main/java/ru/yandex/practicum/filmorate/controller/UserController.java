package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
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
    public User getByIdUser(@PathVariable int id) {
        return userService.getByIdUser(id);
    }

    @DeleteMapping("/{id}")
    public User deleteByIdUser(@PathVariable int id) {
        return userService.deleteByIdUser(id);
    }

    @PutMapping("/{firstId}/friends/{secondId}")
    public List<User> addFriends(@PathVariable int firstId, @PathVariable int secondId) {
        return userService.addFriends(firstId, secondId);
    }

    @DeleteMapping("/{firstId}/friends/{secondId}")
    public List<User> removeFriends(@PathVariable int firstId, @PathVariable int secondId) {
        return userService.removeFriends(firstId, secondId);
    }

    @GetMapping("{id}/friends")
    public List<User> getFriendsListById(@PathVariable int id) {
        return userService.getFriendsListById(id);
    }

    @GetMapping("/{firstId}/friends/common/{secondId}")
    public List<User> getCommonFriendsList(@PathVariable int firstId, @PathVariable int secondId) {
        return userService.getCommonFriendsList(firstId, secondId);
    }
}