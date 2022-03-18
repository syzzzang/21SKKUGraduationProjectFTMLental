package com.soyoung.grad.controller

import com.soyoung.grad.controller.model.PostReservationBody
import com.soyoung.grad.controller.model.PostReservationBucketBody
import com.soyoung.grad.service.ReservationService
import com.soyoung.grad.service.model.*
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/reservations")
class ReservationController(private val reservationService: ReservationService) {

    @ApiOperation("장비 대여 현황 조회")
    @GetMapping("")
    fun getReservations(
        @ApiParam("시작 시간(yyyymmddhh)") @RequestParam("start_time") startTime: Long,
        @ApiParam("종료 시간(yyyymmddhh)") @RequestParam("end_time") endTime: Long
    ): JoinedReservationStatus {
        if (!isTimeValid(startTime, endTime)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "시작시간이 종료시간 이후에요")
        }
        return reservationService.queryReservations(startTime, endTime)
    }

    @ApiOperation("내가 대여한 장비 조회")
    @GetMapping("/me")
    fun getMyReservations(
        @ApiParam("학번") @RequestParam("student_number") studentNumber: Long
    ): MyReservation {
        return reservationService.queryMyReservations(studentNumber)
    }

    @ApiOperation("장비 타입으로 대여 현황 조회")
    @GetMapping("/types/{typeId}")
    fun getReservationsByEquipmentTypeId(
        @ApiParam("장비 타입 ID") @PathVariable typeId: Long,
        @ApiParam("시작 시간(yyyymmddhh)") @RequestParam("start_time") startTime: Long,
        @ApiParam("종료 시간(yyyymmddhh)") @RequestParam("end_time") endTime: Long
    ): JoinedEquipmentReservationStatus {
        if (!isTimeValid(startTime, endTime)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "시작시간이 종료시간 이후에요")
        }
        return reservationService.queryReservationsByEquipmentTypeIdAndTime(typeId, startTime, endTime)
    }

    @ApiOperation("내가 찜한 장비 목록 조회")
    @GetMapping("/bucket/me")
    fun getSoftReservations(
        @ApiParam("학번") @RequestParam("student_number") studentNumber: Long,
    ): JoinedSoftReservations {
        return reservationService.querySoftReservations(studentNumber)
    }

    @ApiOperation("장비 찜하기(10분간 유효해요)")
    @PostMapping("/bucket")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun postSoftReservation(
        @RequestBody body: PostReservationBucketBody
    ) {
        if (!isTimeValid(body.startTime, body.endTime)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "시작시간이 종료시간 이후에요")
        }
        try {
            val equipmentInput = EquipmentInput(body.equipment.id, body.equipment.typeId)

            reservationService.commandSoftReservation(
                body.studentId,
                equipmentInput,
                body.startTime,
                body.endTime
            )
        } catch (exception: IllegalStateException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청이에요")
        }
    }

    @ApiOperation("장비 대여 요청")
    @PostMapping("")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun postReservation(
        @RequestBody body: PostReservationBody
    ) {
        if (!isTimeValid(body.startTime, body.endTime)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "시작시간이 종료시간 이후에요")
        }
        try {
            val equipmentInputs = body.equipments.map { EquipmentInput(it.id, it.typeId) }

            reservationService.commandReservation(
                body.studentId,
                equipmentInputs,
                body.startTime,
                body.endTime
            )
        } catch (exception: IllegalStateException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청이에요")
        }
    }

    private fun isTimeValid(startTime: Long, endTime: Long): Boolean {
        return startTime < endTime
    }
}
