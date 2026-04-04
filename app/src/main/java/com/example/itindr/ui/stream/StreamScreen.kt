package com.example.itindr.ui.stream

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.itindr.R
import com.example.itindr.ui.MainScreenActivity
import com.example.itindr.ui.common.CustomButton
import com.example.itindr.ui.theme.InterFontFamily

private const val ASPECT_RATIO_WIDTH = 363f
private const val ASPECT_RATIO_HEIGHT = 624f
private const val DURATION_300 = 300
private const val THRESHOLD = 0.5f
private const val ALPHA_60 = 0.6f
private const val MAX_LINES = 20
private const val DELTA = 200f

@Composable
fun StreamScreen(
    person: MainScreenActivity.Person,
    onNavigateToPeople: () -> Unit,
    onNavigateToChats: () -> Unit,
    onNavigateToProfile: () -> Unit
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
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

            Spacer(modifier = Modifier
                .height(136.dp))
        }

        NavigationBarMain(
            modifier = Modifier
                .padding(bottom = 48.dp)
                .align(Alignment.BottomCenter),
            onStreamClick = { },
            onPeopleClick = onNavigateToPeople,
            onChatsClick = onNavigateToChats,
            onProfileClick = onNavigateToProfile
        )
    }
}

@Suppress("LoopWithTooManyJumpStatements", "CyclomaticComplexMethod")
@Composable
fun PersonCard(
    person: MainScreenActivity.Person
) {
    val expandProgress = remember { mutableFloatStateOf(0f) }
    val bioHeight = remember { mutableFloatStateOf(0f) }
    val nameAreaBounds = remember { mutableStateOf(Rect.Zero) }
    val cardLayoutCoordinates = remember { mutableStateOf<LayoutCoordinates?>(null) }
    val buttonRowBounds = remember { mutableStateOf(Rect.Zero) }
    val density = LocalDensity.current

    val offsetPx = remember(bioHeight.floatValue) {
        derivedStateOf {
            bioHeight.floatValue + with(density) { 24.dp.toPx() }
        }
    }

    val whiteBarProgress = animateFloatAsState(
        targetValue = expandProgress.floatValue,
        animationSpec = tween(durationMillis = DURATION_300)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 24.dp,
                start = 24.dp,
                end = 24.dp
            )
            .aspectRatio(ASPECT_RATIO_WIDTH / ASPECT_RATIO_HEIGHT)
            .clip(RoundedCornerShape(32.dp))
            .onGloballyPositioned { coordinates ->
                cardLayoutCoordinates.value = coordinates
            }
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    var isDraggingEnabled = false

                    while (true) {
                        val event = awaitPointerEvent()
                        when (event.type) {
                            PointerEventType.Press -> {
                                val change = event.changes.firstOrNull() ?: continue
                                val localPos = change.position
                                val layoutCoords = cardLayoutCoordinates.value
                                val globalPos = layoutCoords?.localToRoot(localPos) ?: continue
                                val dragZone = Rect(
                                    left = nameAreaBounds.value.left,
                                    top = nameAreaBounds.value.top,
                                    right = nameAreaBounds.value.right,
                                    bottom = buttonRowBounds.value.top
                                )
                                isDraggingEnabled = dragZone.contains(globalPos)
                            }

                            PointerEventType.Move -> {
                                if (!isDraggingEnabled) continue
                                val change = event.changes.firstOrNull() ?: continue
                                val delta = change.positionChange()
                                val deltaProgress = -delta.y / DELTA
                                val newProgress = (expandProgress.floatValue + deltaProgress)
                                    .coerceIn(0f, 1f)
                                expandProgress.floatValue = newProgress
                                change.consume()
                            }

                            PointerEventType.Release -> {
                                if (isDraggingEnabled) {
                                    expandProgress.floatValue =
                                        if (expandProgress.floatValue > THRESHOLD) 1f else 0f
                                }
                                isDraggingEnabled = false
                            }

                            else -> {}
                        }
                    }
                }
            }
    ) {
        val darkenProgress = animateFloatAsState(
            targetValue = expandProgress.floatValue,
            animationSpec = tween(durationMillis = DURATION_300)
        )

        Image(
            painter = painterResource(R.drawable.ic_mock_user_photo),
            contentDescription = stringResource(R.string.Andrey),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.Black.copy(alpha = ALPHA_60 * darkenProgress.value)
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            colorResource(R.color.gradient_start),
                            colorResource(R.color.gradient_end)
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        val animationSpec = tween<Float>(durationMillis = DURATION_300)
        val slideProgress = animateFloatAsState(
            targetValue = expandProgress.floatValue,
            animationSpec = animationSpec
        )

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
                        translationY = offsetPx.value * (1 - slideProgress.value)
                    }
                    .onGloballyPositioned { coordinates ->
                        nameAreaBounds.value = coordinates.boundsInRoot()
                    }
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            bottom = 12.dp
                        ),
                    text = person.name,
                    fontSize = 24.sp,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    person.tags.forEach { tag ->
                        TagChip(tag = tag)
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 24.dp
                        )
                        .onGloballyPositioned { coordinates ->
                            bioHeight.floatValue = coordinates.size.height.toFloat()
                        }
                        .graphicsLayer { alpha = slideProgress.value }
                ) {
                    Text(
                        text = person.bio,
                        fontSize = 14.sp,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        maxLines = MAX_LINES,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 24.dp
                    )
                    .height(56.dp)
                    .onGloballyPositioned { coordinates ->
                        buttonRowBounds.value = coordinates.boundsInRoot()
                    },
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CustomButton(
                    icon = painterResource(R.drawable.ic_dislike),
                    iconSize = 24.dp,
                    onClick = { },
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
                    onClick = { },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(32.dp),
                    backroundColor = colorResource(R.color.like_background),
                    iconTint = Color.White,
                    contentAlignment = Alignment.Center
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(120.dp)
                    .background(
                        shape = RoundedCornerShape(2.dp),
                        color = colorResource(R.color.black).copy(alpha = THRESHOLD)
                    )
            )

            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(24.dp)
                    .graphicsLayer {
                        translationY = whiteBarProgress.value * with(density) { 96.dp.toPx() }
                    }
                    .background(
                        shape = RoundedCornerShape(2.dp),
                        color = colorResource(R.color.white)
                    )
            )
        }
    }
}

@Composable
fun TagChip(tag: String) {
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .heightIn(min = 24.dp)
            .background(
                color = Color.Black.copy(alpha = THRESHOLD),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = tag,
            fontSize = 12.sp,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun NavigationBarMain(
    modifier: Modifier = Modifier,
    onStreamClick: () -> Unit,
    onPeopleClick: () -> Unit,
    onChatsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val windowSize = LocalWindowInfo.current.containerSize
    val density = LocalDensity.current
    val screenWidthDp = with(density) { windowSize.width.toDp() }

    val canUseSidePadding = screenWidthDp - 128.dp >= 283.dp

    Box(
        modifier = modifier
            .then(
                if (canUseSidePadding) {
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 64.dp)
                } else {
                    Modifier.width(283.dp)
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
                    .width(99.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(32.dp),
                backroundColor = Color.White,
                iconTint = Color.Black,
                text = stringResource(R.string.stream),
                textColor = Color.Black,
                textStyle = TextStyle(
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                ),
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
                    .size(48.dp),
                shape = RoundedCornerShape(32.dp),
                backroundColor = colorResource(R.color.nav_bar),
                iconTint = Color.White,
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
