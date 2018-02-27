package hu.bartl.todo.model;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import java.util.UUID;

@Data
public class TaskResource extends ResourceSupport {

    private UUID taskId;
    private long createdAt;
    private String description;
}
