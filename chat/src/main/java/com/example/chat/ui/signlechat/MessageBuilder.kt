@file:Suppress("MagicNumber")
package com.example.chat.ui.signlechat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chat.R
import com.example.chat.ui.theme.InterFontFamily

class MessageBuilder {
    private var text: String? = null
    private var images = mutableListOf<Int>()
    private var timestamp: String = ""
    private var isMine: Boolean = false

    fun addText(text: String) = apply { this.text = text }

    fun addImages(images: List<Int>) = apply { this.images.addAll(images) }

    fun setIsMine(isMine: Boolean) = apply { this.isMine = isMine }

    fun addTimestamp(timestamp: String) = apply { this.timestamp = timestamp }

    @Suppress("LongMethod")
    fun build(): @Composable () -> Unit {
        return {
            val backgroundColor = if (isMine) {
                colorResource(R.color.my_message_background)
            } else {
                colorResource(R.color.message_background)
            }
            val alignment = if (isMine) Alignment.CenterEnd else Alignment.CenterStart
            val horizontalAlignment = if (isMine) Alignment.End else Alignment.Start

            val isSinglePhotoOnly = images.size == 1 && text.isNullOrBlank()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 16.dp),
                contentAlignment = alignment
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 280.dp)
                        .clip(RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isMine) 16.dp else 0.dp,
                            bottomEnd = if (isMine) 0.dp else 16.dp
                        ))
                        .background(if (isSinglePhotoOnly) Color.Transparent else backgroundColor)
                        .padding(bottom = if (isSinglePhotoOnly) 0.dp else 8.dp),
                    horizontalAlignment = horizontalAlignment
                ) {
                    if (isSinglePhotoOnly) {
                        Box(contentAlignment = Alignment.BottomEnd) {
                            PhotoItem(images[0], Modifier.fillMaxWidth().height(240.dp))

                            Box(
                                modifier = Modifier
                                    .padding(bottom = 4.dp, end = 12.dp)
                                    .background(
                                        color = Color.Black.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(100.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = timestamp,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontFamily = InterFontFamily,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    } else {
                        if (images.isNotEmpty()) {
                            PhotoGrid(images)
                            Spacer(modifier = Modifier.height(4.dp))
                        }

                        if (!text.isNullOrBlank()) {
                            Text(
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = if (images.isEmpty()) 12.dp else 4.dp
                                ),
                                text = text!!,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Normal,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }

                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            text = timestamp,
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 12.sp,
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PhotoGrid(images: List<Int>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        when (images.size) {
            1 -> PhotoItem(images[0], Modifier.fillMaxWidth().height(240.dp))
            2 -> PhotoRow(images, 180.dp, 2.dp)
            3 -> {
                PhotoItem(images[0], Modifier.fillMaxWidth().height(180.dp))
                PhotoRow(images.drop(1), 120.dp, 2.dp)
            }
            4 -> {
                PhotoRow(images.take(2), 150.dp, 2.dp)
                PhotoRow(images.drop(2), 150.dp, 2.dp)
            }
            else -> {
                val rows = distributeImages(images)
                rows.forEach { rowImages ->
                    val rowHeight = if (rowImages.size > 3) 80.dp else 110.dp
                    PhotoRow(rowImages, rowHeight, 2.dp)
                }
            }
        }
    }
}

private fun distributeImages(images: List<Int>): List<List<Int>> {
    val count = images.size
    return when (count) {
        5 -> listOf(images.take(2), images.drop(2))
        6 -> listOf(images.take(3), images.drop(3))
        7 -> listOf(images.take(3), images.drop(3).take(2), images.drop(5))
        8 -> listOf(images.take(3), images.drop(3).take(2), images.drop(5).take(3))
        9 -> images.chunked(3)
        10 -> listOf(images.take(3), images.drop(3).take(3),
            images.drop(6).take(2), images.drop(8).take(2))
        else -> images.chunked(3)
    }
}

@Composable
fun PhotoItem(imageResId: Int, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = imageResId),
        contentDescription = stringResource(R.string.photo),
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}

@Composable
fun PhotoRow(items: List<Int>, height: Dp, spacing: Dp) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        items.forEach { item ->
            PhotoItem(imageResId = item, modifier = Modifier.weight(1f).fillMaxHeight())
        }
    }
}
