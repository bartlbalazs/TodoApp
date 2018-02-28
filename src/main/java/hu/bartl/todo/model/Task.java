package hu.bartl.todo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.Identifiable;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import static java.util.UUID.randomUUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task implements Identifiable<UUID>, Serializable {

    @Id
    private UUID taskId;
    private Instant createdAt;
    @NotEmpty
    private String description;

    @Override
    @JsonIgnore
    public UUID getId() {
        return taskId;
    }

    public static Task createWithDefaultValues(String description) {
        return builder().taskId(randomUUID()).description(description).createdAt(Instant.now()).build();
    }
}
