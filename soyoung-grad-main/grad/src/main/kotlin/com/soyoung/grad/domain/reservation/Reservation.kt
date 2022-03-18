package com.soyoung.grad.domain.reservation

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Reservation(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val equipmentId: Long,
    val equipmentTypeId: Long,
    val studentId: String,
    val startTime: Long,
    val endTime: Long
)