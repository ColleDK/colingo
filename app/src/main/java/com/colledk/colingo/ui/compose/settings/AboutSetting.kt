package com.colledk.colingo.ui.compose.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.colledk.colingo.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AboutPane(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.settings_about_page_title),
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
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.settings_about_page_description1),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(id = R.string.settings_about_page_description2),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(id = R.string.settings_about_page_description3),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            SelectionContainer {
                Text(
                    text = buildAnnotatedString {
                        append(stringResource(id = R.string.settings_about_page_contact))
                        append(" ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(stringResource(id = R.string.settings_about_page_contact_email))
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}