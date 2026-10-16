package ru.tusur.rbpo.lab2.repository;

import org.springframework.stereotype.Repository;
import ru.tusur.rbpo.lab2.model.InspectionRecord;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InspectionRecordRepository {
    private final Map<Long, InspectionRecord> data = new LinkedHashMap<>();
    private final AtomicLong sequence = new AtomicLong();
    public List<InspectionRecord> findAll() { return new ArrayList<>(data.values()); }
    public Optional<InspectionRecord> findById(Long id) { return Optional.ofNullable(data.get(id)); }
    public List<InspectionRecord> findByRentalItemId(Long itemId) { return data.values().stream().filter(r -> r.rentalItemId().equals(itemId)).toList(); }
    public InspectionRecord save(InspectionRecord record) { Long id = record.id() == null ? sequence.incrementAndGet() : record.id(); InspectionRecord saved = new InspectionRecord(id, record.rentalItemId(), record.inspectionType(), record.condition(), record.createdAt()); data.put(id, saved); return saved; }
    public void delete(Long id) { data.remove(id); }
}
