package com.colledk.onboarding.ui.profilesetup

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.colledk.onboarding.R

@Composable
internal fun SelectPicturePane(
    profilePicture: Uri?,
    onPictureSelected: (uri: Uri?) -> Unit,
    onPictureRemoved: () ->  Unit,
    modifier: Modifier = Modifier
) {
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
            onPictureSelected(it)
        }

    ProfileSetup(
        titleId = R.string.add_picture_title,
        subtitleId = R.string.add_picture_subtitle,
        modifier = modifier
    ) {
        profilePicture?.let {
            Box(modifier = Modifier.fillMaxWidth(.8f)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(it)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)),
                    contentScale = ContentScale.FillWidth
                )
                IconButton(
                    onClick = onPictureRemoved,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        } ?: run {
            Image(
                painter = painterResource(id = R.drawable.add_picture),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(.8f).clickable {
                    launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            )
        }
    }
}
