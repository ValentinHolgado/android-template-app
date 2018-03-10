package ar.valentinholgado.template.view.tasks

data class TasksUiModel(val tasksList: List<TaskUiModel>)

data class TaskUiModel(val id: String,
                       val title: String,
                       val description: String,
                       val completed: Boolean)