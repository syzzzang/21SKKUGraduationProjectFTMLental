package com.soyoung.grad.domain.privilege

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PrivilegeRepository : CrudRepository<Privilege, String> {
    fun findByEquipmentId(equipmentId: Long): List<Privilege>
}