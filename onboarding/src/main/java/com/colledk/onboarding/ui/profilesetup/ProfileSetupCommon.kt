package com.colledk.onboarding.ui.profilesetup

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.colledk.onboarding.R

@Composable
internal fun ProfileSetup(
    @StringRes titleId: Int,
    @StringRes subtitleId: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FillOrMinHeight(minHeight = 24.dp)
        Image(
            painter = painterResource(id = R.drawable.world),
            modifier = Modifier.fillMaxWidth(),
            contentDescription = null // Not needed for talkback
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(id = titleId),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(id = subtitleId),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Spacer(modifier = Modifier.height(48.dp))

        // Content
        content()

        FillOrMinHeight(minHeight = 24.dp)
    }
}

/**
 * Workaround to fill entire space on larger screens,
 * but having minimum height on smaller screens or when keyboard is active.
 */
@Composable
private fun ColumnScope.FillOrMinHeight(minHeight: Dp) {
    Spacer(modifier = Modifier.weight(1f))
    Spacer(modifier = Modifier.height(minHeight))
}