package es.uji.sape.model

import es.uji.sape.model.Itinerary.SOFTWARE_ENGINEERING

data class Tutor(
    val code: String = "",
    val name: String = "",
    val surname: String = "",
    val itinerary: Itinerary = SOFTWARE_ENGINEERING,
    val department: String = "",
    val office: String = ""
)

