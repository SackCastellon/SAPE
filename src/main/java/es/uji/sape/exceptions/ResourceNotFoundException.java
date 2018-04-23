package es.uji.sape.exceptions;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.NotSerializableException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NOT_FOUND)
public final class ResourceNotFoundException extends RuntimeException {

    private final @NotNull String resourceName;
    private final @NotNull Map<String, Object> fields = new HashMap<>();

    public ResourceNotFoundException(@NotNull String resourceName, @NotNull Map<String, Object> fields) {
        super(String.format("Resource '%s' not found with fields: %s", resourceName, fields.entrySet().stream().map(it -> String.format("%s : '%s'", it.getKey(), it.getValue())).collect(Collectors.joining(", ", "[", "]"))));
        this.resourceName = resourceName;
        this.fields.putAll(fields);
    }

    private void readObject(java.io.ObjectInputStream in) throws NotSerializableException {
        throw new NotSerializableException("es.uji.sape.exceptions.ResourceNotFoundException");
    }

    private void writeObject(java.io.ObjectOutputStream out) throws NotSerializableException {
        throw new NotSerializableException("es.uji.sape.exceptions.ResourceNotFoundException");
    }
}
