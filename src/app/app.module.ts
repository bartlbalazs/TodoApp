import {BrowserModule} from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatCardModule} from '@angular/material/card';
import {MatListModule} from '@angular/material/list';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FlexLayoutModule} from "@angular/flex-layout";

import {AppComponent} from './app.component';
import {TasksComponent} from './tasks/tasks.component';
import {NewTaskComponent} from './new-task/new-task.component';
import {TaskComponent} from './task/task.component';
import {TaskService} from "./task.service";
import {Stomp} from 'stompjs'

@NgModule({
    declarations: [
        AppComponent,
        TasksComponent,
        NewTaskComponent,
        TaskComponent
    ],
    imports: [
        BrowserModule,
        MatToolbarModule,
        MatCardModule,
        MatListModule,
        FormsModule,
        MatInputModule,
        HttpClientModule,
        MatButtonModule,
        BrowserAnimationsModule,
        FlexLayoutModule
    ],
    providers: [TaskService],
    bootstrap: [AppComponent]
})
export class AppModule {
}
