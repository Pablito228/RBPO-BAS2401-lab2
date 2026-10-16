package ru.tusur.rbpo.lab2.repository;

import org.springframework.stereotype.Repository;
import ru.tusur.rbpo.lab2.model.User;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {
    private final Map<Long, User> data = new LinkedHashMap<>();
    private final AtomicLong sequence = new AtomicLong();
    public List<User> findAll() { return new ArrayList<>(data.values()); }
    public Optional<User> findById(Long id) { return Optional.ofNullable(data.get(id)); }
    public User save(User user) { Long id = user.id() == null ? sequence.incrementAndGet() : user.id(); User saved = new User(id, user.name(), user.email()); data.put(id, saved); return saved; }
    public void delete(Long id) { data.remove(id); }
}
