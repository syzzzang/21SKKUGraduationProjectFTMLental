package com.soyoung.grad.controller.model

data class PostReservationBody(
    val studentId: String,
    val equipments: List<EquipmentBody>,
    val startTime: Long,
    val endTime: Long
)