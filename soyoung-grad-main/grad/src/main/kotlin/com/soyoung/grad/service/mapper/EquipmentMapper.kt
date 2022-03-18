package com.soyoung.grad.service.mapper

import com.soyoung.grad.domain.equipment.Equipment
import com.soyoung.grad.domain.equipment.EquipmentType
import com.soyoung.grad.service.model.EquipmentWithTypeName
import org.springframework.stereotype.Component

@Component
class EquipmentMapper {
    fun toEquipmentWithTypeName(equipment: Equipment, equipmentType: EquipmentType): EquipmentWithTypeName {
        return EquipmentWithTypeName(equipment.id!!, equipment.name, equipmentType.name)
    }
}