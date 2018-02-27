package hu.bartl.todo.messaging;

import hu.bartl.todo.service.TaskService;

public class TaskMessageListener {

    private TaskService taskService;

    public TaskMessageListener(TaskService taskService) {
        this.taskService = taskService;
    }

    public void receiveTaskMessage(String description) {
        taskService.createTaskWithDescription(description);
    }
}
