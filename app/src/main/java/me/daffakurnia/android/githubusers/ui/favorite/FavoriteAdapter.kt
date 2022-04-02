package me.daffakurnia.android.githubusers.ui.favorite

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.daffakurnia.android.githubusers.R
import me.daffakurnia.android.githubusers.database.Favorite
import me.daffakurnia.android.githubusers.databinding.ItemUserBinding
import me.daffakurnia.android.githubusers.helper.FavoriteDiffCallback
import me.daffakurnia.android.githubusers.ui.detail.DetailActivity
import me.daffakurnia.android.githubusers.ui.main.ListUserAdapter

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private val listFavorites = ArrayList<Favorite>()
    fun setListUsers(listNotes: List<Favorite>) {
        val diffCallback = FavoriteDiffCallback(this.listFavorites, listNotes)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavorites.clear()
        this.listFavorites.addAll(listNotes)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorites[position])
    }

    override fun getItemCount(): Int = listFavorites.size

    inner class FavoriteViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: Favorite) {
            with(binding) {
                textUsername.text = favorite.login
                textUserUrl.text = favorite.url
                Glide.with(this.imgUser)
                    .load(favorite.avatar_url)
                    .circleCrop()
                    .into(this.imgUser)
                imgUser.setOnClickListener {
                    val intent = Intent(it.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.USERNAME, favorite.login)
                    intent.putExtra(DetailActivity.URL, favorite.url)
                    intent.putExtra(DetailActivity.AVATAR, favorite.avatar_url)
                    it.context.startActivity(intent)
                }
            }
        }
    }
}