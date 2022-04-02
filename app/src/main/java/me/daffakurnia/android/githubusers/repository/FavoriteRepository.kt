package me.daffakurnia.android.githubusers.repository

import android.app.Application
import androidx.lifecycle.LiveData
import me.daffakurnia.android.githubusers.database.Favorite
import me.daffakurnia.android.githubusers.database.FavoriteDao
import me.daffakurnia.android.githubusers.database.FavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }

    fun getAllFavorite(): LiveData<List<Favorite>> = mFavoriteDao.getAllUsers()

    fun getFavorite(username: String): LiveData<Favorite> = mFavoriteDao.getUser(username)

    fun insert(favorite: Favorite) {
        executorService.execute { mFavoriteDao.insert(favorite) }
    }

    fun delete(favorite: Favorite) {
        executorService.execute { mFavoriteDao.delete(favorite) }
    }

    fun update(favorite: Favorite) {
        executorService.execute { mFavoriteDao.update(favorite) }
    }
}