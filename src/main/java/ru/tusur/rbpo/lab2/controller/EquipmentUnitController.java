package ru.tusur.rbpo.lab2.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.tusur.rbpo.lab2.dto.EquipmentUnitRequest;
import ru.tusur.rbpo.lab2.model.EquipmentUnit;
import ru.tusur.rbpo.lab2.service.RentalService;
import java.util.List;

@RestController @RequestMapping("/api/equipment-units")
public class EquipmentUnitController {
    private final RentalService service;
    public EquipmentUnitController(RentalService service) { this.service = service; }
    @GetMapping public List<EquipmentUnit> all() { return service.equipment(); }
    @GetMapping("/{id}") public EquipmentUnit one(@PathVariable Long id) { return service.equipment(id); }
    @PostMapping public ResponseEntity<EquipmentUnit> create(@RequestBody EquipmentUnitRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(service.createEquipment(request)); }
    @PatchMapping("/{id}") public EquipmentUnit update(@PathVariable Long id, @RequestBody EquipmentUnitRequest request) { return service.updateEquipment(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.deleteEquipment(id); }
}
