package ru.tusur.rbpo.lab2.dto;

import java.math.BigDecimal;

public record EquipmentUnitRequest(String inventoryNumber, String type, BigDecimal dailyPrice, String condition) { }
