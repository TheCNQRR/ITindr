package com.example.itindr.ui.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.itindr.R
import com.example.itindr.ui.common.CustomButton
import com.example.itindr.ui.theme.InterFontFamily

@Composable
fun ChatsScreen(
    onNavigateToStream: () -> Unit,
    onNavigateToPeople: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onChatClick: () -> Unit
) {
    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.gradient_background),
            contentDescription = stringResource(R.string.background),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Text(
            modifier = Modifier
                .padding(top = 60.dp, start = 24.dp),
            text = stringResource(R.string.chats),
            fontSize = 40.sp,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Box(
            modifier = Modifier
                .padding(top = 132.dp, start = 24.dp, end = 24.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clickable(onClick = onChatClick)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_mock_user_photo),
                        contentDescription = stringResource(R.string.Andrey),
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(start = 16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.Andrey),
                            fontSize = 16.sp,
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = stringResource(R.string.message),
                            fontSize = 16.sp,
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        )
                    }
                }
            }
        }

        NavigationBarChats(
            modifier = Modifier
                .padding(bottom = 48.dp)
                .align(Alignment.BottomCenter),
            onStreamClick = onNavigateToStream,
            onPeopleClick = onNavigateToPeople,
            onChatsClick = { },
            onProfileClick = onNavigateToProfile
        )
    }
}

@Composable
fun NavigationBarChats(
    modifier: Modifier = Modifier,
    onStreamClick: () -> Unit,
    onPeopleClick: () -> Unit,
    onChatsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val windowSize = LocalWindowInfo.current.containerSize
    val density = LocalDensity.current
    val screenWidthDp = with(density) { windowSize.width.toDp() }

    val canUseSidePadding = screenWidthDp - 136.dp >= 277.dp

    Box(
        modifier = modifier
            .then(
                if (canUseSidePadding) {
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 64.dp)
                } else {
                    Modifier.width(277.dp)
                }
            )
            .height(64.dp)
            .background(
                color = colorResource(R.color.nav_bar),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 8.dp,
                    start = 8.dp,
                    end = 8.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CustomButton(
                icon = painterResource(R.drawable.ic_stream),
                iconSize = 24.dp,
                onClick = onStreamClick,
                modifier = Modifier
                    .size(48.dp),
                shape = RoundedCornerShape(32.dp),
                backroundColor = colorResource(R.color.nav_bar),
                iconTint = Color.White,
                contentAlignment = Alignment.Center
            )

            CustomButton(
                icon = painterResource(R.drawable.ic_people),
                iconSize = 24.dp,
                onClick = onPeopleClick,
                modifier = Modifier
                    .size(48.dp),
                shape = RoundedCornerShape(32.dp),
                backroundColor = colorResource(R.color.nav_bar),
                iconTint = Color.White,
                contentAlignment = Alignment.Center
            )

            CustomButton(
                icon = painterResource(R.drawable.ic_chats),
                iconSize = 24.dp,
                onClick = onChatsClick,
                modifier = Modifier
                    .width(93.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(32.dp),
                backroundColor = colorResource(R.color.white),
                iconTint = Color.Black,
                text = stringResource(R.string.chats),
                textColor = Color.Black,
                textStyle = TextStyle(
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                ),
                contentAlignment = Alignment.Center
            )

            CustomButton(
                icon = painterResource(R.drawable.ic_profile),
                iconSize = 24.dp,
                onClick = onProfileClick,
                modifier = Modifier
                    .size(48.dp),
                shape = RoundedCornerShape(32.dp),
                backroundColor = colorResource(R.color.nav_bar),
                iconTint = Color.White,
                contentAlignment = Alignment.Center
            )
        }
    }
}
