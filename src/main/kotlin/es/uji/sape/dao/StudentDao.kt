package es.uji.sape.dao

import es.uji.sape.model.Itinerary
import es.uji.sape.model.Month
import es.uji.sape.model.Student
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet
import javax.sql.DataSource

@Repository
@Transactional
class StudentDao {

    lateinit var template: JdbcTemplate

    @Autowired
    fun setDataSource(@Qualifier("dataSource") dataSource: DataSource) {
        template = JdbcTemplate(dataSource)
    }

    fun findAll(): List<Student> = template.query("SELECT * FROM student;", StudentMapper())

    fun find(code: String) = template.queryForObject("SELECT * FROM student WHERE code = ?;", StudentMapper(), code)

    fun add(student: Student) {
        template.update(
            "INSERT INTO student(code, name, surname, itinerary, passed_credits, average_grade, pending_subjects, internship_start_semester) VALUES(?,?,?,?,?,?,?,?)",
            student.code,
            student.name,
            student.surname,
            student.itinerary.ordinal,
            student.passedCredits,
            student.averageScore,
            student.pendingSubjects,
            student.internshipStartSemester.ordinal
        )
    }

    fun update(student: Student) {
        template.update(
            "UPDATE student SET name = ?, surname = ?, itinerary = ?, passed_credits = ?, average_grade = ?, pending_subjects = ?, internship_start_semester = ? WHERE code = ?",
            student.name,
            student.surname,
            student.itinerary.ordinal,
            student.passedCredits,
            student.averageScore,
            student.pendingSubjects,
            student.internshipStartSemester.ordinal,
            student.code
        )
    }

    fun delete(code: String) {
        template.update("DELETE FROM student WHERE code = ?", code)
    }

    private class StudentMapper : RowMapper<Student> {
        override fun mapRow(rs: ResultSet, rowNum: Int): Student {
            return Student(
                code = rs.getString("code"),
                name = rs.getString("name"),
                surname = rs.getString("surname"),
                itinerary = Itinerary.values()[rs.getInt("itinerary")],
                passedCredits = rs.getInt("passed_credits"),
                averageScore = rs.getFloat("average_grade"),
                pendingSubjects = rs.getInt("pending_subjects"),
                internshipStartSemester = Month.values()[rs.getInt("internship_start_semester")]
            )
        }
    }
}
