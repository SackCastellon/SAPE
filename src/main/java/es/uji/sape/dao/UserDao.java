package es.uji.sape.dao;

import es.uji.sape.security.User;
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

    private JdbcTemplate template;

    @Autowired
    public final void setDataSource(@Qualifier("dataSource") @NotNull DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    public @NotNull Optional<User> find(@NotNull String username) {
        @Nullable User value;
        try {
            value = template.queryForObject("SELECT * FROM user_info WHERE username = ?;", new UserMapper(), username);
        } catch (DataAccessException ignored) {
            value = null;
        }
        return Optional.ofNullable(value);
    }

    public void add(@NotNull User user) {
        template.update(
                "INSERT INTO user_info(id, username, password) VALUES(?,?,?)",
                user.getId(),
                user.getUsername(),
                user.getPassword()
        );
    }

    public void update(@NotNull User user) {
        template.update(
                "UPDATE user_info SET username = ?, password = ? WHERE id = ?",
                user.getUsername(),
                user.getPassword(),
                user.getId()
        );
    }

    public void delete(@NotNull String id) {
        template.update("DELETE FROM user_info WHERE id = ?", id);
    }

    private static final class UserMapper implements RowMapper<User> {

        public @NotNull User mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
        }
    }
}