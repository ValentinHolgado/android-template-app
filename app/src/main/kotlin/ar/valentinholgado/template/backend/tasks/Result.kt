package ar.valentinholgado.template.backend.tasks

import ar.valentinholgado.template.backend.Result

data class GetTasksResult(val tasks: List<Task>,
                          override val status: Status,
                          override val successMessage: String? = null,
                          override val errorMessage: String? = null) : Result()

data class AddTaskResult(override val status: Status) : Result()

data class DeleteTaskResult(override val status: Status) : Result()

data class CompleteTaskResult(override val status: Status) : Result()

data class ActivateTaskResult(override val status: Status) : Result()
