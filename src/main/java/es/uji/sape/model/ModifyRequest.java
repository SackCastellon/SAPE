package es.uji.sape.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.time.Instant;


@Data
@NoArgsConstructor
public class ModifyRequest {
    private @NotNull Date date = (Date) java.util.Date.from(Instant.MIN);
    private @NotNull String message = "";
    private int id;
    private int project_offer_id;

}
