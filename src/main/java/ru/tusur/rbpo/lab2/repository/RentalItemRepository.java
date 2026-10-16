package ru.tusur.rbpo.lab2.repository;

import org.springframework.stereotype.Repository;
import ru.tusur.rbpo.lab2.model.RentalItem;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class RentalItemRepository {
    private final Map<Long, RentalItem> data = new LinkedHashMap<>();
    private final AtomicLong sequence = new AtomicLong();
    public List<RentalItem> findAll() { return new ArrayList<>(data.values()); }
    public Optional<RentalItem> findById(Long id) { return Optional.ofNullable(data.get(id)); }
    public List<RentalItem> findByOrderId(Long orderId) { return data.values().stream().filter(i -> i.rentalOrderId().equals(orderId)).toList(); }
    public List<RentalItem> findByEquipmentUnitId(Long unitId) { return data.values().stream().filter(i -> i.equipmentUnitId().equals(unitId)).toList(); }
    public RentalItem save(RentalItem item) { Long id = item.id() == null ? sequence.incrementAndGet() : item.id(); RentalItem saved = new RentalItem(id, item.rentalOrderId(), item.equipmentUnitId(), item.fixedDailyPrice(), item.returnStatus()); data.put(id, saved); return saved; }
    public void delete(Long id) { data.remove(id); }
}
