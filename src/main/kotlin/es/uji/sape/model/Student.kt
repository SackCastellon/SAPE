package es.uji.sape.model

import es.uji.sape.model.Itinerary.SOFTWARE_ENGINEERING
import es.uji.sape.model.Month.JANUARY

data class Student(
    var code: String = "",
    var name: String = "",
    var surname: String = "",
    var itinerary: Itinerary = SOFTWARE_ENGINEERING,
    var passedCredits: Int = 0,
    var averageScore: Float = 0.0F,
    var pendingSubjects: Int = 0,
    var internshipStartSemester: Month = JANUARY
)

