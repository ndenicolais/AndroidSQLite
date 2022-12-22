package com.denicks21.sqlite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter: RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private var userList: ArrayList<UserModel> = ArrayList()
    private var onClickItem:((UserModel) -> Unit)? = null
    private var onClickDeleteItem:((UserModel) -> Unit)? = null

    fun addItems(items: ArrayList<UserModel>) {
        this.userList = items
        notifyDataSetChanged()
    }

    fun setOnClickItem(callback: (UserModel) -> Unit) {
        this.onClickItem = callback
    }

    fun setOnClickDeleteItem(callback: (UserModel) -> Unit){
        this.onClickDeleteItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.card_users, parent, false)
    )

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bindView(user)
        holder.itemView.setOnClickListener { onClickItem?.invoke(user) }
        holder.btnDelete.setOnClickListener { onClickDeleteItem?.invoke(user) }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(var view: View): RecyclerView.ViewHolder(view) {
        private var id = view.findViewById<TextView>(R.id.tvId)
        private var name = view.findViewById<TextView>(R.id.tvName)
        private var city = view.findViewById<TextView>(R.id.tvCity)
        var btnDelete = view.findViewById<Button>(R.id.btnDelete)

        fun bindView(user: UserModel) {
            id.text = user.id.toString()
            name.text = user.name
            city.text = user.city
        }
    }
}