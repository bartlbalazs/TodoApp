import {Component, OnInit, ViewChild} from '@angular/core';
import {NgForm} from "@angular/forms";

@Component({
    selector: 'app-new-task',
    templateUrl: 'new-task.component.html',
    styleUrls: ['new-task.component.css']
})
export class NewTaskComponent implements OnInit {

    @ViewChild('f') taskForm: NgForm;

    constructor() {
    }

    ngOnInit() {

    }

    onAddItem() {
        const content = this.taskForm.value.content;
        console.log(content);
        this.taskForm.resetForm();
    }
}
