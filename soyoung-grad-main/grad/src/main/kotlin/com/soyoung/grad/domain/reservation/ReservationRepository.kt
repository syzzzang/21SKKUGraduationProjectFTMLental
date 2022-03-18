package com.soyoung.grad.domain.reservation

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReservationRepository : JpaRepository<Reservation, Long>, ReservationCustomRepository {
}