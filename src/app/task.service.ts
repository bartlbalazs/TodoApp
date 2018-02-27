import {Injectable} from '@angular/core';
//import {Stomp} from 'stompjs'; // the code completion works this way
import * as Stomp from "stompjs" // this works at runtime
import * as SockJS from 'sockjs-client'
import {Observable} from "rxjs/Observable";
import {Task} from "./model/Task";
import {BehaviorSubject} from "rxjs/BehaviorSubject";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class TaskService {

    private stompClient;
    private tasks: BehaviorSubject<Array<Task>> = new BehaviorSubject([]);

    constructor(private http: HttpClient) {
        this.initializeTasks();
        this.initializeWsConnection();
    }

    private initializeWsConnection() {
        const socket = new SockJS('/gs-guide-websocket');
        this.stompClient = Stomp.over(socket);
        const self = this;

        this.stompClient.connect({}, (frame) => {
            this.stompClient.subscribe('/task/created', taskMessage => {
                const tasks = self.tasks.getValue();

                let messageObject = JSON.parse(taskMessage.body);
                const task = new Task();
                task.createdAt = messageObject.createdAt.epochSecond;
                task.description = messageObject.description;
                tasks.push(task);
                self.tasks.next(tasks);
            });
        });
    }

    private initializeTasks() : void {
        this.http.get<Array<Task>>("/tasks").subscribe((tasks : Array<Task>) =>{
            this.tasks.next(tasks);
        });
    }

    public getTasks() : Observable<Array<Task>>  {
        return this.tasks;
    }
    public publishTask(description: String): void {
        this.stompClient.send("/app/createTask", {}, JSON.stringify({"description": description}));
    }

}
