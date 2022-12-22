package com.denicks21.sqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var edName: EditText
    private lateinit var edCity: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnView: Button
    private lateinit var btnUpdate: Button
    private lateinit var sqLiteHelper: SQLiteHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter: UserAdapter? = null
    private var user: UserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initRecyclerView()
        sqLiteHelper = SQLiteHelper(this)

        btnAdd.setOnClickListener { addUser() }
        btnView.setOnClickListener { getUser() }
        btnUpdate.setOnClickListener { updateUser() }

        adapter?.setOnClickItem {
            Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()
            edName.setText(it.name)
            edCity.setText(it.city)
            user = it
        }

        adapter?.setOnClickDeleteItem {
            deleteUser(it.id)
        }
    }

    private fun getUser() {
        val userList = sqLiteHelper.getAllUser()
        Log.e("PP", "${userList.size}")

        adapter?.addItems(userList)
    }

    private fun addUser() {
        val name = edName.text.toString()
        val city = edCity.text.toString()

        if (name.isEmpty() || city.isEmpty()) {
            Toast.makeText(this, R.string.requiriedFiled, Toast.LENGTH_SHORT).show()
        } else {
            val user = UserModel(name = name, city = city)
            val status = sqLiteHelper.insertUser(user)
            //
            if (status > -1) {
                Toast.makeText(this, R.string.userAdded, Toast.LENGTH_SHORT).show()
                clearEditText()
                getUser()
            } else {
                Toast.makeText(this, R.string.recordFailed, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUser() {
        val name = edName.text.toString()
        val location = edCity.text.toString()

        if (name == user?.name && location == user?.city) {
            Toast.makeText(this, R.string.valueFailed, Toast.LENGTH_SHORT).show()
            return
        }

        if (user == null) return

        val user = UserModel(id = user!!.id, name = name, city = location)
        val status = sqLiteHelper.updateUser(user)
        if (status > -1) {
            clearEditText()
            getUser()
        } else {
            Toast.makeText(this, R.string.updateFailed, Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteUser(id: Int) {
        if (id == null) return

        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.confirm)
        builder.setCancelable(true)
        builder.setPositiveButton(R.string.yes) { dialog, _ ->
            sqLiteHelper.deleteUserById(id)
            getUser()
            dialog.dismiss()
        }
        builder.setNegativeButton(R.string.no) { dialog, _ ->
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }

    private fun clearEditText() {
        edName.setText("")
        edCity.setText("")
        edName.requestFocus()
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UserAdapter()
        recyclerView.adapter = adapter
    }

    private fun initView() {
        edName = findViewById(R.id.edName)
        edCity = findViewById(R.id.edLocation)
        btnAdd = findViewById(R.id.btnAdd)
        btnView = findViewById(R.id.btnView)
        btnUpdate = findViewById(R.id.btnUpdate)
        recyclerView = findViewById(R.id.recyclerView)
    }
}