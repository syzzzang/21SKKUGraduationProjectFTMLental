package com.soyoung.grad.controller

import com.soyoung.grad.controller.model.PostEquipmentBody
import com.soyoung.grad.controller.model.PostEquipmentTypeBody
import com.soyoung.grad.domain.equipment.EquipmentType
import com.soyoung.grad.service.EquipmentService
import com.soyoung.grad.service.model.EquipmentWithTypeName
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/equipments")
class EquipmentController(private val equipmentService: EquipmentService) {

    @ApiOperation("장비 조회")
    @GetMapping("")
    fun getEquipments(): List<EquipmentWithTypeName> {
        return equipmentService.queryEquipments()
    }

    @ApiOperation("장비 추가")
    @PostMapping("")
    fun postEquipment(postEquipmentBody: PostEquipmentBody) {
        equipmentService.createEquipment(postEquipmentBody.name, postEquipmentBody.typeName)
    }

    @ApiOperation("장비 제거")
    @DeleteMapping("")
    fun deleteEquipment(@ApiParam("장비 아이디") @RequestParam("equipment_id") equipmentId: Long) {
        equipmentService.deleteEquipment(equipmentId)
    }

    @ApiOperation("장비 타입 조회")
    @GetMapping("/types")
    fun getEquipmentTypes(): List<EquipmentType> {
        return equipmentService.queryEquipmentTypes()
    }

    @ApiOperation("장비 타입 추가")
    @PostMapping("/types")
    fun postEquipmentType(postEquipmentTypeBody: PostEquipmentTypeBody) {
        equipmentService.createEquipmentType(postEquipmentTypeBody.name)
    }

}