package `in`.rahul.todoapp.database

import androidx.room.*

@Dao
interface TodoDataDao {

    @Query("Select * from todoData")
    fun getAllData(): MutableList<TodoData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllData(todoData: MutableList<TodoData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOneData(todoData: TodoData)

    @Update
    fun updateData(todoData: TodoData)

}