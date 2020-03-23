package `in`.rahul.todoapp.activity

import `in`.rahul.todoapp.R
import `in`.rahul.todoapp.database.TodoData
import `in`.rahul.todoapp.database.TodoDatabase
import `in`.rahul.todoapp.model.TodoModel
import `in`.rahul.todoapp.utils.CommonUtils.showMessage
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_edit_todo.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditTodoActivity : AppCompatActivity() {
    var userId = 0
    var id = 0
    var title = ""
    var description = ""
    var mDb: TodoDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_todo)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        mDb = TodoDatabase.getInstance(this)

        if (intent.extras != null) {
            userId = intent.getIntExtra("userId", 0)
            id = intent.getIntExtra("id", 0)
            title = intent?.getStringExtra("title")!!
            if (intent.hasExtra("description")) {
                description = intent?.getStringExtra("description")!!
            }
        }

        tv_noteId.text = "Note Id: $id"
        tv_title.setText(title)
        if (!description.equals("")) {
            tv_description.setText(description)
        }

        fab_save.setOnClickListener {
            title = tv_title.text.toString()
            description = tv_description.text.toString()
            if (title.equals("") || title.equals(" ")) {
                showMessage(this, "Please provide a Title")
            } else if (description.equals("") || description.equals(" ")) {
                showMessage(this, "Please provide a Description")
            } else {
                val todoData = TodoData(id, userId, title, description)
                updateTodoInDb(todoData)
                finish()
            }
        }
    }

    private fun updateTodoInDb(todoData: TodoData) {
        GlobalScope.launch(Dispatchers.IO) {
            mDb?.todoDataDao()?.updateData(todoData)
        }
    }

    override fun onResume() {
        super.onResume()
        val ab = supportActionBar
        if (ab != null) {
            ab.title = "Edit Todo"
        }
        ab?.setDisplayHomeAsUpEnabled(true)
    }
}
