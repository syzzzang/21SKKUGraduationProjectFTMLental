package com.soyoung.grad.controller

import com.soyoung.grad.controller.model.PostStudentBody
import com.soyoung.grad.controller.model.ResetStudentBody
import com.soyoung.grad.service.StudentService
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/students")
class StudentController(private val studentService: StudentService) {
    @ApiOperation("학생 추가")
    @PostMapping("")
    fun postStudent(postStudentBody: PostStudentBody) {
        studentService.createStudent(
            postStudentBody.id,
            postStudentBody.pw,
            postStudentBody.studentId,
            postStudentBody.name,
            postStudentBody.nickname
        )
    }

    @ApiOperation("학생 초기화")
    @PostMapping("/reset")
    fun resetStudent(resetStudentBody: ResetStudentBody) {
        studentService.resetStudent(resetStudentBody.studentNumber)
    }
}