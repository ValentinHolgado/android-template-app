package ar.valentinholgado.template.backend.tasks.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "tasks")
class TaskEntity(
        @PrimaryKey
        @ColumnInfo(name = "taskid")
        val id: String,

        @ColumnInfo(name = "title")
        val title: String,

        @ColumnInfo(name = "description")
        val description: String,

        @ColumnInfo(name = "completed")
        val completed: Boolean)