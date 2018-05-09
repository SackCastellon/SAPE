package es.uji.sape.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@ResponseStatus(HttpStatus.NOT_FOUND)
public final class ResourceNotFoundException extends RuntimeException {

    @NonNls
    private static final String CLASS_NAME = "es.uji.sape.exceptions.ResourceNotFoundException";

    public ResourceNotFoundException(final @NotNull String resourceName, final @NotNull Map<String, Object> fields) {
        super(String.format("Resource '%s' not found with fields: %s", resourceName, fields.entrySet().stream().map(it -> String.format("%s='%s'", it.getKey(), it.getValue())).collect(Collectors.joining(", ", "[", "]"))));
    }

    private void readObject(ObjectInputStream in) throws NotSerializableException {
        throw new NotSerializableException(CLASS_NAME);
    }

    private void writeObject(ObjectOutputStream out) throws NotSerializableException {
        throw new NotSerializableException(CLASS_NAME);
    }
}
