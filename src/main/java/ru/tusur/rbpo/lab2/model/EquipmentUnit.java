package ru.tusur.rbpo.lab2.model;

import java.math.BigDecimal;

public record EquipmentUnit(Long id, String inventoryNumber, String type, BigDecimal dailyPrice, String condition) { }
