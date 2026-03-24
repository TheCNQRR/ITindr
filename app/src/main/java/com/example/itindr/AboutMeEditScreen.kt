package com.example.itindr

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AboutMeEditScreen(
    onBackClick: () -> Unit
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
        }

        val interBold = FontFamily(Font(R.font.inter_bold, FontWeight.Bold))

        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 75.dp),
            text = stringResource(R.string.about_yourself),
            fontSize = 16.sp,
            fontFamily = interBold,
            color = Color.White
        )
    }
}
