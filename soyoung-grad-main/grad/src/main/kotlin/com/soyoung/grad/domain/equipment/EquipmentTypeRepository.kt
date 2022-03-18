package com.soyoung.grad.domain.equipment

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface EquipmentTypeRepository : JpaRepository<EquipmentType, Long> {
    fun findByName(name: String): Optional<EquipmentType>
}