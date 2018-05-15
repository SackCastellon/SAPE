package es.uji.sape.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
public final class InternshipOffer {
    private int id;
    private int degree;
    private @NotNull String description = "";
    private @NotNull Month startDate = Month.INDIFFERENT;
    private @NotNull String contactUsername = "";
}
