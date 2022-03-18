package com.soyoung.grad.service.model

import com.soyoung.grad.domain.equipment.EquipmentType
import com.soyoung.grad.domain.reservation.Reservation

data class MyReservation(
    val studentId: String,
    val reservations: List<Reservation>,
    val equipmentTypes: Map<Long, EquipmentType>
)
