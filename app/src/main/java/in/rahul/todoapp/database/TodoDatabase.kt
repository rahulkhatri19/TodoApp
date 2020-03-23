package `in`.rahul.todoapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TodoData::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun todoDataDao(): TodoDataDao

    companion object {
        private var instance: TodoDatabase? = null

        fun getInstance(context: Context): TodoDatabase? {
            if (instance == null) {
                synchronized(TodoDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TodoDatabase::class.java,
                        "todo.db"
                    ).build()
                }
            }
            return instance
        }

        fun destroyInstance() {
            instance = null
        }
    }
}