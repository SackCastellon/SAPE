package es.uji.sape.dao;

import es.uji.sape.model.Business;
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
public class BusinessDao {

    private JdbcTemplate template;

    @Autowired
    public final void setDataSource(@Qualifier("dataSource") final @NotNull DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    public @NotNull List<Business> findAll() {
        return template.query("SELECT * FROM business;", new BusinessMapper());
    }

    public @NotNull Optional<Business> find(final @NotNull String cif) {
        @Nullable Business value;
        try {
            value = template.queryForObject("SELECT * FROM business WHERE cif = ?;", new BusinessMapper(), cif);
        } catch (final DataAccessException ignored) {
            value = null;
        }
        return Optional.ofNullable(value);
    }

    public void add(final @NotNull Business business) {
        template.update(
                "INSERT INTO business(cif, name, address, telephone) VALUES(?,?,?,?)",
                business.getCif(),
                business.getName(),
                business.getAddress(),
                business.getTelephone()
        );
    }

    public void update(final @NotNull Business business) {
        template.update(
                "UPDATE business SET name = ?, address = ?, telephone = ? WHERE cif = ?",
                business.getName(),
                business.getAddress(),
                business.getTelephone(),
                business.getCif()
        );
    }

    public void delete(final @NotNull String cif) {
        template.update("DELETE FROM business WHERE cif = ?", cif);
    }

    private static final class BusinessMapper implements RowMapper<Business> {

        public @NotNull Business mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            final val business = new Business();
            business.setCif(rs.getString("cif"));
            business.setName(rs.getString("name"));
            business.setAddress(rs.getString("address"));
            business.setTelephone(rs.getString("telephone"));
            return business;

        }
    }
}
