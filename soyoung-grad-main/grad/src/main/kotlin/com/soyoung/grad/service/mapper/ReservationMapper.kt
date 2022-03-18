package com.soyoung.grad.service.mapper

import com.soyoung.grad.domain.equipment.EquipmentType
import com.soyoung.grad.domain.privilege.Privilege
import com.soyoung.grad.domain.reservation.Reservation
import com.soyoung.grad.service.model.*
import org.springframework.stereotype.Component

@Component
class ReservationMapper {

    fun toReservationStatus(key: EquipmentType, value: AvailableCount): ReservationStatus {
        return ReservationStatus(key, value)
    }

    fun toJoinedReservationStatus(
        startTime: Long,
        endTime: Long,
        reservationStatuses: List<ReservationStatus>
    ): JoinedReservationStatus {
        return JoinedReservationStatus(startTime, endTime, reservationStatuses)
    }

    fun toJoinedSoftReservations(
        studentId: String,
        softReservations: List<Privilege>
    ): JoinedSoftReservations {
        return JoinedSoftReservations(studentId, softReservations)
    }

    fun toMyReservation(
        studentId: String,
        reservations: List<Reservation>,
        equipmentTypes: Map<Long, EquipmentType>
    ) : MyReservation {
        return MyReservation(studentId, reservations, equipmentTypes)
    }
}