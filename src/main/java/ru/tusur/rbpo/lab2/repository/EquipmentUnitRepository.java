package ru.tusur.rbpo.lab2.repository;

import org.springframework.stereotype.Repository;
import ru.tusur.rbpo.lab2.model.EquipmentUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class EquipmentUnitRepository {
    private final Map<Long, EquipmentUnit> data = new LinkedHashMap<>();
    private final AtomicLong sequence = new AtomicLong();
    public List<EquipmentUnit> findAll() { return new ArrayList<>(data.values()); }
    public Optional<EquipmentUnit> findById(Long id) { return Optional.ofNullable(data.get(id)); }
    public EquipmentUnit save(EquipmentUnit unit) { Long id = unit.id() == null ? sequence.incrementAndGet() : unit.id(); EquipmentUnit saved = new EquipmentUnit(id, unit.inventoryNumber(), unit.type(), unit.dailyPrice(), unit.condition()); data.put(id, saved); return saved; }
    public void delete(Long id) { data.remove(id); }
}
