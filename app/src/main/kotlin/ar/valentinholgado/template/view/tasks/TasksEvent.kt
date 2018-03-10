package ar.valentinholgado.template.view.tasks

import ar.valentinholgado.template.view.Event

data class TaskCheckboxEvent(val taskUiModel: TaskUiModel) : Event()
data class TaskClickedEvent(val taskUiModel: TaskUiModel) : Event()