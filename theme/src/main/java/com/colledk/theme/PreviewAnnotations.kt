package com.colledk.theme

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview


@Preview(
    name = "Extra large font",
    group = "Font group",
    fontScale = 2.5f,
    showBackground = true
)
@Preview(
    name = "Large font",
    group = "Font group",
    fontScale = 1.5f,
    showBackground = true
)
@Preview(
    name = "Normal font",
    group = "Font group",
    fontScale = 1f,
    showBackground = true
)
annotation class PreviewFonts()

@Preview(
    name = "Dark mode",
    group = "Font group",
    fontScale = 1f,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
annotation class PreviewUiModes()

@PreviewUiModes
@PreviewFonts
annotation class PreviewAnnotations