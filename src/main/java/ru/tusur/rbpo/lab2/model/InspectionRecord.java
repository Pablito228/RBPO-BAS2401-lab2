package ru.tusur.rbpo.lab2.model;

import java.time.Instant;

public record InspectionRecord(Long id, Long rentalItemId, String inspectionType,
                               String condition, Instant createdAt) { }
