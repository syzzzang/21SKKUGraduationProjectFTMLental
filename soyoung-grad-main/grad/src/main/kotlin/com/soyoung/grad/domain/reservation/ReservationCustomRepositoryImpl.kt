package com.soyoung.grad.domain.reservation

import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import com.soyoung.grad.domain.reservation.QReservation.reservation

class ReservationCustomRepositoryImpl(private val jpaQueryFactory: JPAQueryFactory) : ReservationCustomRepository {
    override fun findByTime(startTime: Long, endTime: Long): List<Reservation> {
        val query = jpaQueryFactory.selectFrom(reservation)
        whereTimeBetween(query, startTime, endTime)
        return query.fetch()
    }

    override fun findByEquipmentTypeIdAndTime(
        equipmentTypeId: Long,
        startTime: Long,
        endTime: Long
    ): List<Reservation> {
        val query = jpaQueryFactory.selectFrom(reservation)
        whereTimeBetween(query, startTime, endTime)
        query.where(reservation.equipmentTypeId.eq(equipmentTypeId))
        return query.fetch()
    }

    override fun findByStudentId(studentId: String): List<Reservation> {
        val query = jpaQueryFactory.selectFrom(reservation)
        query.where(
            reservation.studentId.eq(studentId)
        )
        return query.fetch()
    }

    override fun clearStudentReservation(studentId: String) {
        val query = jpaQueryFactory.delete(reservation)
        query.where(
            reservation.studentId.eq(studentId)
        )
        query.execute()
    }

    private fun whereTimeBetween(query: JPAQuery<Reservation>, startTime: Long, endTime: Long) {
        query.where(
            !(reservation.endTime.loe(startTime).or(reservation.startTime.goe(endTime)))
        )
    }
}