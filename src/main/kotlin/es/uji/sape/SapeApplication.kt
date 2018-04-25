package es.uji.sape

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SapeApplication

fun main(args: Array<String>) {
    runApplication<SapeApplication>(*args)
}
