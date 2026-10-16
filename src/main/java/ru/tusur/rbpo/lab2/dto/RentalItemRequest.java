package ru.tusur.rbpo.lab2.dto;

public record RentalItemRequest(Long rentalOrderId, Long equipmentUnitId, String returnStatus) { }
