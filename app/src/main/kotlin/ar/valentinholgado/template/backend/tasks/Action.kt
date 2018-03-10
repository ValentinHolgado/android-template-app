package ar.valentinholgado.template.backend.tasks

import ar.valentinholgado.template.backend.Action

/**
 * Adds a new task to the repository
 */
class NewTaskAction(val title: String, val description: String) : Action()

/**
 * Updates an existing task
 */
class UpdateTaskAction(val task: Task) : Action()

/**
 * Mark a task as completed
 */
class CompleteTaskAction(val taskId: String) : Action()

/**
 * Makes a task active
 */
class ActivateTaskAction(val taskId: String) : Action()

/**
 * Deletes a task from the repository
 */
class DeleteTaskAction(val taskId: String) : Action()

/**
 * Remove all completed tasks from repository
 */
class ClearCompletedTasksAction() : Action()

/**
 * Forces a refresh on all tasks
 */
class RefreshTasksAction() : Action()

/**
 * Deletes all tasks from the repository
 */
class DeleteAllTasksAction() : Action()
