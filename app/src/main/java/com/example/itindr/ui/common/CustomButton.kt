package com.example.itindr.ui.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.itindr.R
private const val DURATION_100 = 100
private const val ALPHA_80 = 0.8f
private val DEFAULT_ICON_SIZE = 24.dp


@Suppress("LoopWithTooManyJumpStatements")
@Composable
fun CustomButton(
    shape: Shape,
    backroundColor: Color,
    iconTint: Color,
    contentAlignment: Alignment,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    iconSize: Dp = DEFAULT_ICON_SIZE,
    backgroundAlpha: Float = 1f,
    text: String? = null,
    textColor: Color = Color.Black,
    textStyle: TextStyle = LocalTextStyle.current,
    horizontalArrangment: Arrangement.Horizontal = Arrangement.Start
) {
    val isPressed = remember { mutableStateOf(false) }

    val backgroundAlp = animateFloatAsState(
        targetValue = if (isPressed.value) ALPHA_80 else backgroundAlpha,
        animationSpec = tween(durationMillis = DURATION_100)
    )

    Box(
        modifier = modifier
            .background(
                color = backroundColor.copy(alpha = backgroundAlp.value),
                shape = shape
            )
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val pressEvent = awaitPointerEvent()
                        if (pressEvent.type != PointerEventType.Press) continue

                        isPressed.value = true
                        pressEvent.changes.forEach { it.consume() }

                        var isDragging = false

                        while (true) {
                            val event = awaitPointerEvent()
                            when (event.type) {
                                PointerEventType.Move -> {
                                    val change = event.changes.firstOrNull() ?: continue
                                    if (change.positionChange() != Offset.Zero) {
                                        isDragging = true
                                        isPressed.value = false
                                    }
                                    event.changes.forEach { it.consume() }
                                }
                                PointerEventType.Release -> {
                                    if (!isDragging) {
                                        onClick()
                                    }
                                    isPressed.value = false
                                    event.changes.forEach { it.consume() }
                                    break
                                }
                                else -> {}
                            }
                        }
                    }
                }
            },
        contentAlignment = contentAlignment
    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = dimensionResource(R.dimen.size_12),
                    vertical = dimensionResource(R.dimen.size_12)
                ),
            horizontalArrangement = horizontalArrangment,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    painter = icon,
                    contentDescription = stringResource(R.string.background),
                    modifier = Modifier.size(iconSize),
                    tint = iconTint
                )
            }

            if (text != null) {
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.size_4)))

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