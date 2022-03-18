package com.soyoung.grad.domain.student

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Student(
    @Id
    val id: String,
    val pw: String,
    val studentNumber: Long,
    val name: String,
    val nickname: String
)