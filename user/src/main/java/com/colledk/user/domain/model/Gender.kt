package com.colledk.user.domain.model

import androidx.annotation.StringRes
import com.colledk.user.R

enum class Gender(@StringRes val nameId: Int) {
    MALE(R.string.gender_male),
    FEMALE(R.string.gender_female),
    OTHER(R.string.gender_other)
}