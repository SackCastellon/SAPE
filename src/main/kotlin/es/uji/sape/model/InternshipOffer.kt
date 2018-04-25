package es.uji.sape.model

import java.time.LocalDate

data class InternshipOffer(
    val id: Int = 0,
    val degree: Int = 0,
    val tasks: String = "",
    val startDate: LocalDate = LocalDate.now(),
    val contactUsername: String = ""
)
