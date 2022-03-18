package com.soyoung.grad.domain.equipment

import com.querydsl.core.Tuple
import com.querydsl.jpa.impl.JPAQueryFactory
import com.soyoung.grad.domain.equipment.QEquipment.equipment

class EquipmentCustomRepositoryImpl(private val jpaQueryFactory: JPAQueryFactory) : EquipmentCustomRepository {
    override fun countEquipmentsByEquipmentType(): Map<Long, Long> {
        val query = jpaQueryFactory.from(equipment)
        query.select(equipment.typeId, equipment.typeId.count())
        query.groupBy(equipment.typeId)
        val queryResult = query.fetch()

        return resultToMap(queryResult)
    }

    private fun resultToMap(queryResult: List<Any>): Map<Long, Long> {
        val countByEquipmentTypeMap = mutableMapOf<Long, Long>()
        queryResult.map {
            val list = (it as Tuple)
            val typeId = list.get(0, Long::class.java)!!
            val count = list.get(1, Long::class.java)!!
            when (countByEquipmentTypeMap[typeId]) {
                null -> countByEquipmentTypeMap[typeId] = count
                else -> countByEquipmentTypeMap[typeId] = countByEquipmentTypeMap[typeId]!!.plus(count)
            }
        }
        return countByEquipmentTypeMap
    }

}