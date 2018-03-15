package es.uji.sape.dao;

import es.uji.sape.model.Business;
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
public class BusinessDao {

    private JdbcTemplate template;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") @NotNull DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    public @NotNull List<Business> findAll() {
        return template.query("SELECT * FROM business;", new BusinessMapper());
    }

    public @NotNull Optional<Business> find(@NotNull String cif) {
        @Nullable Business value;
        try {
            value = template.queryForObject("SELECT * FROM business WHERE cif = ?;", new BusinessMapper(), cif);
        } catch (DataAccessException ignored) {
            value = null;
        }
        return Optional.ofNullable(value);
    }

    public void add(@NotNull Business business) {
        template.update(
                "INSERT INTO business(cif, name, address, telephone) VALUES(?,?,?,?)",
                business.getCif(),
                business.getName(),
                business.getAddress(),
                business.getTelephone()
        );
    }

    public void update(@NotNull Business business) {
        template.update(
                "UPDATE business SET name = ?, address = ?, telephone = ? WHERE cif = ?",
                business.getName(),
                business.getAddress(),
                business.getTelephone(),
                business.getCif()
        );
    }

    public void delete(@NotNull String cif) {
        template.update("DELETE FROM business WHERE cif = ?", cif);
    }

    private static final class BusinessMapper implements RowMapper<Business> {

        public @NotNull Business mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            return new Business(
                    rs.getString("cif"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("telephone")
            );
        }
    }
}
