package es.uji.sape.model;

import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Data
public final class Preference {

    @Getter
    private final int priority;
    @Getter
    private final @NotNull String studentCode;
    @Getter
    private final int projectOfferId;
}
