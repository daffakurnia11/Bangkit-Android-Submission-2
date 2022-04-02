package me.daffakurnia.android.githubusers.helper

import androidx.recyclerview.widget.DiffUtil
import me.daffakurnia.android.githubusers.database.Favorite

class FavoriteDiffCallback(
    private val mOldFavoriteList: List<Favorite>,
    private val mNewFavoriteList: List<Favorite>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = mOldFavoriteList.size

    override fun getNewListSize(): Int = mNewFavoriteList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldFavoriteList[oldItemPosition].id == mNewFavoriteList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = mOldFavoriteList[oldItemPosition]
        val newUser = mNewFavoriteList[newItemPosition]
        return oldUser.login == newUser.login
                && oldUser.url == newUser.url
                && oldUser.avatar_url == newUser.avatar_url
    }
}