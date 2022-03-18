package com.soyoung.grad.service.model

import com.soyoung.grad.domain.equipment.Equipment

data class EquipmentReservationStatus(
    val equipment: Equipment,
    val isReserved: Boolean
)