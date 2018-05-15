package es.uji.sape.dao;

import es.uji.sape.model.Itinerary;
import es.uji.sape.model.OfferState;
import es.uji.sape.model.ProjectOffer;
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
public class ProjectOfferDao {

    private JdbcTemplate template;

    @Autowired
    public final void setDataSource(@Qualifier("dataSource") @NotNull DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    public @NotNull List<ProjectOffer> findAll() {
        return template.query("SELECT * FROM project_offer;", new ProjectOfferMapper());
    }

    public @NotNull Optional<ProjectOffer> find(int id) {
        @Nullable ProjectOffer value;
        try {
            value = template.queryForObject("SELECT * FROM project_offer WHERE id = ?;", new ProjectOfferMapper(), id);
        } catch (DataAccessException ignored) {
            value = null;
        }
        return Optional.ofNullable(value);
    }

    public void add(@NotNull ProjectOffer projectOffer) {
        template.update(
                "INSERT INTO project_offer(id, itinerary, technologies, objectives, state) VALUES(?,?,?,?,?)",
                projectOffer.getId(),
                projectOffer.getItinerary().ordinal(),
                projectOffer.getTechnologies(),
                projectOffer.getObjectives(),
                projectOffer.getState().ordinal()
        );
    }

    public List<ProjectOffer> findForPreferences() {
        return template.query("SELECT * FROM internship_offer" +
                " WHERE id NOT IN (SELECT project_offer_id FROM preference)", new ProjectOfferMapper());

    }

    public void update(@NotNull ProjectOffer projectOffer) {
        template.update(
                "UPDATE project_offer SET itinerary = ?, technologies = ?, objectives = ?, state = ? WHERE id = ?",
                projectOffer.getItinerary().ordinal(),
                projectOffer.getTechnologies(),
                projectOffer.getObjectives(),
                projectOffer.getState().ordinal(),
                projectOffer.getId()
        );
    }

    public void delete(int id) {
        template.update("DELETE FROM project_offer WHERE id = ?", id);
    }

    private static final class ProjectOfferMapper implements RowMapper<ProjectOffer> {

        public @NotNull ProjectOffer mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            @NotNull val offer = new ProjectOffer();
            offer.setId(rs.getInt("id"));
            offer.setItinerary(Itinerary.values()[rs.getInt("itinerary")]);
            offer.setTechnologies(rs.getString("technologies"));
            offer.setObjectives(rs.getString("objectives"));
            offer.setState(OfferState.values()[rs.getInt("state")]);
            return offer;
        }
    }
}
