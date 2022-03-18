package com.soyoung.grad.domain.privilege

import org.springframework.data.redis.core.RedisHash
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@RedisHash(timeToLive = 600)
data class Privilege(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: String?,
    val equipmentId: Long,
    val equipmentTypeId: Long,
    val studentId: String,
    val startTime: Long,
    val endTime: Long
)