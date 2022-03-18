package com.soyoung.grad.domain.reservation

interface ReservationCustomRepository {
    fun findByTime(startTime: Long, endTime: Long): List<Reservation>
    fun findByEquipmentTypeIdAndTime(equipmentTypeId: Long, startTime: Long, endTime: Long): List<Reservation>
    fun findByStudentId(studentId: String): List<Reservation>
    fun clearStudentReservation(studentId: String)
}