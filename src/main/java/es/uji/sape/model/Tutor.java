package es.uji.sape.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
@NoArgsConstructor
public final class Tutor {

    private @NotNull String code = "";
    private @NotNull String name = "";
    private @NotNull String surname = "";
    private @Nullable Itinerary itinerary;
    private @NotNull String department = "";
    private @NotNull String office = "";
}
