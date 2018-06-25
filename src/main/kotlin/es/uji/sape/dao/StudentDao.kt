package es.uji.sape.dao

import es.uji.sape.model.Student
import es.uji.sape.tables.Students
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class StudentDao {

    fun findAll(): List<Student> = Students.selectAll().map { it.toStudent() }

    fun find(code: String): List<Student> = Students.select { Students.code eq code }.map { it.toStudent() }

    fun add(student: Student) {
        Students.insert {
            it[Students.code] = student.code
            it[Students.name] = student.name
            it[Students.surname] = student.surname
            it[Students.itinerary] = student.itinerary
            it[Students.passedCredits] = student.passedCredits
            it[Students.averageScore] = student.averageScore
            it[Students.pendingSubjects] = student.pendingSubjects
            it[Students.internshipStartMonth] = student.internshipStartMonth
        }
    }

    fun update(student: Student) {
        Students.update({ Students.code eq student.code }) {
            it[Students.name] = student.name
            it[Students.surname] = student.surname
            it[Students.itinerary] = student.itinerary
            it[Students.passedCredits] = student.passedCredits
            it[Students.averageScore] = student.averageScore
            it[Students.pendingSubjects] = student.pendingSubjects
            it[Students.internshipStartMonth] = student.internshipStartMonth
        }
    }

    fun delete(code: String) {
        Students.deleteWhere { Students.code eq code }
    }

    private fun ResultRow.toStudent() = Student(
        code = this[Students.code],
        name = this[Students.name],
        surname = this[Students.surname],
        itinerary = this[Students.itinerary],
        passedCredits = this[Students.passedCredits],
        averageScore = this[Students.averageScore],
        internshipStartMonth = this[Students.internshipStartMonth]
    )
}
