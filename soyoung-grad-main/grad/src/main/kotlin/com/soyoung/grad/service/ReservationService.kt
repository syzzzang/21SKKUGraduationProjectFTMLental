package com.soyoung.grad.service

import com.soyoung.grad.domain.equipment.Equipment
import com.soyoung.grad.domain.equipment.EquipmentType
import com.soyoung.grad.domain.privilege.Privilege
import com.soyoung.grad.domain.privilege.PrivilegeRepository
import com.soyoung.grad.domain.reservation.Reservation
import com.soyoung.grad.domain.reservation.ReservationRepository
import com.soyoung.grad.service.mapper.ReservationMapper
import com.soyoung.grad.service.model.*
import com.soyoung.grad.util.logger
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class ReservationService(
    private val reservationRepository: ReservationRepository,
    private val privilegeRepository: PrivilegeRepository,

    private val studentService: StudentService,
    private val equipmentService: EquipmentService,

    private val reservationMapper: ReservationMapper
) {

    companion object {
        val logger = logger()
    }

    fun queryReservations(startTime: Long, endTime: Long): JoinedReservationStatus {
        val reservations = reservationRepository.findByTime(startTime, endTime)
        val softReservations = privilegeRepository.findAll()
            .filterNotNull()
            .filter { !(it.endTime <= startTime || it.startTime >= endTime) }

        val equipmentTypes = equipmentService.queryEquipmentTypes().associateBy { it.id!! }

        val reservationMapByEquipmentType = reservationMapByEquipmentType(reservations, softReservations)
        val equipmentTypeCountMap = equipmentService.equipmentTypeCountMap()

        val availableEquipmentCountMap =
            availableEquipmentCountMap(reservationMapByEquipmentType, equipmentTypeCountMap, equipmentTypes)
        val reservationStatuses =
            availableEquipmentCountMap.map { reservationMapper.toReservationStatus(it.key, it.value) }

        return reservationMapper.toJoinedReservationStatus(startTime, endTime, reservationStatuses)
    }

    fun queryMyReservations(studentUid: Long): MyReservation {
        val student = studentService.queryStudentByNumber(studentUid)!!
        val reservations = reservationRepository.findByStudentId(student.id)
        val equipmentTypes = equipmentService.queryEquipmentTypes().associateBy { it.id!! }
        return reservationMapper.toMyReservation(student.id, reservations, equipmentTypes)
    }

    fun querySoftReservations(studentUid: Long): JoinedSoftReservations {
        val student = studentService.queryStudentByNumber(studentUid)!!
        val softReservations = privilegeRepository.findAll()
            .filterNotNull()
            .filter { it.studentId == student.id }
        return reservationMapper.toJoinedSoftReservations(student.id, softReservations)
    }

    @Transactional
    fun commandReservation(studentId: String, equipmentInputs: List<EquipmentInput>, startTime: Long, endTime: Long) {
        studentService.validateStudent(studentId)
        validateTime(startTime, endTime)

        equipmentInputs.map {
            equipmentService.validateEquipmentAndType(it.id, it.typeId)
            bookedReservationCheck(it.id, startTime, endTime)
            reservationRepository.save(Reservation(null, it.id, it.typeId, studentId, startTime, endTime))
        }
        val softReservations = privilegeRepository.findAll()
            .filterNotNull()
            .filter { it.studentId == studentId }
        privilegeRepository.deleteAll(softReservations)
    }

    private fun bookedReservationCheck(equipmentId: Long, startTime: Long, endTime: Long) {
        val currentReservations = reservationRepository.findByTime(startTime, endTime)
        val bookedReservation = currentReservations.filter { it.equipmentId == equipmentId }

        val bookedSoftReservations = privilegeRepository.findByEquipmentId(equipmentId)
            .filter { !(it.endTime <= startTime || it.startTime >= endTime) }

        val bookedFlag = bookedReservation.isNotEmpty() || bookedSoftReservations.isNotEmpty()
        if (bookedFlag) {
            throw IllegalStateException("equipment $equipmentId is already booked in ($startTime ~ $endTime)")
        }
    }

    private fun reservationMapByEquipmentType(
        reservations: List<Reservation>,
        softReservations: List<Privilege>
    ): Map<Long, Long> {
        val reservationMap = mutableMapOf<Long, Long>()
        reservations.map {
            when (reservationMap[it.equipmentTypeId]) {
                null -> reservationMap[it.equipmentTypeId] = 1
                else -> reservationMap[it.equipmentTypeId] = reservationMap[it.equipmentTypeId]!!.plus(1)
            }
        }
        softReservations.map {
            when (reservationMap[it.equipmentTypeId]) {
                null -> reservationMap[it.equipmentTypeId] = 1
                else -> reservationMap[it.equipmentTypeId] = reservationMap[it.equipmentTypeId]!!.plus((1))
            }
        }
        return reservationMap
    }

    private fun availableEquipmentCountMap(
        reservationMapByEquipmentType: Map<Long, Long>,
        equipmentTypeCountMap: Map<Long, Long>,
        equipmentTypeNameMap: Map<Long, EquipmentType>
    ): Map<EquipmentType, AvailableCount> {
        val availableEquipmentCountMap = mutableMapOf<EquipmentType, AvailableCount>()

        equipmentTypeNameMap.map {
            val equipmentType = equipmentTypeNameMap[it.key]!!
            val equipmentTypeTotalCount = equipmentTypeCountMap[it.key] ?: 0
            val equipmentTypeReservedCount = reservationMapByEquipmentType[it.key] ?: 0
            val availableEquipmentTypeCount = equipmentTypeTotalCount - equipmentTypeReservedCount

            val availableCount = AvailableCount(availableEquipmentTypeCount, equipmentTypeTotalCount)
            availableEquipmentCountMap[equipmentType] = availableCount
        }

        return availableEquipmentCountMap
    }

    fun queryReservationsByEquipmentTypeIdAndTime(
        equipmentTypeId: Long,
        startTime: Long,
        endTime: Long
    ): JoinedEquipmentReservationStatus {
        val reservationMap = reservationRepository.findByEquipmentTypeIdAndTime(equipmentTypeId, startTime, endTime)
            .associateBy { it.equipmentId }
        val softReservationsMap = privilegeRepository.findAll()
            .filterNotNull()
            .filter { !(it.endTime <= startTime || it.startTime >= endTime) }
            .filter { it.equipmentTypeId == equipmentTypeId }
            .associateBy { it.equipmentId }

        val equipments = equipmentService.queryEquipmentsByType(equipmentTypeId)
        val equipmentType = equipmentService.queryEquipmentTypeById(equipmentTypeId)!!

        val equipmentReservationStatuses = equipmentReservationStatuses(equipments, reservationMap, softReservationsMap)

        return JoinedEquipmentReservationStatus(equipmentType, startTime, endTime, equipmentReservationStatuses)
    }

    private fun equipmentReservationStatuses(
        equipments: List<Equipment>,
        reservationMap: Map<Long, Reservation>,
        softReservations: Map<Long, Privilege>
    ) = equipments.map {
        val isHardReserved = isHardReserved(reservationMap, it)
        val isSoftReserved = isSoftReserved(softReservations, it)

        if (!isHardReserved && !isSoftReserved) {
            EquipmentReservationStatus(it, false)
        } else {
            EquipmentReservationStatus(it, true)
        }
    }

    private fun isSoftReserved(
        softReservations: Map<Long, Privilege>,
        it: Equipment
    ) = when (softReservations[it.id]) {
        null -> false
        else -> true
    }

    private fun isHardReserved(
        reservationMap: Map<Long, Reservation>,
        it: Equipment
    ) = when (reservationMap[it.id]) {
        null -> false
        else -> true
    }

    @Transactional
    fun commandSoftReservation(studentId: String, equipmentInput: EquipmentInput, startTime: Long, endTime: Long) {
        studentService.validateStudent(studentId)
        equipmentService.validateEquipmentAndType(equipmentInput.id, equipmentInput.typeId)
        validateTime(startTime, endTime)

        bookedReservationCheck(equipmentInput.id, startTime, endTime)

        privilegeRepository.save(
            Privilege(
                null,
                equipmentInput.id,
                equipmentInput.typeId,
                studentId,
                startTime,
                endTime
            )
        )
    }

    private fun validateTime(startTime: Long, endTime: Long) {
        if (startTime >= endTime) {
            throw IllegalArgumentException("시작시간은 종료시간 이전으로 해주세요")
        }
    }
}
