package com.soyoung.grad.service

import com.soyoung.grad.domain.reservation.ReservationRepository
import com.soyoung.grad.domain.student.Student
import com.soyoung.grad.domain.student.StudentRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class StudentService(
    private val studentRepository: StudentRepository,
    private val reservationRepository: ReservationRepository
) {
    fun queryStudent(id: String): Student? {
        return studentRepository.findById(id).orElse(null)
    }

    fun validateStudent(id: String) {
        queryStudent(id) ?: throw IllegalStateException("student($id) not found")
    }

    fun validateStudentNull(id: String) {
        if (queryStudent(id) != null) {
            throw IllegalStateException("student($id) exists")
        }
    }

    @Transactional
    fun createStudent(id: String, pw: String, studentNumber: Long, name: String, nickname: String) {
        validateStudentNull(id)
        studentRepository.save(Student(id, pw, studentNumber, name, nickname))
    }

    fun queryStudentByNumber(studentNumber: Long): Student? {
        return studentRepository.findByStudentNumber(studentNumber)
    }

    @Transactional
    fun resetStudent(studentNumber: Long) {
        val student = queryStudentByNumber(studentNumber)?:return
        reservationRepository.clearStudentReservation(student.id)
    }
}