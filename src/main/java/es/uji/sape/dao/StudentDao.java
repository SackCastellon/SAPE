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
    public final void setDataSource(@Qualifier("dataSource") @NotNull DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    public @NotNull List<Student> findAll() {
        return template.query("SELECT * FROM student;", new StudentMapper());
    }

    public @NotNull Optional<Student> find(@NotNull String code) {
        @Nullable Student value;
        try {
            value = template.queryForObject("SELECT * FROM student WHERE code = ?;", new StudentMapper(), code);
        } catch (DataAccessException ignored) {
            value = null;
        }
        return Optional.ofNullable(value);
    }

    public void add(@NotNull Student student) {
        template.update(
                "INSERT INTO student(code, name, surname, itinerary, passed_credits, average_grade, pending_subjects, internship_start_semester) " +
                        "VALUES(?,?,?,?,?,?,?,?)",
                student.getCode(),
                student.getName(),
                student.getSurname(),
                student.getItinerary().ordinal(),
                student.getPassedCredits(),
                student.getAverageScore(),
                student.getPendingSubjects(),
                student.getInternshipStartSemester()
        );
    }

    public void update(@NotNull Student student) {
        template.update(
                "UPDATE student SET name = ?, surname = ?, itinerary = ?, passed_credits = ?, average_grade = ?, pending_subjects = ?, " +
                        "internship_start_semester = ? WHERE code = ?",
                student.getName(),
                student.getSurname(),
                student.getItinerary().ordinal(),
                student.getPassedCredits(),
                student.getAverageScore(),
                student.getPendingSubjects(),
                student.getInternshipStartSemester(),
                student.getCode()
        );
    }

    public void delete(@NotNull String code) {
        template.update("DELETE FROM student WHERE code = ?", code);
    }

    private static final class StudentMapper implements RowMapper<Student> {

        public @NotNull Student mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            final @NotNull Student student = new Student();
            student.setCode(rs.getString("code"));
            student.setName(rs.getString("name"));
            student.setSurname(rs.getString("surname"));
            student.setItinerary(Itinerary.values()[rs.getInt("itinerary")]);
            student.setPassedCredits(rs.getInt("passed_credits"));
            student.setAverageScore(rs.getFloat("average_grade"));
            student.setPendingSubjects(rs.getInt("pending_subjects"));
            student.setInternshipStartSemester(rs.getInt("internship_start_semester"));
            return student;
        }
    }
}
