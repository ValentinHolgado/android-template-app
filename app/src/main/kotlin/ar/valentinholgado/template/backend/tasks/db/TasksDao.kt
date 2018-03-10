package ar.valentinholgado.template.backend.tasks.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Flowable

@Dao
interface TasksDao {

    @Query("SELECT * FROM tasks")
    fun getTasks(): Flowable<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE taskid = :taskId")
    fun deleteTask(taskId: String)

    @Query("DELETE FROM tasks")
    fun deleteAllTasks()
}