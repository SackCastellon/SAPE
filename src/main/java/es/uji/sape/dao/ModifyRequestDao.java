package es.uji.sape.dao;

import es.uji.sape.model.ModifyRequest;
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
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@SuppressWarnings("DesignForExtension")
public class ModifyRequestDao {

    @NonNls
    private JdbcTemplate template;

    @Autowired
    public final void setDataSource(@Qualifier("dataSource") @NotNull DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    public @NotNull List<ModifyRequest> findPerBusiness(int id) {
        return template.query("SELECT modify_request.date, modify_request.message FROM modify_request JOIN project_offer ON modify_request.id = project_offer.id WHERE project_offer.id = ?;", new ModifyRequestMapper(), id);
    }


    public @NotNull Optional<ModifyRequest> find(int id) {
        @Nullable ModifyRequest value;
        try {
            value = template.queryForObject("SELECT * FROM modify_request WHERE id = ?;", new ModifyRequestMapper(), id);
        } catch (DataAccessException ignored) {
            value = null;
        }
        return Optional.ofNullable(value);
    }

    public void add(@NotNull ModifyRequest modifyRequest) {
        template.update(
                "INSERT INTO modify_request(id,project_offer_id,date,message) VALUES(?,?,?,?)",
                modifyRequest.getId(),
                modifyRequest.getProjectOfferId(),
                Date.valueOf(modifyRequest.getDate()),
                modifyRequest.getMessage()
        );
    }

    public void delete(int id) {
        template.update("DELETE FROM modify_request WHERE id = ?", id);
    }

    private static final class ModifyRequestMapper implements RowMapper<ModifyRequest> {

        @Override
        public @NotNull ModifyRequest mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            @NotNull val request = new ModifyRequest();
            request.setId(rs.getInt("id"));
            request.setProjectOfferId(rs.getInt("projectOfferId"));
            request.setDate(rs.getDate("date").toLocalDate());
            request.setMessage(rs.getString("message"));
            return request;
        }
    }
}
