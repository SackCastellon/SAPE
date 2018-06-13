package es.uji.sape.dao;

import es.uji.sape.model.Role;
import es.uji.sape.security.User;
import lombok.val;
import org.jetbrains.annotations.NonNls;
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
import java.util.Optional;

@Repository
@SuppressWarnings("DesignForExtension")
public class UserDao {

    @NonNls
    private JdbcTemplate template;

    @Autowired
    public final void setDataSource(@Qualifier("dataSource") final @NotNull DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    public @NotNull Optional<User> find(final @NotNull String username) {
        @Nullable User value;
        try {
            value = template.queryForObject("SELECT * FROM login_info WHERE username = ?;", new UserMapper(), username);
        } catch (final DataAccessException ignored) {
            value = null;
        }
        return Optional.ofNullable(value);
    }

    public void add(final @NotNull User user) {
        template.update(
                "INSERT INTO login_info(id, username, password, role) VALUES(?,?,?,?)",
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole().ordinal()
        );
    }

    public void update(final @NotNull User user) {
        template.update(
                "UPDATE login_info SET username = ?, password = ?, role = ? WHERE id = ?",
                user.getUsername(),
                user.getPassword(),
                user.getRole().ordinal(),
                user.getId()
        );
    }

    public void delete(final @NotNull String id) {
        template.update("DELETE FROM login_info WHERE id = ?", id);
    }

    private static final class UserMapper implements RowMapper<User> {

        @Override
        public @NotNull User mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            @NotNull val user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setRole(Role.values()[rs.getInt("role")]);
            return user;
        }
    }
}
