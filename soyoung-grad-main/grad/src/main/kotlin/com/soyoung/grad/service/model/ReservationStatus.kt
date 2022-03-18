package com.soyoung.grad.service.model

import com.soyoung.grad.domain.equipment.EquipmentType

data class ReservationStatus(
    val equipmentType: EquipmentType,
    val availableCount: AvailableCount
)