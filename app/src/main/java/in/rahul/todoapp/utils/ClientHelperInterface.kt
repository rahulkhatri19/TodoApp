package `in`.rahul.todoapp.utils

import `in`.rahul.todoapp.model.TodoModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ClientHelperInterface {
    @GET("todos")
    fun getTodoData(): Call<MutableList<TodoModel>>

    companion object Factory {
        fun create(): ClientHelperInterface {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiUrlHelper.BaseUrl)
                .build()
            return retrofit.create(ClientHelperInterface::class.java)
        }
    }
}