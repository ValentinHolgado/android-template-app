package ar.valentinholgado.template.backend.tasks

import ar.valentinholgado.template.backend.Action
import ar.valentinholgado.template.backend.Result
import ar.valentinholgado.template.backend.tasks.db.TaskEntity
import ar.valentinholgado.template.backend.tasks.db.TasksDao
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import java.util.*
import kotlin.collections.ArrayList

class TasksRepository(private val inputStream: Subject<Action> = BehaviorSubject.create(),
                      private val outputStream: Subject<Result> = BehaviorSubject.create(),
                      val tasksDao: TasksDao) {

    val tasks = { actions: Observable<Action> ->
        actions.map { action ->
            when (action) {
                is NewTaskAction -> {
                    tasksDao.insertTask(TaskEntity(UUID.randomUUID().toString(),
                            action.title,
                            action.description,
                            false))
                    AddTaskResult(Result.Status.SUCCESS)
                }

                is UpdateTaskAction -> {
                    val task = action.task
                    tasksDao.insertTask(TaskEntity(task.id, task.title, task.description, task.completed))
                    AddTaskResult(Result.Status.SUCCESS)
                }

                is CompleteTaskAction -> {
                    TODO()
                    CompleteTaskResult(Result.Status.SUCCESS)
                }

                is ActivateTaskAction -> {
                    ActivateTaskResult(Result.Status.SUCCESS)
                }

                is DeleteTaskAction -> {
                    tasksDao.deleteTask(action.taskId)
                    DeleteTaskResult(Result.Status.SUCCESS)
                }

                is DeleteAllTasksAction -> {
                    tasksDao.deleteAllTasks()
                    GetTasksResult(ArrayList(), Result.Status.SUCCESS)
                }

                else -> TODO()
            }

        }
    }

    init {
        inputStream
                .compose(tasks)
                .subscribe(outputStream)

        tasksDao.getTasks().map { tasks ->
            GetTasksResult(
                    tasks.map { task ->
                        Task(task.id,
                                task.title,
                                task.description,
                                task.completed)
                    },
                    Result.Status.SUCCESS)
        }
    }

    fun inputStream(): Observer<Action> {
        return inputStream
    }

    fun outputStream(): Observable<Result> {
        return outputStream
    }
}