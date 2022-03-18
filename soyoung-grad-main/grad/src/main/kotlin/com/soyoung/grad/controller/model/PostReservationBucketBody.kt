package com.soyoung.grad.controller.model

data class PostReservationBucketBody(
    val studentId: String,
    val equipment: EquipmentBody,
    val startTime: Long,
    val endTime: Long
)