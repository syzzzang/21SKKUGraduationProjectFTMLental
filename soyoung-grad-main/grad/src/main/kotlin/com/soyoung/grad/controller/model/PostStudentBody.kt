package com.soyoung.grad.controller.model

data class PostStudentBody(
    val id: String,
    val pw: String,
    val studentId: Long,
    val name: String,
    val nickname: String
)