package com.example.chat.ui.signlechat

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chat.R
import com.example.chat.ui.common.CustomButton

@Composable
fun ChatScreen(
    chatId: String,
    onBackClick: () -> Unit,
    viewModel: SingleChatViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect = viewModel.effect

    LaunchedEffect(Unit) {
        viewModel.dispatchEvent(ChatContract.Event.LoadMessages)
    }

    LaunchedEffect(Unit) {
        effect.collect { effect ->
            when (effect) {
                ChatContract.Effect.NavigateBack -> onBackClick()
            }
        }
    }

    Image(
        painter = painterResource(R.drawable.gradient_background),
        contentDescription = stringResource(R.string.background),
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )

    Box(
        modifier = Modifier
            .padding(start = 24.dp, top = 60.dp)
    ) {
        CustomButton(
            icon = painterResource(R.drawable.ic_back),
            iconSize = 24.dp,
            onClick = onBackClick,
            modifier = Modifier
                .size(48.dp),
            shape = RoundedCornerShape(24.dp),
            backroundColor = Color.Black,
            backgroundAlpha = 0.4f,
            iconTint = Color.White,
            contentAlignment = Alignment.Center
        )

        Column(
            modifier = Modifier.padding(top = 60.dp)
                .verticalScroll(rememberScrollState())
        ) {
            when (state) {
                is ChatContract.State.Loading -> {}
                is ChatContract.State.Content -> {
                    val messages = (state as ChatContract.State.Content).messages
                    messages.forEach { message ->
                        MessageBuilder()
                            .setIsMine(message.isMine)
                            .apply {
                                message.text?.let { addText(it) }
                                if (message.images.isNotEmpty()) addImages(message.images)
                            }
                            .addTimestamp(message.timestamp)
                            .build()()
                    }
                }
                is ChatContract.State.Error -> {
                    val error = (state as ChatContract.State.Error).message
                }
            }
        }
    }
}
