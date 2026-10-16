package ru.tusur.rbpo.lab2.repository;

import org.springframework.stereotype.Repository;
import ru.tusur.rbpo.lab2.model.RentalOrder;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class RentalOrderRepository {
    private final Map<Long, RentalOrder> data = new LinkedHashMap<>();
    private final AtomicLong sequence = new AtomicLong();
    public List<RentalOrder> findAll() { return new ArrayList<>(data.values()); }
    public Optional<RentalOrder> findById(Long id) { return Optional.ofNullable(data.get(id)); }
    public RentalOrder save(RentalOrder order) { Long id = order.id() == null ? sequence.incrementAndGet() : order.id(); RentalOrder saved = new RentalOrder(id, order.userId(), order.startDate(), order.endDate(), order.status(), order.totalAmount(), order.createdAt()); data.put(id, saved); return saved; }
    public void delete(Long id) { data.remove(id); }
}
