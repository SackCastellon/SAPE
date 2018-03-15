package es.uji.sape.dao;

import es.uji.sape.model.Itinerary;
import es.uji.sape.model.Student;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@SuppressWarnings("DesignForExtension")
public class StudentDao {

    private JdbcTemplate template;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") @NotNull DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    public @NotNull List<Student> findAll() {
        return template.query("SELECT * FROM student;", new StudentMapper());
    }

    public @NotNull Optional<Student> find(@NotNull String dni) {
        @Nullable Student value;
        try {
            value = template.queryForObject("SELECT * FROM student WHERE dni = ?;", new StudentMapper(), dni);
        } catch (DataAccessException ignored) {
            value = null;
        }
        return Optional.ofNullable(value);
    }

    public void add(@NotNull Student student) {
        template.update(
                "INSERT INTO student(dni, name, surname, code, itinerary, passed_credits, average_grade, pending_subjects, internship_start_semester) " +
                        "VALUES(?,?,?,?,?,?,?,?,?)",
                student.getDni(),
                student.getName(),
                student.getSurname(),
                student.getCode(),
                student.getItinerary().ordinal(),
                student.getPassedCredits(),
                student.getAverageScore(),
                student.getPendingSubjects(),
                student.getInternshipStartSemester()
        );
    }

    public void update(@NotNull Student student) {
        template.update(
                "UPDATE student SET name = ?, surname = ?, code = ?, itinerary = ?, passed_credits = ?, average_grade = ?, pending_subjects = ?, " +
                        "internship_start_semester = ? WHERE dni = ?",
                student.getName(),
                student.getSurname(),
                student.getCode(),
                student.getItinerary().ordinal(),
                student.getPassedCredits(),
                student.getAverageScore(),
                student.getPendingSubjects(),
                student.getInternshipStartSemester(),
                student.getDni()
        );
    }

    public void delete(@NotNull String dni) {
        template.update("DELETE FROM student WHERE dni = ?", dni);
    }

    private static final class StudentMapper implements RowMapper<Student> {

        public @NotNull Student mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            return new Student(
                    rs.getString("dni"),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getInt("code"),
                    Itinerary.values()[rs.getInt("itinerary")],
                    rs.getInt("passed_credits"),
                    rs.getFloat("average_grade"),
                    rs.getInt("pending_subjects"),
                    rs.getInt("internship_start_semester")
            );
        }
    }
}
