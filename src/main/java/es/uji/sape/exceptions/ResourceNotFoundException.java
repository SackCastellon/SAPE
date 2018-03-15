package es.uji.sape.exceptions;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public final class ResourceNotFoundException extends RuntimeException {

    private final @NotNull String resourceName;
    private final @NotNull String fieldName;
    private final @NotNull Object fieldValue;

    public ResourceNotFoundException(@NotNull String resourceName, @NotNull String fieldName, @NotNull Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public @NotNull String getResourceName() {
        return resourceName;
    }

    public @NotNull String getFieldName() {
        return fieldName;
    }

    public @NotNull Object getFieldValue() {
        return fieldValue;
    }
}
