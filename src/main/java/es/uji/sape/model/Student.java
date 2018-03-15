package es.uji.sape.model;

import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Data
public final class Student {

    @Getter
    private final @NotNull String dni;
    @Getter
    private final @NotNull String name;
    @Getter
    private final @NotNull String surname;
    @Getter
    private final int code;
    @Getter
    private final @NotNull Itinerary itinerary;
    @Getter
    private final int passedCredits;
    @Getter
    private final float averageScore;
    @Getter
    private final int pendingSubjects;
    @Getter
    private final int internshipStartSemester;
}
