package `in`.rahul.todoapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todoData")
data class TodoData(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo var userId: Int?,
    @ColumnInfo var title: String?,
    @ColumnInfo var description: String?
) {
}