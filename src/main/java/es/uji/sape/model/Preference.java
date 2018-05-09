package es.uji.sape.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
public final class Preference {
    private int priority;
    private @NotNull String studentCode = "";
    private int projectOfferId;
    private String name;
}
