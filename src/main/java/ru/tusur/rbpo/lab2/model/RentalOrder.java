package ru.tusur.rbpo.lab2.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record RentalOrder(Long id, Long userId, LocalDate startDate, LocalDate endDate,
                          String status, BigDecimal totalAmount, Instant createdAt) { }
