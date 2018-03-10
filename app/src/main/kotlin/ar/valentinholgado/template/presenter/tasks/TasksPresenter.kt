package ar.valentinholgado.template.presenter.tasks

import ar.valentinholgado.template.backend.tasks.TasksRepository
import ar.valentinholgado.template.view.Event
import ar.valentinholgado.template.view.ReactiveView
import ar.valentinholgado.template.view.tasks.TaskUiModel
import ar.valentinholgado.template.view.tasks.TasksUiModel
import java.util.*
import javax.inject.Inject

class TasksPresenter @Inject constructor(tasksView: ReactiveView<TasksUiModel, Event>,
                                         tasksRepository: TasksRepository) {

    init {
        val tasksModel = Arrays.asList(
                TaskUiModel("1", "Ir al supermercado", "", false),
                TaskUiModel("2", "Llamar al médico", "", false),
                TaskUiModel("3", "Hacer esto", "", false),
                TaskUiModel("4", "Ir al cine", "", false)
        )
        tasksView.inputStream().onNext(TasksUiModel(tasksModel))
    }
}