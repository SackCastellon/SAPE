package es.uji.sape.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;


@Data
@NoArgsConstructor
public class ModifyRequest {
    private @NotNull LocalDate date = LocalDate.now();
    private @NotNull String message = "";
    private int id;
    private int projectOfferId;

}
