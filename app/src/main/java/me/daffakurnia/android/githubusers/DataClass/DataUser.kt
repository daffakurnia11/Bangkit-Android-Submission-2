package me.daffakurnia.android.githubusers.DataClass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataUser(
    var login: String?,
    var url: String?,
    var avatar_url: String?
) : Parcelable
