package com.colledk.home.domain.usecase

import android.content.Context
import android.icu.number.Notation
import android.icu.number.NumberFormatter
import android.icu.number.Precision
import android.icu.text.CompactDecimalFormat
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import java.math.RoundingMode
import javax.inject.Inject

class FormatNumberUseCase @Inject constructor(@ApplicationContext private val context: Context) {
    operator fun invoke(num: Number): String {
        val currentLocale = context.resources.configuration.locales.get(0)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            NumberFormatter.withLocale(currentLocale)
                .roundingMode(RoundingMode.FLOOR)
                .precision(Precision.maxFraction(1))
                .notation(Notation.compactShort())
                .format(num)
                .toString()
        } else {
            CompactDecimalFormat.getInstance(
                /* locale = */ currentLocale,
                /* style = */ CompactDecimalFormat.CompactStyle.SHORT
            ).format(num)
        }
    }
}