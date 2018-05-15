package es.uji.sape.dao;

import es.uji.sape.model.InternshipOffer;
import es.uji.sape.model.Month;
import lombok.val;
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
public class InternshipOfferDao {

    private JdbcTemplate template;

    @Autowired
    public final void setDataSource(@Qualifier("dataSource") @NotNull DataSource dataSource) {
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
                "INSERT INTO internship_offer(id, degree, description, start_date, contact_username) VALUES(?,?,?,?,?)",
                internshipOffer.getId(),
                internshipOffer.getDegree(),
                internshipOffer.getDescription(),
                internshipOffer.getStartDate().getCode(),
                internshipOffer.getContactUsername()
        );
    }

    public void update(@NotNull InternshipOffer internshipOffer) {
        template.update(
                "UPDATE internship_offer SET degree = ?, description = ?, start_date = ?, contact_username = ? WHERE id = ?",
                internshipOffer.getDegree(),
                internshipOffer.getDescription(),
                internshipOffer.getStartDate().getCode(),
                internshipOffer.getContactUsername(),
                internshipOffer.getId()
        );
    }

    public void delete(int id) {
        template.update("DELETE FROM internship_offer WHERE id = ?", id);
    }

    private static final class InternshipOfferMapper implements RowMapper<InternshipOffer> {

        public @NotNull InternshipOffer mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            @NotNull val offer = new InternshipOffer();
            offer.setId(rs.getInt("id"));
            offer.setDegree(rs.getInt("degree"));
            offer.setDescription(rs.getString("description"));
            offer.setStartDate(Month.valueOf(rs.getInt("start_date")));
            offer.setContactUsername(rs.getString("contact_username"));
            return offer;
        }
    }
}
