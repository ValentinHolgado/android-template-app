package ar.valentinholgado.template.backend.tasks

data class Task(val id: String,
                val title: String,
                val description: String,
                val completed: Boolean)