package `in`.rahul.todoapp.activity

import `in`.rahul.todoapp.R
import `in`.rahul.todoapp.database.TodoDatabase
import `in`.rahul.todoapp.model.TodoModel
import `in`.rahul.todoapp.utils.CommonUtils.showMessage
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_add_todo.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddTodoActivity : AppCompatActivity() {

    var title = ""
    var description = ""
    var mDb: TodoDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)

        mDb = TodoDatabase.getInstance(this)

        fab_save.setOnClickListener {
            title = tv_title.text.toString()
            description = tv_description.text.toString()
            if (title.equals("") || title.equals(" ")){
                showMessage(this, "Please provide a Title")
            } else if (description.equals("") || description.equals(" ")){
                showMessage(this, "Please provide a Description")
            } else {
                val todoModel = TodoModel()
                todoModel.userId = 1
                todoModel.title = title
                todoModel.description = description
                addTodoInDb(todoModel)
            }

        }
    }

    private fun addTodoInDb(todoModel: TodoModel) {
        GlobalScope.launch(Dispatchers.IO) {
            mDb?.todoDataDao()?.insertOneData(todoModel)
        }
    }
}
