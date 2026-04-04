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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.itindr.R
import com.example.itindr.ui.MainScreenActivity
import com.example.itindr.ui.common.CustomButton

private const val ASPECT_RATIO_WIDTH = 363f
private const val ASPECT_RATIO_HEIGHT = 624f
private val BIO_SPACING = 24.dp
private const val DURATION_300 = 300
private const val THRESHOLD = 0.5f
private const val ALPHA_60 = 0.6f
private const val MAX_LINES = 20
private val BUTTONS_HEIGHT = 56.dp
private const val DELTA = 200f
private val TRANSLATION_Y = 96.dp
private val PADDING_128 = 128.dp

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
                    .padding(top = dimensionResource(R.dimen.app_name_padding_top))
                    .width(dimensionResource(R.dimen.app_name_width))
                    .height(dimensionResource(R.dimen.app_name_height)),
                painter = painterResource(R.drawable.itindr_text),
                contentDescription = stringResource(R.string.app_name),
                tint = colorResource(R.color.white)
            )

            Spacer(modifier = Modifier
                .height(dimensionResource(R.dimen.spacer_24)))

            PersonCard(person)

            Spacer(modifier = Modifier
                .height(136.dp))
        }

        NavigationBarMain(
            modifier = Modifier
                .padding(bottom = dimensionResource(R.dimen.size_xxmedium))
                .align(Alignment.BottomCenter),
            onStreamClick = { },
            onPeopleClick = onNavigateToPeople,
            onChatsClick = onNavigateToChats,
            onProfileClick = onNavigateToProfile
        )
    }
}

@Suppress("LoopWithTooManyJumpStatements")
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
            bioHeight.floatValue + with(density) { BIO_SPACING.toPx() }
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
                top = dimensionResource(R.dimen.size_xxsmall),
                start = dimensionResource(R.dimen.size_xxsmall),
                end = dimensionResource(R.dimen.size_xxsmall)
            )
            .aspectRatio(ASPECT_RATIO_WIDTH / ASPECT_RATIO_HEIGHT)
            .clip(RoundedCornerShape(dimensionResource(R.dimen.size_medium)))
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

        val inter = FontFamily(Font(R.font.inter_bold, FontWeight.Bold))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = dimensionResource(R.dimen.size_xsmall)),
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
                    modifier = Modifier.padding(start = dimensionResource(R.dimen.size_xsmall),
                        bottom = dimensionResource(R.dimen.size_12)),
                    text = person.name,
                    fontSize = dimensionResource(R.dimen.font_24).value.sp,
                    fontFamily = inter,
                    color = Color.White
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = dimensionResource(R.dimen.size_xsmall), end = dimensionResource(
                            R.dimen.size_xsmall
                        )),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.size_small)),
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
                            start = dimensionResource(R.dimen.size_xsmall),
                            end = dimensionResource(R.dimen.size_xsmall),
                            top = dimensionResource(R.dimen.size_xxsmall))
                        .onGloballyPositioned { coordinates ->
                            bioHeight.floatValue = coordinates.size.height.toFloat()
                        }
                        .graphicsLayer { alpha = slideProgress.value }
                ) {
                    val interRegular = FontFamily(Font(R.font.inter_regular, FontWeight.Normal))

                    Text(
                        text = person.bio,
                        fontSize = dimensionResource(R.dimen.font_14).value.sp,
                        fontFamily = interRegular,
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
                        start = dimensionResource(R.dimen.size_xsmall),
                        end = dimensionResource(R.dimen.size_xsmall),
                        top = dimensionResource(R.dimen.size_xxsmall)
                    )
                    .height(BUTTONS_HEIGHT)
                    .onGloballyPositioned { coordinates ->
                        buttonRowBounds.value = coordinates.boundsInRoot()
                    },
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.size_xsmall))
            ) {
                CustomButton(
                    icon = painterResource(R.drawable.ic_dislike),
                    iconSize = dimensionResource(R.dimen.size_xxsmall),
                    onClick = { },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.size_medium)),
                    backroundColor = colorResource(R.color.dislike_background),
                    iconTint = Color.White,
                    contentAlignment = Alignment.Center
                )

                CustomButton(
                    icon = painterResource(R.drawable.ic_like),
                    iconSize = dimensionResource(R.dimen.size_xxsmall),
                    onClick = { },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.size_medium)),
                    backroundColor = colorResource(R.color.like_background),
                    iconTint = Color.White,
                    contentAlignment = Alignment.Center
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = dimensionResource(R.dimen.size_small))
        ) {
            Box(
                modifier = Modifier
                    .width(dimensionResource(R.dimen.scrollbar_widht))
                    .height(dimensionResource(R.dimen.scrollbar_height))
                    .background(
                        shape = RoundedCornerShape(dimensionResource(R.dimen.size_2)),
                        color = colorResource(R.color.black).copy(alpha = THRESHOLD)
                    )
            )

            Box(
                modifier = Modifier
                    .width(dimensionResource(R.dimen.scrollbar_widht))
                    .height(dimensionResource(R.dimen.size_xxsmall))
                    .graphicsLayer {
                        translationY = whiteBarProgress.value * with(density) { TRANSLATION_Y.toPx() }
                    }
                    .background(
                        shape = RoundedCornerShape(dimensionResource(R.dimen.size_2)),
                        color = colorResource(R.color.white)
                    )
            )
        }
    }
}

@Composable
fun TagChip(tag: String) {
    val inter = FontFamily(Font(R.font.inter_bold, FontWeight.Bold))

    Box(
        modifier = Modifier
            .wrapContentWidth()
            .heightIn(min = dimensionResource(R.dimen.size_xxsmall))
            .background(
                color = Color.Black.copy(alpha = THRESHOLD),
                shape = RoundedCornerShape(dimensionResource(R.dimen.size_xsmall))
            )
            .padding(horizontal = dimensionResource(R.dimen.size_12)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = tag,
            fontSize = dimensionResource(R.dimen.font_12).value.sp,
            fontFamily = inter,
            fontWeight = FontWeight.Medium,
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

    val canUseSidePadding = screenWidthDp - PADDING_128 >= dimensionResource(R.dimen.navbar_283)

    Box(
        modifier = modifier
            .then(
                if (canUseSidePadding) {
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(R.dimen.size_64))
                } else {
                    Modifier.width(dimensionResource(R.dimen.navbar_283))
                }
            )
            .height(dimensionResource(R.dimen.size_64))
            .background(
                color = colorResource(R.color.nav_bar),
                shape = RoundedCornerShape(dimensionResource(R.dimen.size_xxsmall))
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = dimensionResource(R.dimen.size_small),
                    start = dimensionResource(R.dimen.size_small),
                    end = dimensionResource(R.dimen.size_small)
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val interSemiBold = FontFamily(Font(R.font.inter_semi_bold, FontWeight.SemiBold))

            CustomButton(
                icon = painterResource(R.drawable.ic_stream),
                iconSize = dimensionResource(R.dimen.size_xxsmall),
                onClick = onStreamClick,
                modifier = Modifier
                    .width(99.dp)
                    .height(dimensionResource(R.dimen.size_xxmedium)),
                shape = RoundedCornerShape(dimensionResource(R.dimen.size_medium)),
                backroundColor = Color.White,
                iconTint = Color.Black,
                text = stringResource(R.string.stream),
                textColor = Color.Black,
                textStyle = TextStyle(
                    fontFamily = interSemiBold,
                    fontSize = dimensionResource(R.dimen.font_14).value.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                contentAlignment = Alignment.Center
            )

            CustomButton(
                icon = painterResource(R.drawable.ic_people),
                iconSize = dimensionResource(R.dimen.size_xxsmall),
                onClick = onPeopleClick,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.size_xxmedium)),
                shape = RoundedCornerShape(dimensionResource(R.dimen.size_medium)),
                backroundColor = colorResource(R.color.nav_bar),
                iconTint = Color.White,
                contentAlignment = Alignment.Center
            )

            CustomButton(
                icon = painterResource(R.drawable.ic_chats),
                iconSize = dimensionResource(R.dimen.size_xxsmall),
                onClick = onChatsClick,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.size_xxmedium)),
                shape = RoundedCornerShape(dimensionResource(R.dimen.size_medium)),
                backroundColor = colorResource(R.color.nav_bar),
                iconTint = Color.White,
                contentAlignment = Alignment.Center
            )

            CustomButton(
                icon = painterResource(R.drawable.ic_profile),
                iconSize = dimensionResource(R.dimen.size_xxsmall),
                onClick = onProfileClick,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.size_xxmedium)),
                shape = RoundedCornerShape(dimensionResource(R.dimen.size_medium)),
                backroundColor = colorResource(R.color.nav_bar),
                iconTint = Color.White,
                contentAlignment = Alignment.Center
            )
        }
    }
}
