package com.soyoung.grad.service.model

import com.soyoung.grad.domain.equipment.EquipmentType

data class JoinedEquipmentReservationStatus(
    val equipmentType: EquipmentType,
    val startTime: Long,
    val endTime: Long,
    val equipmentReservationStatuses: List<EquipmentReservationStatus>
)