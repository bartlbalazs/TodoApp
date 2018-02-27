package hu.bartl.todo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class TaskDto {

    @NotEmpty
    private String description;
}
