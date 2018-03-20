package es.uji.sape.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public final class InternshipOffer {

    private int id;
    private int degree;
    private @NotNull String tasks = "";
    private @NotNull LocalDate startDate = LocalDate.now();
    private @NotNull String contactUsername = "";
}
