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
    public final void setDataSource(@Qualifier("dataSource") @NotNull DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    public @NotNull List<Assignment> findAll() {
        return template.query("SELECT * FROM assignment;", new AssignmentMapper());
    }

    public @NotNull Optional<Assignment> find(int projectOfferId, @NotNull String studentCode) {
        @Nullable Assignment value;
        try {
            value = template.queryForObject("SELECT * FROM assignment WHERE project_offer_id = ? AND student_code = ?",
                    new AssignmentMapper(), projectOfferId, studentCode);
        } catch (DataAccessException ignored) {
            value = null;
        }
        return Optional.ofNullable(value);
    }

    public void add(@NotNull Assignment assignment) {
        final @Nullable LocalDate acceptanceDate = assignment.getAcceptanceDate();
        final @Nullable LocalDate rejectionDate = assignment.getRejectionDate();
        template.update(
                "INSERT INTO assignment(project_offer_id, student_code, tutor_code, proposal_date, acceptance_date, rejection_date, iglu_transfer_date, state) VALUES(?,?,?,?,?,?,?,?)",
                assignment.getProjectOfferId(),
                assignment.getStudentCode(),
                assignment.getTutorCode(),
                assignment.getProposalDate(),
                (acceptanceDate == null) ? null : Date.valueOf(acceptanceDate),
                (rejectionDate == null) ? null : Date.valueOf(rejectionDate),
                assignment.getIgluTransferDate(),
                assignment.getState().ordinal()
        );
    }

    public void update(@NotNull Assignment assignment) {
        final @Nullable LocalDate acceptanceDate = assignment.getAcceptanceDate();
        final @Nullable LocalDate rejectionDate = assignment.getRejectionDate();
        template.update(
                "UPDATE assignment SET tutor_code = ?, proposal_date = ?, acceptance_date = ?, rejection_date = ?, iglu_transfer_date = ?, state = ? WHERE project_offer_id = ? AND student_code = ?",
                assignment.getTutorCode(),
                assignment.getProposalDate(),
                (acceptanceDate == null) ? null : Date.valueOf(acceptanceDate),
                (rejectionDate == null) ? null : Date.valueOf(rejectionDate),
                assignment.getIgluTransferDate(),
                assignment.getState().ordinal(),
                assignment.getProjectOfferId(),
                assignment.getStudentCode()
        );
    }

    public void delete(int projectOfferId, @NotNull String studentCode) {
        template.update("DELETE FROM assignment WHERE project_offer_id = ? AND student_code = ?", projectOfferId, studentCode);
    }

    private static final class AssignmentMapper implements RowMapper<Assignment> {

        public @NotNull Assignment mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            final @Nullable Date acceptanceDate = rs.getDate("acceptance_date");
            final @Nullable Date rejectionDate = rs.getDate("rejection_date");
            final @NotNull Assignment assignment = new Assignment();
            assignment.setProjectOfferId(rs.getInt("project_offer_id"));
            assignment.setStudentCode(rs.getString("student_code"));
            assignment.setTutorCode(rs.getString("tutor_code"));
            assignment.setProposalDate(rs.getDate("proposal_date").toLocalDate());
            assignment.setAcceptanceDate((acceptanceDate == null) ? null : acceptanceDate.toLocalDate());
            assignment.setRejectionDate((rejectionDate == null) ? null : rejectionDate.toLocalDate());
            assignment.setIgluTransferDate(rs.getDate("iglu_transfer_date").toLocalDate());
            assignment.setState(AssignmentState.values()[rs.getInt("state")]);
            return assignment;
        }
    }
}
