package es.uji.sape.model;

import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

@Data
public final class InternshipOffer {

    @Getter
    private final int id;
    @Getter
    private final int degree;
    @Getter
    private final @NotNull String tasks;
    @Getter
    private final @NotNull LocalDate startDate;
    @Getter
    private final @NotNull String contactUsername;
}
