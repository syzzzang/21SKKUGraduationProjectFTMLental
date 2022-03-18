package com.soyoung.grad.service.model

data class JoinedReservationStatus(
    val startTime: Long,
    val endTime: Long,
    val reservationStatuses: List<ReservationStatus>
)