package ru.tusur.rbpo.lab2.model;

import java.math.BigDecimal;

public record RentalItem(Long id, Long rentalOrderId, Long equipmentUnitId,
                         BigDecimal fixedDailyPrice, String returnStatus) { }
