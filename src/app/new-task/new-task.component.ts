import {Component, OnInit, ViewChild} from '@angular/core';
import {NgForm} from "@angular/forms";
import {TaskService} from "../task.service";

@Component({
    selector: 'app-new-task',
    templateUrl: 'new-task.component.html',
    styleUrls: ['new-task.component.css']
})
export class NewTaskComponent implements OnInit {

    @ViewChild('f') taskForm: NgForm;

    constructor(private taskService: TaskService) {
    }

    ngOnInit() {

    }

    onAddItem() {
        const content = this.taskForm.value.content;
        this.taskService.publishTask(content);
        this.taskForm.resetForm();
    }
}
