package es.uji.sape.dao;

import es.uji.sape.model.InternshipOffer;
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
import java.util.List;
import java.util.Optional;

@Repository
@SuppressWarnings("DesignForExtension")
public class InternshipOfferDao {

    private JdbcTemplate template;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") @NotNull DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    public @NotNull List<InternshipOffer> findAll() {
        return template.query("SELECT * FROM internship_offer;", new InternshipOfferMapper());
    }

    public @NotNull Optional<InternshipOffer> find(int id) {
        @Nullable InternshipOffer value;
        try {
            value = template.queryForObject("SELECT * FROM internship_offer WHERE id = ?;", new InternshipOfferMapper(), id);
        } catch (DataAccessException ignored) {
            value = null;
        }
        return Optional.ofNullable(value);
    }

    public void add(@NotNull InternshipOffer internshipOffer) {
        template.update(
                "INSERT INTO internship_offer(id, degree, tasks, start_date, contact_username) VALUES(?,?,?,?,?)",
                internshipOffer.getId(),
                internshipOffer.getDegree(),
                internshipOffer.getTasks(),
                Date.valueOf(internshipOffer.getStartDate()),
                internshipOffer.getContactUsername()
        );
    }

    public void update(@NotNull InternshipOffer internshipOffer) {
        template.update(
                "UPDATE internship_offer SET degree = ?, tasks = ?, start_date = ?, contact_username = ? WHERE id = ?",
                internshipOffer.getDegree(),
                internshipOffer.getTasks(),
                Date.valueOf(internshipOffer.getStartDate()),
                internshipOffer.getContactUsername(),
                internshipOffer.getId()
        );
    }

    public void delete(@NotNull String id) {
        template.update("DELETE FROM internship_offer WHERE id = ?", id);
    }

    private static final class InternshipOfferMapper implements RowMapper<InternshipOffer> {

        public @NotNull InternshipOffer mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            return new InternshipOffer(
                    rs.getInt("id"),
                    rs.getInt("degree"),
                    rs.getString("tasks"),
                    rs.getDate("start_date").toLocalDate(),
                    rs.getString("telephone")
            );
        }
    }
}
