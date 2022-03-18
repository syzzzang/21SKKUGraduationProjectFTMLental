package com.soyoung.grad.domain.equipment

interface EquipmentCustomRepository {
    fun countEquipmentsByEquipmentType(): Map<Long, Long>
}