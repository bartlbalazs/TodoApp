import {Component, Input, OnInit} from '@angular/core';
import {Task} from "../model/Task";

@Component({
    selector: 'app-tasks',
    templateUrl: './tasks.component.html'
})
export class TasksComponent implements OnInit {

    tasks: Task[];

    constructor() {
    }

    ngOnInit() {
        const t1 = new Task();
        t1.createdAt = 1;
        t1.description = "desc1";
        const t2 = new Task();
        t2.createdAt = 2;
        t2.description = "desc2";
        this.tasks = [t1, t2];
    }
}
