package es.uji.sape.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;


@Data
@NoArgsConstructor
public class ModifyRequest {
    private @NotNull LocalDate date = LocalDate.now();
    private @NotNull String message = "";
    private int id;
    private int project_offer_id;

}
