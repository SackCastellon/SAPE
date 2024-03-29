package es.uji.sape.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import static es.uji.sape.model.Itinerary.SOFTWARE_ENGINEERING;
import static es.uji.sape.model.Month.JANUARY;

@Data
@NoArgsConstructor
public final class Student {
    public static final int CODE_MAX_LENGTH = 20;
    public static final int NAME_MAX_LENGTH = 40;

    private @NotNull String code = "";
    private @NotNull String name = "";
    private @NotNull String surname = "";
    private @NotNull Itinerary itinerary = SOFTWARE_ENGINEERING;
    private int passedCredits;
    private float averageScore;
    private int pendingSubjects;
    private @NotNull Month internshipStartSemester = JANUARY;
}
