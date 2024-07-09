package com.colledk.user.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Language(
    val code: String = "",
    val name: String = "",
    val image: String = ""
) : Parcelable