package com.colledk.colingo.ui.compose.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import com.colledk.colingo.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReportBugPane(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.settings_report_bug_page_title),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(24.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    append(stringResource(id = R.string.settings_report_bug_title))
                    append(" ")
                    withLink(
                        LinkAnnotation.Url(
                            url = stringResource(id = R.string.settings_report_bug_link),
                            styles = TextLinkStyles(style = SpanStyle(textDecoration = TextDecoration.Underline))
                        )
                    ) {
                        append(stringResource(id = R.string.settings_report_bug_link))
                    }
                },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}