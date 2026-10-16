package ru.tusur.rbpo.lab2.service;

import org.springframework.stereotype.Service;
import ru.tusur.rbpo.lab2.dto.*;
import ru.tusur.rbpo.lab2.exception.NotFoundException;
import ru.tusur.rbpo.lab2.model.*;
import ru.tusur.rbpo.lab2.repository.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class RentalService {
    private final UserRepository users;
    private final EquipmentUnitRepository equipment;
    private final RentalOrderRepository orders;
    private final RentalItemRepository items;
    private final InspectionRecordRepository inspections;

    public RentalService(UserRepository users, EquipmentUnitRepository equipment, RentalOrderRepository orders,
                         RentalItemRepository items, InspectionRecordRepository inspections) {
        this.users = users; this.equipment = equipment; this.orders = orders; this.items = items; this.inspections = inspections;
    }

    public List<User> users() { return users.findAll(); }
    public User user(Long id) { return users.findById(id).orElseThrow(() -> missing("User", id)); }
    public User createUser(UserRequest r) { required(r.name(), "name"); required(r.email(), "email"); return users.save(new User(null, r.name(), r.email())); }
    public User updateUser(Long id, UserRequest r) { User old = user(id); return users.save(new User(id, value(r.name(), old.name()), value(r.email(), old.email()))); }
    public void deleteUser(Long id) { user(id); orders.findAll().stream().filter(o -> o.userId().equals(id)).map(RentalOrder::id).toList().forEach(this::deleteOrder); users.delete(id); }

    public List<EquipmentUnit> equipment() { return equipment.findAll(); }
    public EquipmentUnit equipment(Long id) { return equipment.findById(id).orElseThrow(() -> missing("Equipment unit", id)); }
    public EquipmentUnit createEquipment(EquipmentUnitRequest r) { required(r.inventoryNumber(), "inventoryNumber"); required(r.type(), "type"); positive(r.dailyPrice(), "dailyPrice"); required(r.condition(), "condition"); return equipment.save(new EquipmentUnit(null, r.inventoryNumber(), r.type(), r.dailyPrice(), r.condition())); }
    public EquipmentUnit updateEquipment(Long id, EquipmentUnitRequest r) { EquipmentUnit old = equipment(id); BigDecimal price = r.dailyPrice() == null ? old.dailyPrice() : r.dailyPrice(); positive(price, "dailyPrice"); return equipment.save(new EquipmentUnit(id, value(r.inventoryNumber(), old.inventoryNumber()), value(r.type(), old.type()), price, value(r.condition(), old.condition()))); }
    public void deleteEquipment(Long id) { equipment(id); items.findByEquipmentUnitId(id).stream().map(RentalItem::id).toList().forEach(this::deleteItem); equipment.delete(id); }

    public List<RentalOrder> orders() { return orders.findAll(); }
    public RentalOrder order(Long id) { return orders.findById(id).orElseThrow(() -> missing("Rental order", id)); }
    public RentalOrder createOrder(RentalOrderRequest r) { validateOrderRequest(r); user(r.userId()); return orders.save(new RentalOrder(null, r.userId(), r.startDate(), r.endDate(), value(r.status(), "NEW"), BigDecimal.ZERO, Instant.now())); }
    public RentalOrder updateOrder(Long id, RentalOrderRequest r) { RentalOrder old = order(id); Long userId = r.userId() == null ? old.userId() : r.userId(); user(userId); RentalOrder candidate = new RentalOrder(id, userId, r.startDate() == null ? old.startDate() : r.startDate(), r.endDate() == null ? old.endDate() : r.endDate(), value(r.status(), old.status()), old.totalAmount(), old.createdAt()); if (candidate.endDate().isBefore(candidate.startDate())) throw new IllegalArgumentException("endDate must not be before startDate"); return orders.save(candidate); }
    public void deleteOrder(Long id) { order(id); items.findByOrderId(id).stream().map(RentalItem::id).toList().forEach(this::deleteItem); orders.delete(id); }

    public List<RentalItem> items() { return items.findAll(); }
    public RentalItem item(Long id) { return items.findById(id).orElseThrow(() -> missing("Rental item", id)); }
    public RentalItem createItem(RentalItemRequest r) { required(r.rentalOrderId(), "rentalOrderId"); required(r.equipmentUnitId(), "equipmentUnitId"); order(r.rentalOrderId()); EquipmentUnit unit = equipment(r.equipmentUnitId()); RentalItem item = items.save(new RentalItem(null, r.rentalOrderId(), r.equipmentUnitId(), unit.dailyPrice(), value(r.returnStatus(), "NOT_RETURNED"))); recalculate(item.rentalOrderId()); return item; }
    public RentalItem updateItem(Long id, RentalItemRequest r) { RentalItem old = item(id); Long orderId = r.rentalOrderId() == null ? old.rentalOrderId() : r.rentalOrderId(); Long unitId = r.equipmentUnitId() == null ? old.equipmentUnitId() : r.equipmentUnitId(); order(orderId); EquipmentUnit unit = equipment(unitId); RentalItem updated = items.save(new RentalItem(id, orderId, unitId, unit.dailyPrice(), value(r.returnStatus(), old.returnStatus()))); recalculate(old.rentalOrderId()); if (!old.rentalOrderId().equals(orderId)) recalculate(orderId); return updated; }
    public void deleteItem(Long id) { RentalItem old = item(id); inspections.findByRentalItemId(id).stream().map(InspectionRecord::id).toList().forEach(inspections::delete); items.delete(id); recalculate(old.rentalOrderId()); }

    public List<InspectionRecord> inspections() { return inspections.findAll(); }
    public InspectionRecord inspection(Long id) { return inspections.findById(id).orElseThrow(() -> missing("Inspection record", id)); }
    public InspectionRecord createInspection(InspectionRecordRequest r) { required(r.rentalItemId(), "rentalItemId"); required(r.inspectionType(), "inspectionType"); required(r.condition(), "condition"); item(r.rentalItemId()); return inspections.save(new InspectionRecord(null, r.rentalItemId(), r.inspectionType(), r.condition(), Instant.now())); }
    public InspectionRecord updateInspection(Long id, InspectionRecordRequest r) { InspectionRecord old = inspection(id); Long itemId = r.rentalItemId() == null ? old.rentalItemId() : r.rentalItemId(); item(itemId); return inspections.save(new InspectionRecord(id, itemId, value(r.inspectionType(), old.inspectionType()), value(r.condition(), old.condition()), old.createdAt())); }
    public void deleteInspection(Long id) { inspection(id); inspections.delete(id); }

    private void recalculate(Long orderId) { RentalOrder old = order(orderId); BigDecimal total = items.findByOrderId(orderId).stream().map(RentalItem::fixedDailyPrice).reduce(BigDecimal.ZERO, BigDecimal::add); orders.save(new RentalOrder(old.id(), old.userId(), old.startDate(), old.endDate(), old.status(), total, old.createdAt())); }
    private void validateOrderRequest(RentalOrderRequest r) { required(r.userId(), "userId"); required(r.startDate(), "startDate"); required(r.endDate(), "endDate"); if (r.endDate().isBefore(r.startDate())) throw new IllegalArgumentException("endDate must not be before startDate"); }
    private NotFoundException missing(String type, Long id) { return new NotFoundException(type + " " + id + " not found"); }
    private <T> void required(T value, String name) { if (value == null || value instanceof String s && s.isBlank()) throw new IllegalArgumentException(name + " is required"); }
    private void positive(BigDecimal value, String name) { required(value, name); if (value.signum() <= 0) throw new IllegalArgumentException(name + " must be positive"); }
    private <T> T value(T newValue, T oldValue) { return newValue == null ? oldValue : newValue; }
}
