package `in`.rahul.todoapp.database

import `in`.rahul.todoapp.model.TodoModel
import androidx.room.*

@Dao
interface TodoDataDao {

    @Query("Select * from todoData")
    fun getAllData(): MutableList<TodoModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllData(todoData: MutableList<TodoModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOneData(todoData: TodoModel)

    @Update
    fun updateData(todoData: TodoModel)

}