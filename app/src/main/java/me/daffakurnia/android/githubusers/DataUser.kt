package me.daffakurnia.android.githubusers

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataUser(
    var login: String?,
    var id: Int?,
    var avatar_url: String?
) : Parcelable
