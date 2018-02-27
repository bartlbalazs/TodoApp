package hu.bartl.todo.model;

import lombok.Data;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

import static java.time.Instant.now;

@Data
@Entity
public class Task implements Identifiable<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID taskId;
    private Instant createdAt;
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
