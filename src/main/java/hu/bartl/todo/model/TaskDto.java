package hu.bartl.todo.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class TaskDto {

    @NotEmpty
    private String description;
}
