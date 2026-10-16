package ru.tusur.rbpo.lab2.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.tusur.rbpo.lab2.dto.RentalOrderRequest;
import ru.tusur.rbpo.lab2.model.RentalOrder;
import ru.tusur.rbpo.lab2.service.RentalService;
import java.util.List;

@RestController @RequestMapping("/api/rental-orders")
public class RentalOrderController {
    private final RentalService service;
    public RentalOrderController(RentalService service) { this.service = service; }
    @GetMapping public List<RentalOrder> all() { return service.orders(); }
    @GetMapping("/{id}") public RentalOrder one(@PathVariable Long id) { return service.order(id); }
    @PostMapping public ResponseEntity<RentalOrder> create(@RequestBody RentalOrderRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(service.createOrder(request)); }
    @PatchMapping("/{id}") public RentalOrder update(@PathVariable Long id, @RequestBody RentalOrderRequest request) { return service.updateOrder(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.deleteOrder(id); }
}
