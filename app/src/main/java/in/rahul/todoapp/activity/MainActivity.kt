package `in`.rahul.todoapp.activity

import `in`.rahul.todoapp.R
import `in`.rahul.todoapp.adapter.MainAdapter
import `in`.rahul.todoapp.database.TodoData
import `in`.rahul.todoapp.database.TodoDatabase
import `in`.rahul.todoapp.model.TodoModel
import `in`.rahul.todoapp.utils.ClientHelperInterface
import `in`.rahul.todoapp.utils.CommonUtils.LogMessage
import `in`.rahul.todoapp.utils.CommonUtils.isOnline
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

        recycleView.visibility = View.GONE
        shimmer_layout.visibility = View.VISIBLE
        shimmer_layout.startShimmer()

        mDB = TodoDatabase.getInstance(this@MainActivity)
        getTodoData()
        fab_add_note.setOnClickListener {
            startActivity(Intent(this, AddTodoActivity::class.java))
        }
    }

    private fun getTodoData() {
        ClientHelperInterface.create().getTodoData()
            .enqueue(object : Callback<MutableList<TodoModel>> {
                override fun onFailure(call: Call<MutableList<TodoModel>>, t: Throwable) {
                    LogMessage("Main Act", t.message.toString())
                }

                override fun onResponse(
                    call: Call<MutableList<TodoModel>>,
                    response: Response<MutableList<TodoModel>>
                ) {
                    //   LogMessage("Main Act: ", response.body().toString())

//                    recycleView.layoutManager =
//                        LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
//                    recycleView.adapter = MainAdapter(this@MainActivity, response.body()!!)
                    insertIntoDatabase(response.body()!!)
                }

            })
    }

    private fun insertIntoDatabase(dataList: MutableList<TodoModel>) {
        GlobalScope.launch(Dispatchers.IO) {
            val localDataList = mutableListOf<TodoData>()
            for (i in dataList.indices) {
                val todoData = TodoData(
                    dataList[i].id,
                    dataList[i].userId,
                    dataList[i].title,
                    dataList[i].description
                )
                localDataList.add(todoData)
            }
            mDB?.todoDataDao()?.insertAllData(localDataList)
        }
        fetchTodoFromDb()
    }

    override fun onResume() {
        super.onResume()
        if (!isOnline(this)) {
            internetDialog()
        }
        getTodoData()
        shimmer_layout.startShimmer()
    }

    override fun onPause() {
        shimmer_layout.stopShimmer()
        super.onPause()
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
                getTodoData()
                dialog.dismiss()
            }
        }
        runOnUiThread {
            val alert = builder.create()
            alert.show()
        }
    }

    private fun fetchTodoFromDb() {
        GlobalScope.launch(Dispatchers.IO) {
            val todoDataList = mDB?.todoDataDao()?.getAllData()
            val todoModelList = mutableListOf<TodoModel>()
            if (todoDataList == null || todoDataList.size == 0) {
                noDataDialog()
            } else {
                LogMessage("main Act: ", "${todoDataList.size}")
                for (i in todoDataList.indices) {
                    LogMessage("main Act: ", "id: ${todoDataList[i].id}")
                    val todoModel = TodoModel()
                    todoModel.id = todoDataList[i].id
                    todoModel.userId = todoDataList[i].userId
                    todoModel.title = todoDataList[i].title
                    todoModel.description = todoDataList[i].description!!
                    todoModel.completed = false
                    todoModelList.add(todoModel)
                }
                runOnUiThread {
                    recycleView.layoutManager =
                        LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                    recycleView.adapter = MainAdapter(this@MainActivity, todoModelList)
                    recycleView.visibility = View.VISIBLE
                    shimmer_layout.visibility = View.GONE
                    shimmer_layout.stopShimmer()
                }
            }
        }
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}
