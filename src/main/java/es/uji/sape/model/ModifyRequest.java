package es.uji.sape.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.time.Instant;


@Data
@NoArgsConstructor
public class ModifyRequest {
    private @NotNull Date date = (Date) Date.from(Instant.MIN);
    private @NotNull String message = "";
    private @NotNull int id;
    private @NotNull int project_offer_id;

}
