package es.uji.sape.tables

import es.uji.sape.model.Itinerary
import es.uji.sape.model.Month
import org.jetbrains.exposed.sql.Table

object Students : Table() {
    val code = varchar("code", 20).primaryKey()
    val name = varchar("name", 40)
    val surname = varchar("surname", 40)
    val itinerary = enumeration("itinerary", Itinerary::class.java)
    val passedCredits = integer("passed_credits")
    val averageScore = decimal("average_score", 4, 2)
    val pendingSubjects = integer("pending_subjects")
    val internshipStartMonth = enumeration("internship_start_month", Month::class.java)
}
