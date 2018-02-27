package hu.bartl.todo.model;

import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

import static java.time.Instant.now;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task implements Identifiable<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID taskId;
    private Instant createdAt;
    @NotEmpty
    private String description;

    @PrePersist
    public void setCreationTime() {
        this.createdAt = now();
    }

    @Override
    public UUID getId() {
        return taskId;
    }
}
