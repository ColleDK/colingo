package com.colledk.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

/**
 * TODO figure out how to create generic handler for previews
 */
//@PreviewAnnotations
//@Composable
//inline fun<reified T: PreviewParameterProvider<T>> PreviewHandler(
//    @PreviewParameter(T::class) data: PreviewData
//) {
//    ColingoTheme {
//        Surface(color = MaterialTheme.colorScheme.surface) {
//            data.content()
//        }
//    }
//}
//
//data class PreviewData(
//    val content: @Composable () -> Unit
//)

@Composable
fun debugPlaceholder() =
    if (LocalInspectionMode.current) {
        painterResource(id = R.drawable.mainafter)
    } else {
        null
    }