import {Component, Input, OnInit} from '@angular/core';
import {Task} from "../model/Task";
import {TaskService} from "../task.service";
import {Observable} from "rxjs/Observable";

@Component({
    selector: 'app-tasks',
    templateUrl: './tasks.component.html'
})
export class TasksComponent implements OnInit {

    tasks: Observable<Array<Task>>;

    constructor(private taskService: TaskService) {
    }

    ngOnInit() {
        this.tasks = this.taskService.getTasks();
    }
}
