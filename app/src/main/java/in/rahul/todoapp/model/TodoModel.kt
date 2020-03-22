package `in`.rahul.todoapp.model

import com.google.gson.annotations.SerializedName

class TodoModel {
    @SerializedName("userId")
    var userId: Int? = null

    @SerializedName("id")
    var id: Int? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("completed")
    var completed: Boolean? = null

    var description: String = ""
}