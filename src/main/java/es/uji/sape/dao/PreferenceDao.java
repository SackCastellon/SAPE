package es.uji.sape.dao;

import es.uji.sape.model.Preference;
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
public class PreferenceDao {

    private JdbcTemplate template;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") @NotNull DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    public @NotNull List<Preference> findAll() {
        return template.query("SELECT * FROM preference;", new PreferenceMapper());
    }

    @SuppressWarnings("ConstantConditions")
    public @NotNull Optional<Preference> find(int projectOfferId, @NotNull String studentDni) {
        return Optional.ofNullable(template.queryForObject("SELECT * FROM preference WHERE student_dni = ? AND project_offer_id = ?",
                new PreferenceMapper(), studentDni, projectOfferId));
    }

    public void add(@NotNull Preference preference) {
        template.update(
                "INSERT INTO preference(priority, student_dni, project_offer_id) VALUES(?,?,?)",
                preference.getPriority(),
                preference.getStudentDni(),
                preference.getProjectOfferId()
        );
    }

    public void update(@NotNull Preference preference) {
        template.update(
                "UPDATE preference SET priority = ? WHERE student_dni = ? AND project_offer_id = ?",
                preference.getPriority(),
                preference.getStudentDni(),
                preference.getProjectOfferId()
        );
    }

    public void delete(@NotNull String studentDni, int projectOfferId) {
        template.update("DELETE FROM preference WHERE student_dni = ? AND project_offer_id = ?", studentDni, projectOfferId);
    }

    private static final class PreferenceMapper implements RowMapper<Preference> {

        public @NotNull Preference mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            return new Preference(
                    rs.getInt("priority"),
                    rs.getString("student_dni"),
                    rs.getInt("project_offer_id")
            );
        }
    }
}
