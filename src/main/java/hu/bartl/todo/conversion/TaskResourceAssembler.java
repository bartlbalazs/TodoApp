package hu.bartl.todo.conversion;

import hu.bartl.todo.controller.TaskRestController;
import hu.bartl.todo.model.Task;
import hu.bartl.todo.model.TaskResource;
import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

@Component
public class TaskResourceAssembler extends IdentifiableResourceAssemblerSupport<Task, TaskResource> {

    public TaskResourceAssembler() {
        super(TaskRestController.class, TaskResource.class);
    }

    @Override
    public TaskResource toResource(Task task) {
        TaskResource resource;
        if (RequestContextHolder.getRequestAttributes() != null) {
            resource = createResourceWithId(task.getId(), task);
        } else {
            resource = new TaskResource();
        }
        resource.setTaskId(task.getId());
        resource.setDescription(task.getDescription());
        if (task.getCreatedAt() != null) {
            resource.setCreatedAt(task.getCreatedAt().toEpochMilli());
        } else {
            resource.setCreatedAt(0);
        }
        return resource;
    }
}
