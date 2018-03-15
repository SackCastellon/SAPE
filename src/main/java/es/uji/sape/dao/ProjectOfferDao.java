package es.uji.sape.dao;

import es.uji.sape.model.Itinerary;
import es.uji.sape.model.OfferState;
import es.uji.sape.model.ProjectOffer;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@ToString
@SuppressWarnings("DesignForExtension")
public class ProjectOfferDao {

    private JdbcTemplate template;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") @NotNull DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    public @NotNull List<ProjectOffer> findAll() {
        return template.query("SELECT * FROM project_offer;", new ProjectOfferMapper());
    }

    @SuppressWarnings("ConstantConditions")
    public @NotNull Optional<ProjectOffer> find(int id) {
        return Optional.ofNullable(template.queryForObject("SELECT * FROM project_offer WHERE id = ?;", new ProjectOfferMapper(), id));
    }

    public void add(@NotNull ProjectOffer projectOffer) {
        template.update(
                "INSERT INTO project_offer(id, itinerary, technologies, objectives, state, internship_offer_id) VALUES(?,?,?,?,?,?)",
                projectOffer.getId(),
                projectOffer.getItinerary().ordinal(),
                projectOffer.getTechnologies(),
                projectOffer.getObjectives(),
                projectOffer.getState().ordinal(),
                projectOffer.getInternshipOfferId()
        );
    }

    public void update(@NotNull ProjectOffer projectOffer) {
        template.update(
                "UPDATE project_offer SET itinerary = ?, technologies = ?, objectives = ?, state = ?, internship_offer_id = ? WHERE id = ?",
                projectOffer.getItinerary().ordinal(),
                projectOffer.getTechnologies(),
                projectOffer.getObjectives(),
                projectOffer.getState().ordinal(),
                projectOffer.getInternshipOfferId(),
                projectOffer.getId()
        );
    }

    public void delete(@NotNull String id) {
        template.update("DELETE FROM project_offer WHERE id = ?", id);
    }

    private static final class ProjectOfferMapper implements RowMapper<ProjectOffer> {

        public @NotNull ProjectOffer mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            return new ProjectOffer(
                    rs.getInt("id"),
                    Itinerary.values()[rs.getInt("itinerary")],
                    rs.getString("technologies"),
                    rs.getString("objectives"),
                    OfferState.values()[rs.getInt("state")],
                    rs.getInt("internship_offer_id")
            );
        }
    }
}
