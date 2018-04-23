package es.uji.sape.dao;

import es.uji.sape.model.Itinerary;
import es.uji.sape.model.Tutor;
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
public class TutorDao {

    private JdbcTemplate template;

    @Autowired
    public final void setDataSource(@Qualifier("dataSource") @NotNull DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    public @NotNull List<Tutor> findAll() {
        return template.query("SELECT * FROM tutor;", new TutorMapper());
    }

    public @NotNull Optional<Tutor> find(@NotNull String code) {
        @Nullable Tutor value;
        try {
            value = template.queryForObject("SELECT * FROM tutor WHERE code = ?;", new TutorMapper(), code);
        } catch (DataAccessException ignored) {
            value = null;
        }
        return Optional.ofNullable(value);
    }

    public void add(@NotNull Tutor tutor) {
        template.update(
                "INSERT INTO tutor(name, surname, code, itinerary, department, office) VALUES(?,?,?,?,?,?)",
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
                "UPDATE tutor SET name = ?, surname = ?, itinerary = ?, department = ?, office = ? WHERE code = ?",
                tutor.getName(),
                tutor.getSurname(),
                tutor.getItinerary().ordinal(),
                tutor.getDepartment(),
                tutor.getOffice(),
                tutor.getCode()
        );
    }

    public void delete(@NotNull String code) {
        template.update("DELETE FROM tutor WHERE code = ?", code);
    }

    private static final class TutorMapper implements RowMapper<Tutor> {

        public @NotNull Tutor mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            @NotNull val tutor = new Tutor();
            tutor.setCode(rs.getString("code"));
            tutor.setName(rs.getString("name"));
            tutor.setSurname(rs.getString("surname"));
            tutor.setItinerary(Itinerary.values()[rs.getInt("itinerary")]);
            tutor.setDepartment(rs.getString("department"));
            tutor.setOffice(rs.getString("office"));
            return tutor;
        }
    }
}
