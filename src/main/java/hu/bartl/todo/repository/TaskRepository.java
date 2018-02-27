package hu.bartl.todo.repository;

import hu.bartl.todo.model.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TaskRepository extends CrudRepository<Task, UUID>{
}
