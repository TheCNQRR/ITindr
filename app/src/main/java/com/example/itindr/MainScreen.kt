package com.example.itindr

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainScreen(person: MainScreenActivity.Person) {
    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.gradient_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .padding(top = 60.dp)
                    .width(114.dp)
                    .height(32.dp),
                painter = painterResource(R.drawable.itindr_text),
                contentDescription = stringResource(R.string.app_name),
                tint = colorResource(R.color.white)
            )

            Spacer(modifier = Modifier
                .height(24.dp))

            PersonCard(person)
        }

        NavigationBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        )
    }
}

@Composable
fun PersonCard(
    person: MainScreenActivity.Person
) {
    val expandProgress = remember { mutableFloatStateOf(0f) }
    val bioHeight = remember { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(
                top = 24.dp,
                start = 24.dp,
                end = 24.dp,
                bottom = 136.dp)
            .clip(RoundedCornerShape(32.dp))
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        when (event.type) {
                            PointerEventType.Move -> {
                                val change = event.changes.firstOrNull() ?: continue
                                val delta = change.positionChange()
                                val deltaProgress = -delta.y / 200f
                                val newProgress = (expandProgress.floatValue + deltaProgress).coerceIn(0f, 1f)
                                expandProgress.floatValue = newProgress
                            }
                            PointerEventType.Release -> {
                                expandProgress.floatValue = if (expandProgress.floatValue > 0.5f) 1f else 0f
                            }
                            else -> {}
                        }
                    }
                }
            }
    ) {
        val darkenProgress = animateFloatAsState(
            targetValue = expandProgress.floatValue,
            animationSpec = tween(durationMillis = 300),
            label = "darken"
        )

        Image(
            painter = painterResource(person.photoUrl),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.Black.copy(alpha = 0.6f * darkenProgress.value)
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x00000000),
                            Color(0x80000000)
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        val animationSpec = tween<Float>(durationMillis = 300)
        val slideProgress = animateFloatAsState(
            targetValue = expandProgress.floatValue,
            animationSpec = animationSpec,
            label = "slide"
        )

        val inter = FontFamily(Font(R.font.inter_bold, FontWeight.Bold))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        translationY =  -bioHeight.floatValue * slideProgress.value
                    }
            ) {
                Text(
                    modifier = Modifier.padding(start = 16.dp, bottom = 12.dp),
                    text = person.name,
                    fontSize = 24.sp,
                    fontFamily = inter,
                    color = Color.White
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    person.tags.forEach { tag ->
                        TagChip(tag = tag)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CustomButton(
                    icon = painterResource(R.drawable.ic_dislike),
                    iconSize = 24.dp,
                    onClick = {  },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(32.dp),
                    backroundColor = colorResource(R.color.dislike_background),
                    iconTint = Color.White,
                    contentAlignment = Alignment.Center
                )

                CustomButton(
                    icon = painterResource(R.drawable.ic_like),
                    iconSize = 24.dp,
                    onClick = {  },
                    modifier = Modifier
                        .weight(1f).
                        fillMaxHeight(),
                    shape = RoundedCornerShape(32.dp),
                    backroundColor = colorResource(R.color.like_background),
                    iconTint = Color.White,
                    contentAlignment = Alignment.Center
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp)
                .graphicsLayer {
                    translationY = -150f * slideProgress.value
                },
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .onGloballyPositioned { coordinates ->
                        bioHeight.floatValue = coordinates.size.height.toFloat()
                    }
                    .graphicsLayer {
                        alpha = slideProgress.value
                        translationY = (1f - slideProgress.value) * 50f
                    }
            ) {
                val interRegular = FontFamily(Font(R.font.inter_regular, FontWeight.Normal))

                Text(
                    text = person.bio,
                    fontSize = 14.sp,
                    fontFamily = interRegular,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }
        }
    }
}

@Composable
fun TagChip(tag: String) {
    val inter = FontFamily(Font(R.font.inter_bold, FontWeight.Bold))

    Box(
        modifier = Modifier
            .wrapContentWidth()
            .height(24.dp)
            .background(
                color = Color.Black.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = tag,
            fontSize = 12.sp,
            fontFamily = inter,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}

@Composable
fun NavigationBar(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(
                top = 0.dp,
                start = 64.dp,
                end = 64.dp
            )
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
                    start = 8.dp
                    ),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            val interSemiBold = FontFamily(Font(R.font.inter_semi_bold, FontWeight.SemiBold))

            CustomButton(
                icon = painterResource(R.drawable.ic_search),
                iconSize = 24.dp,
                onClick = {  },
                modifier = Modifier
                    .width(99.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(32.dp),
                backroundColor = Color.White,
                iconTint = Color.Black,
                text = stringResource(R.string.stream),
                textColor = Color.Black,
                textStyle = TextStyle(
                    fontFamily = interSemiBold,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                contentAlignment = Alignment.Center
            )

            CustomButton(
                icon = painterResource(R.drawable.ic_people),
                iconSize = 24.dp,
                onClick = {  },
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
                onClick = {  },
                modifier = Modifier
                    .size(48.dp),
                shape = RoundedCornerShape(32.dp),
                backroundColor = colorResource(R.color.nav_bar),
                iconTint = Color.White,
                contentAlignment = Alignment.Center
            )

            CustomButton(
                icon = painterResource(R.drawable.ic_profile),
                iconSize = 24.dp,
                onClick = {  },
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

@Composable
fun CustomButton(
    icon: Painter? = null,
    iconSize: Dp = 24.dp,
    onClick: () -> Unit,
    modifier: Modifier,
    shape: Shape,
    backroundColor: Color,
    iconTint: Color,
    text: String? = null,
    textColor: Color = Color.Black,
    textStyle: TextStyle = LocalTextStyle.current,
    contentAlignment: Alignment,
    horizontalArrangment: Arrangement.Horizontal = Arrangement.Start
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState()

    val backgroundAlpha = animateFloatAsState(
        targetValue = if (isPressed.value) 0.8f else 1f,
        animationSpec = tween(durationMillis = 100)
    )

    Box(
        modifier = modifier
            .background(
                color = backroundColor.copy(alpha = backgroundAlpha.value),
                shape = shape
            )
            .clickable(
                onClick = onClick,
                interactionSource = interactionSource,
                indication = null
            ),
        contentAlignment = contentAlignment
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalArrangement = horizontalArrangment,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize),
                    tint = iconTint
                )
            }

            if (text != null) {
                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = text,
                    color = textColor,
                    style = textStyle,
                    maxLines = 1
                )
            }
        }
    }
}
