package es.uji.sape.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Date;

@Data
@NoArgsConstructor
public class ModifyRequest {
    private @NotNull Date date = Date.from(Instant.MIN);
    private @NotNull String message = "";

}
