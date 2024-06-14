package com.colledk.onboarding.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class OnboardingDestination : Parcelable {
    LOG_IN,
    SIGN_UP
}