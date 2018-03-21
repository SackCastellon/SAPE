package es.uji.sape.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import static es.uji.sape.model.Itinerary.SOFTWARE_ENGINEERING;

@Data
@NoArgsConstructor
public final class Student {

    private @NotNull String code = "";
    private @NotNull String name = "";
    private @NotNull String surname = "";
    private @NotNull Itinerary itinerary = SOFTWARE_ENGINEERING;
    private int passedCredits;
    private float averageScore;
    private int pendingSubjects;
    private int internshipStartSemester;
}
