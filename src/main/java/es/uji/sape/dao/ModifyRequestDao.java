package es.uji.sape.dao;

import es.uji.sape.model.ModifyRequest;
import lombok.val;
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

@Repository
@SuppressWarnings("DesignForExtension")
public class ModifyRequestDao {

    private JdbcTemplate template;

    @Autowired
    public final void setDataSource(@Qualifier("dataSource") @NotNull DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    public @NotNull List<ModifyRequest> findPerBussiness(@NotNull int id) {
        return template.query("SELECT modify_request.date, modify_request.message FROM modify_request" +
                "JOIN project_offer ON modify_request_pid = project_offer.id" +
                "WHERE project_offer.id = ?;", new ModifyRequestMapper(), id);
    }

    private static final class ProjectOfferMapper implements RowMapper<ModifyRequest> {

        public @NotNull ModifyRequest mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            @NotNull val request = new ModifyRequest();
            request.setDate(rs.getDate("date"));
            request.setMessage(rs.getString("message"));
            return request;
        }
}
