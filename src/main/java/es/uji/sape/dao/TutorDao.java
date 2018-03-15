package es.uji.sape.dao;

import es.uji.sape.model.Itinerary;
import es.uji.sape.model.Tutor;
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
public class TutorDao {

    private JdbcTemplate template;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") @NotNull DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    public @NotNull List<Tutor> findAll() {
        return template.query("SELECT * FROM tutor;", new TutorMapper());
    }

    @SuppressWarnings("ConstantConditions")
    public @NotNull Optional<Tutor> find(@NotNull String dni) {
        return Optional.ofNullable(template.queryForObject("SELECT * FROM tutor WHERE dni = ?;", new TutorMapper(), dni));
    }

    public void add(@NotNull Tutor tutor) {
        template.update(
                "INSERT INTO tutor(dni, name, surname, code, itinerary, department, office) VALUES(?,?,?,?,?,?,?)",
                tutor.getDni(),
                tutor.getName(),
                tutor.getSurname(),
                tutor.getCode(),
                tutor.getItinerary().ordinal(),
                tutor.getDepartment(),
                tutor.getOffice()
        );
    }

    public void update(@NotNull Tutor tutor) {
        template.update(
                "UPDATE tutor SET name = ?, surname = ?, code = ?, itinerary = ?, department = ?, office = ? WHERE dni = ?",
                tutor.getName(),
                tutor.getSurname(),
                tutor.getCode(),
                tutor.getItinerary().ordinal(),
                tutor.getDepartment(),
                tutor.getOffice(),
                tutor.getDni()
        );
    }

    public void delete(@NotNull String dni) {
        template.update("DELETE FROM tutor WHERE dni = ?", dni);
    }

    private static final class TutorMapper implements RowMapper<Tutor> {

        public @NotNull Tutor mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            return new Tutor(
                    rs.getString("dni"),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("code"),
                    Itinerary.values()[rs.getInt("itinerary")],
                    rs.getString("department"),
                    rs.getString("office")
            );
        }
    }
}
