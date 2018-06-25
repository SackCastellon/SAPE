package es.uji.sape.model

import es.uji.sape.model.Itinerary.SOFTWARE_ENGINEERING
import es.uji.sape.model.Month.JANUARY
import java.math.BigDecimal

data class Student(
    var code: String = "",
    var name: String = "",
    var surname: String = "",
    var itinerary: Itinerary = SOFTWARE_ENGINEERING,
    var passedCredits: Int = 0,
    var averageScore: BigDecimal = BigDecimal.ZERO,
    var pendingSubjects: Int = 0,
    var internshipStartMonth: Month = JANUARY
)

