package com.finalmobile.todo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.finalmobile.todo.R
import com.finalmobile.todo.database.ToDo
import com.finalmobile.todo.viewmodel.ToDoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var toDoViewModel: ToDoViewModel

    private lateinit var floatingActionButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        floatingActionButton = findViewById(R.id.fab)

        toDoViewModel = ViewModelProvider(this).get(ToDoViewModel::class.java)

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
                    //list
                }
                1 -> {
                    //Update
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
}
