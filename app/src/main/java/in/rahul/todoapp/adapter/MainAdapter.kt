package `in`.rahul.todoapp.adapter

import `in`.rahul.todoapp.R
import `in`.rahul.todoapp.activity.EditTodoActivity
import `in`.rahul.todoapp.model.TodoModel
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.main_layout.view.*

class MainAdapter(val context: Context, val todoList: MutableList<TodoModel>) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle = view.tv_title
        val clMain = view.cl_main
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.main_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listItem = todoList.get(position)
        holder.tvTitle.text = listItem.title
        holder.clMain.setOnClickListener {
            val stMessage =
                "userId: ${listItem.userId}, id: ${listItem.id}, comp: ${listItem.completed}"
//            showMessage(context, stMessage)
            val bundle = Bundle()
            bundle.putInt("userId", listItem.userId!!)
            bundle.putInt("id", listItem.id!!)
            bundle.putString("title", listItem.title)
            bundle.putString("description", listItem.description)

            context.startActivity(Intent(context, EditTodoActivity::class.java).putExtras(bundle))
        }
    }
}