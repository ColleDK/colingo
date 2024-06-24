package com.colledk.onboarding.ui.profilesetup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.util.fastForEach
import com.colledk.onboarding.R
import com.colledk.onboarding.domain.Topic

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun SelectTopicsPane(
    modifier: Modifier = Modifier
) {
    ProfileSetup(
        titleId = R.string.select_topics_title,
        subtitleId = R.string.select_topics_subtitle,
        modifier = modifier
    ) {

        val selectedTopics = remember {
            mutableStateListOf<Topic>()
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(.8f)
        ) {
            Topic.entries.fastForEach { topic ->
                val isSelected = selectedTopics.contains(topic)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.secondaryContainer)
                        .padding(4.dp)
                        .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                        .toggleable(
                            value = isSelected,
                            onValueChange = {
                                if (isSelected) {
                                    selectedTopics.remove(topic)
                                } else {
                                    selectedTopics.add(topic)
                                }
                            }
                        ),
                    verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically)
                ) {
                    Icon(
                        painter = painterResource(id = topic.icon),
                        contentDescription = null,
                        tint = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = stringResource(id = topic.topicName),
                        color = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}