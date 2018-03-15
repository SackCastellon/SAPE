package es.uji.sape.dao;

import es.uji.sape.model.Assignment;
import es.uji.sape.model.AssignmentState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@SuppressWarnings("DesignForExtension")
public class AssignmentDao {

    private JdbcTemplate template;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") @NotNull DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    public @NotNull List<Assignment> findAll() {
        return template.query("SELECT * FROM assignment;", new AssignmentMapper());
    }

    public @NotNull Optional<Assignment> find(int projectOfferId, @NotNull String studentDni, @NotNull String tutorDni) {
        @Nullable Assignment value;
        try {
            value = template.queryForObject("SELECT * FROM assignment WHERE project_offer_id = ? AND student_dni = ? AND tutor_dni = ?",
                    new AssignmentMapper(), projectOfferId, studentDni, tutorDni);
        } catch (DataAccessException ignored) {
            value = null;
        }
        return Optional.ofNullable(value);
    }

    public void add(@NotNull Assignment assignment) {
        final LocalDate acceptanceDate = assignment.getAcceptanceDate();
        final LocalDate rejectionDate = assignment.getRejectionDate();
        template.update(
                "INSERT INTO assignment(project_offer_id, student_dni, tutor_dni, proposal_date, acceptance_date, rejection_date, iglu_transfer_date, state) VALUES(?,?,?,?,?,?,?,?)",
                assignment.getProjectOfferId(),
                assignment.getStudentDni(),
                assignment.getTutorDni(),
                assignment.getProposalDate(),
                (acceptanceDate == null) ? null : Date.valueOf(acceptanceDate),
                (rejectionDate == null) ? null : Date.valueOf(rejectionDate),
                assignment.getIgluTransferDate(),
                assignment.getState().ordinal()
        );
    }

    public void update(@NotNull Assignment assignment) {
        final LocalDate acceptanceDate = assignment.getAcceptanceDate();
        final LocalDate rejectionDate = assignment.getRejectionDate();
        template.update(
                "UPDATE assignment SET proposal_date = ?, acceptance_date = ?, rejection_date = ?, iglu_transfer_date = ?, state = ? WHERE project_offer_id = ? AND student_dni = ? AND tutor_dni = ?",
                assignment.getProposalDate(),
                (acceptanceDate == null) ? null : Date.valueOf(acceptanceDate),
                (rejectionDate == null) ? null : Date.valueOf(rejectionDate),
                assignment.getIgluTransferDate(),
                assignment.getState().ordinal(),
                assignment.getProjectOfferId(),
                assignment.getStudentDni(),
                assignment.getTutorDni()
        );
    }

    public void delete(int projectOfferId, @NotNull String studentDni, @NotNull String tutorDni) {
        template.update("DELETE FROM assignment WHERE project_offer_id = ? AND student_dni = ? AND tutor_dni = ?", projectOfferId, studentDni, tutorDni);
    }

    private static final class AssignmentMapper implements RowMapper<Assignment> {

        public @NotNull Assignment mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            final Date acceptanceDate = rs.getDate("acceptance_date");
            final Date rejectionDate = rs.getDate("rejection_date");
            return new Assignment(
                    rs.getInt("project_offer_id"),
                    rs.getString("student_dni"),
                    rs.getString("tutor_dni"),
                    rs.getDate("proposal_date").toLocalDate(),
                    (acceptanceDate == null) ? null : acceptanceDate.toLocalDate(),
                    (rejectionDate == null) ? null : rejectionDate.toLocalDate(),
                    rs.getDate("iglu_transfer_date").toLocalDate(),
                    AssignmentState.values()[rs.getInt("state")]
            );
        }
    }
}
