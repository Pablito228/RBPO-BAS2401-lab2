package ru.tusur.rbpo.lab2.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.tusur.rbpo.lab2.dto.RentalItemRequest;
import ru.tusur.rbpo.lab2.model.RentalItem;
import ru.tusur.rbpo.lab2.service.RentalService;
import java.util.List;

@RestController @RequestMapping("/api/rental-items")
public class RentalItemController {
    private final RentalService service;
    public RentalItemController(RentalService service) { this.service = service; }
    @GetMapping public List<RentalItem> all() { return service.items(); }
    @GetMapping("/{id}") public RentalItem one(@PathVariable Long id) { return service.item(id); }
    @PostMapping public ResponseEntity<RentalItem> create(@RequestBody RentalItemRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(service.createItem(request)); }
    @PatchMapping("/{id}") public RentalItem update(@PathVariable Long id, @RequestBody RentalItemRequest request) { return service.updateItem(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.deleteItem(id); }
}
