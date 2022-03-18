package com.soyoung.grad.domain.equipment

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface EquipmentRepository : JpaRepository<Equipment, Long>, EquipmentCustomRepository {
    fun findByName(name: String): Optional<Equipment>
    fun findByTypeId(typeId: Long): List<Equipment>
}