package com.soyoung.grad.service

import com.soyoung.grad.domain.equipment.Equipment
import com.soyoung.grad.domain.equipment.EquipmentRepository
import com.soyoung.grad.domain.equipment.EquipmentType
import com.soyoung.grad.domain.equipment.EquipmentTypeRepository
import com.soyoung.grad.service.mapper.EquipmentMapper
import com.soyoung.grad.service.model.EquipmentWithTypeName
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class EquipmentService(
    private val equipmentRepository: EquipmentRepository,
    private val equipmentTypeRepository: EquipmentTypeRepository,
    private val equipmentMapper: EquipmentMapper
) {
    fun queryEquipments(): List<EquipmentWithTypeName> {
        val equipments = equipmentRepository.findAll()
        val equipmentTypes = equipmentTypeRepository.findAll().associateBy { it.id }

        return equipments.map { equipmentMapper.toEquipmentWithTypeName(it, equipmentTypes[it.typeId]!!) }
    }

    fun queryEquipmentTypes(): List<EquipmentType> {
        return equipmentTypeRepository.findAll()
    }

    private fun queryEquipment(equipmentId: Long): Equipment? {
        return equipmentRepository.findById(equipmentId).orElse(null)
    }

    private fun queryEquipment(equipmentName: String): Equipment? {
        return equipmentRepository.findByName(equipmentName).orElse(null)
    }

    private fun queryEquipmentType(equipmentTypeName: String): EquipmentType? {
        return equipmentTypeRepository.findByName(equipmentTypeName).orElse(null)
    }

    fun validateEquipmentType(equipmentTypeId: Long) {
        queryEquipmentTypeById(equipmentTypeId)
            ?: throw IllegalStateException("equipment type($equipmentTypeId) not found")
    }

    fun validateEquipmentType(equipmentTypeName: String) {
        queryEquipmentType(equipmentTypeName)
            ?: throw IllegalStateException("equipment type($equipmentTypeName) not found")
    }

    fun validateEquipmentNull(equipmentName: String) {
        if (queryEquipment(equipmentName) != null) {
            throw IllegalStateException("equipment($equipmentName) exists")
        }
    }

    fun validateEquipmentTypeNull(equipmentTypeName: String) {
        if (queryEquipmentType(equipmentTypeName) != null) {
            throw IllegalStateException("equipmentType($equipmentTypeName) exists")
        }
    }

    fun createEquipment(name: String, typeName: String) {
        validateEquipmentNull(name)
        validateEquipmentType(typeName)

        val equipmentType = queryEquipmentType(typeName)!!
        equipmentRepository.save(Equipment(null, name, equipmentType.id!!))
    }

    fun createEquipmentType(typeName: String) {
        validateEquipmentTypeNull(typeName)
        equipmentTypeRepository.save(EquipmentType(null, typeName))
    }

    fun equipmentTypeCountMap(): Map<Long, Long> {
        return equipmentRepository.countEquipmentsByEquipmentType()
    }

    fun queryEquipmentsByType(equipmentTypeId: Long): List<Equipment> {
        return equipmentRepository.findByTypeId(equipmentTypeId)
    }

    fun queryEquipmentTypeById(equipmentTypeId: Long): EquipmentType? {
        return equipmentTypeRepository.findById(equipmentTypeId).orElse(null)
    }

    fun validateEquipmentAndType(id: Long, typeId: Long) {
        val equipment = queryEquipment(id) ?: throw IllegalStateException("\"equipment($id) not found\"")
        if (equipment.typeId != typeId) {
            throw IllegalStateException("equipment type(${equipment.name}) mismatch")
        }
        validateEquipmentType(typeId)
    }

    @Transactional
    fun deleteEquipment(equipmentId: Long) {
        equipmentRepository.deleteById(equipmentId)
    }
}