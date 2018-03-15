package es.uji.sape.model;

import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Data
public final class Tutor {

    @Getter
    private final @NotNull String dni;
    @Getter
    private final @NotNull String name;
    @Getter
    private final @NotNull String surname;
    @Getter
    private final @NotNull String code;
    @Getter
    private final @NotNull Itinerary itinerary;
    @Getter
    private final @NotNull String department;
    @Getter
    private final @NotNull String office;
}
