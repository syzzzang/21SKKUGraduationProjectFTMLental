package com.soyoung.grad.service.model

import com.soyoung.grad.domain.privilege.Privilege

data class JoinedSoftReservations(
    val studentId: String,
    val softReservations: List<Privilege>
)