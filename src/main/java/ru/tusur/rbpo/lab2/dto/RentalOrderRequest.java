package ru.tusur.rbpo.lab2.dto;

import java.time.LocalDate;

public record RentalOrderRequest(Long userId, LocalDate startDate, LocalDate endDate, String status) { }
