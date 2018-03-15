package es.uji.sape.dao;

import es.uji.sape.model.ContactPerson;
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
public class ContactPersonDao {

    private JdbcTemplate template;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") @NotNull DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    public @NotNull List<ContactPerson> findAll() {
        return template.query("SELECT * FROM contact_person;", new ContactPersonMapper());
    }

    public @NotNull Optional<ContactPerson> find(@NotNull String username) {
        @Nullable ContactPerson value;
        try {
            value = template.queryForObject("SELECT * FROM contact_person WHERE username = ?;", new ContactPersonMapper(), username);
        } catch (DataAccessException ignored) {
            value = null;
        }
        return Optional.ofNullable(value);
    }

    public void add(@NotNull ContactPerson contactPerson) {
        template.update(
                "INSERT INTO contact_person(username, name, email, business_cif) VALUES(?,?,?,?)",
                contactPerson.getUsername(),
                contactPerson.getName(),
                contactPerson.getEmail(),
                contactPerson.getBusinessCif()
        );
    }

    public void update(@NotNull ContactPerson contactPerson) {
        template.update(
                "UPDATE contact_person SET name = ?, email = ?, business_cif = ? WHERE username = ?",
                contactPerson.getName(),
                contactPerson.getEmail(),
                contactPerson.getBusinessCif(),
                contactPerson.getUsername()
        );
    }

    public void delete(@NotNull String username) {
        template.update("DELETE FROM contact_person WHERE username = ?", username);
    }

    private static final class ContactPersonMapper implements RowMapper<ContactPerson> {

        public @NotNull ContactPerson mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            return new ContactPerson(
                    rs.getString("username"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("business_cif")
            );
        }
    }
}
