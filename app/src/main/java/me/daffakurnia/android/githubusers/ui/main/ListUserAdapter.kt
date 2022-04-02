package me.daffakurnia.android.githubusers.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.daffakurnia.android.githubusers.R
import me.daffakurnia.android.githubusers.dataclass.DataUser
import me.daffakurnia.android.githubusers.ui.detail.DetailActivity

class ListUserAdapter(private val dataUser: ArrayList<DataUser>) :
    RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgUser: ImageView = itemView.findViewById(R.id.img_user)
        var textUsername: TextView = itemView.findViewById(R.id.text_username)
        var textUrl: TextView = itemView.findViewById(R.id.text_user_url)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (login, url, avatar_url) = dataUser[position]
        holder.textUsername.text = login
        holder.textUrl.text = url
        Glide.with(holder.imgUser.context)
            .load(avatar_url)
            .circleCrop()
            .into(holder.imgUser)
        holder.imgUser.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.USERNAME, login)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = dataUser.size
}