package com.soyoung.grad.domain.student

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentRepository : JpaRepository<Student, String> {
    fun findByStudentNumber(studentNumber: Long): Student?
}