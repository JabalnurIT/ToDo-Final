package com.finalmobile.todo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.finalmobile.todo.R
import com.finalmobile.todo.adpater.ToDoAdapter
import com.finalmobile.todo.database.ToDo
import com.finalmobile.todo.viewmodel.ToDoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var toDoViewModel: ToDoViewModel
    private lateinit var toDoListAdapter: ToDoAdapter
    private lateinit var floatingActionButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        floatingActionButton = findViewById(R.id.fab)

        rv_notes.layoutManager = LinearLayoutManager(this)
        toDoListAdapter =
            ToDoAdapter(this) { toDoList, i ->
                showAlertMenu(toDoList)
            }
        rv_notes.adapter = toDoListAdapter

        toDoViewModel = ViewModelProvider(this).get(ToDoViewModel::class.java)
        toDoViewModel.getLists()?.observe(this, Observer {
            toDoListAdapter.setLists(it)
        })

        floatingActionButton.setOnClickListener {
            addList()
        }
    }

    private fun addList(){
        val addIntent = Intent(this, AddActivity::class.java)
        startActivity(addIntent)
    }

    private fun showAlertMenu(toDo: ToDo){
        val items = arrayOf("Rincian", "Ubah", "Hapus")

        val builder = AlertDialog.Builder(this)
        val alert = AlertDialog.Builder(this)
        builder.setItems(items){ dialog, which ->
            when(which){
                0 -> {
                    listDetails(alert, toDo)
                }
                1 -> {
                    updateList(toDo)
                }
                2 -> {
                    alert.setTitle("Hapus Tugas?")
                        .setMessage("Tugas yang dihapus tidak dapat dikembalikan, kamu yakin?")
                        .setPositiveButton("Iya"){dialog, _ ->
                            toDoViewModel.deleteList(toDo)
                            dialog.dismiss()
                        }
                        .setNegativeButton("Tidak"){dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }
        }
        builder.show()
    }
    private fun listDetails(alert: AlertDialog.Builder, toDo: ToDo){
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.item_detail, null)

        val title: TextView = dialogView.findViewById(R.id.title)
        val createdDate: TextView = dialogView.findViewById(R.id.created_date_content)
        val dueTime: TextView = dialogView.findViewById(R.id.due_time_content)
        val note: TextView = dialogView.findViewById(R.id.desc_content)

        title.text = toDo.title
        createdDate.text = toDo.strCreatedDate
        dueTime.text = "${toDo.strDueDate}, ${toDo.strDueHour}"
        note.text = toDo.note

        alert.setView(dialogView)
            .setNeutralButton("Kembali"){dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    private fun updateList(toDo: ToDo){
        val addIntent = Intent(this, UpdateActivity::class.java)
            .putExtra("EXTRA_LIST", toDo)
            .putExtra(UpdateActivity.EXTRA_TITLE_UPDATE, toDo.title)
            .putExtra(UpdateActivity.EXTRA_DATE_UPDATE, toDo.strDueDate)
            .putExtra(UpdateActivity.EXTRA_TIME_UPDATE, toDo.strDueHour)
            .putExtra(UpdateActivity.EXTRA_NOTE_UPDATE, toDo.note)
            .putExtra(UpdateActivity.EXTRA_IS_FINISHED_UPDATE, toDo.isFinished)
        startActivity(addIntent)
    }
}
