package ru.tusur.rbpo.lab2.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.tusur.rbpo.lab2.dto.UserRequest;
import ru.tusur.rbpo.lab2.model.User;
import ru.tusur.rbpo.lab2.service.RentalService;
import java.util.List;

@RestController @RequestMapping("/api/users")
public class UserController {
    private final RentalService service;
    public UserController(RentalService service) { this.service = service; }
    @GetMapping public List<User> all() { return service.users(); }
    @GetMapping("/{id}") public User one(@PathVariable Long id) { return service.user(id); }
    @PostMapping public ResponseEntity<User> create(@RequestBody UserRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(request)); }
    @PatchMapping("/{id}") public User update(@PathVariable Long id, @RequestBody UserRequest request) { return service.updateUser(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.deleteUser(id); }
}
