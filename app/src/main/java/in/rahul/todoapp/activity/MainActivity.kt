package `in`.rahul.todoapp.activity

import `in`.rahul.todoapp.R
import `in`.rahul.todoapp.adapter.MainAdapter
import `in`.rahul.todoapp.database.TodoDatabase
import `in`.rahul.todoapp.model.TodoModel
import `in`.rahul.todoapp.utils.ClientHelperInterface
import `in`.rahul.todoapp.utils.CommonUtils.LogMessage
import `in`.rahul.todoapp.utils.CommonUtils.isOnline
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    var mDB: TodoDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDB = TodoDatabase.getInstance(this@MainActivity)
        getTodoData()
        fab_add_note.setOnClickListener {
            startActivity(Intent(this, AddTodoActivity::class.java))
        }
    }

    private fun getTodoData() {
        ClientHelperInterface.create().getTodoData()
            .enqueue(object : Callback<ArrayList<TodoModel>> {
                override fun onFailure(call: Call<ArrayList<TodoModel>>, t: Throwable) {
                    LogMessage("Main Act", t.message.toString())
                }

                override fun onResponse(
                    call: Call<ArrayList<TodoModel>>,
                    response: Response<ArrayList<TodoModel>>
                ) {
                    //   LogMessage("Main Act: ", response.body().toString())

                    recycleView.layoutManager =
                        LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                    recycleView.adapter = MainAdapter(this@MainActivity, response.body()!!)
                    insertIntoDatabase(response.body()!!)
                }

            })
    }

    private fun insertIntoDatabase(dataList: ArrayList<TodoModel>) {
        GlobalScope.launch(Dispatchers.IO) {
            mDB?.todoDataDao()?.insertAllData(dataList)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isOnline(this)) {
            internetDialog()
        }
        getTodoData()
    }

    override fun onDestroy() {
        TodoDatabase.destroyInstance()
        super.onDestroy()
    }

    fun internetDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Please Connect to Internet")
        builder.setCancelable(false).setIcon(R.drawable.ic_internet)
            .setTitle("No Internet Connection")
        builder.setPositiveButton(
            "OK"
        ) { dialog, _ ->
            dialog.cancel()
            finish()
        }.setNegativeButton("Go Offline") { dialog, _ ->
            fetchTodoFromDb()
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }

    fun noDataDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Unable to Get Local Data\nPlease Connect to Internet")
        builder.setCancelable(false).setIcon(R.drawable.ic_internet)
            .setTitle("No Internet Connection")
        builder.setPositiveButton(
            "OK"
        ) { dialog, _ ->
            dialog.cancel()
            finish()
        }.setNegativeButton("Retry") { dialog, _ ->
            if (!isOnline(this)) {
                noDataDialog()
            } else {
                dialog.dismiss()
            }
        }
        val alert = builder.create()
        alert.show()
    }

    private fun fetchTodoFromDb() {
        GlobalScope.launch(Dispatchers.IO) {
            val todoData = mDB?.todoDataDao()?.getAllData()
            if (todoData == null && todoData?.size == 0) {
                noDataDialog()
            } else {
                recycleView.layoutManager =
                    LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                recycleView.adapter = MainAdapter(this@MainActivity, todoData!!)
            }
        }
    }
}
