package es.uji.sape.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException(resourceName: String, fields: Map<String, Any>) :
    RuntimeException("Resource '$resourceName' not found with fields: ${fields.map { (k, v) -> "$k : '$v'" }.joinToString(", ", "[", "]")}")
