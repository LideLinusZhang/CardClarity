package edu.card.clarity.presentation.common

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog


@Composable
fun ImageDialog(photoPath: String, onClose: () -> Unit) {
    Dialog(onDismissRequest = onClose) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(8.dp)
        ) {
            Column {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
                Image(
                    bitmap = BitmapFactory.decodeFile(photoPath).asImageBitmap(),
                    contentDescription = "Receipt Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .align(Alignment.CenterHorizontally)
                        .graphicsLayer {
                            rotationZ = 90f  // receipt is originally rotated for some reason
                        }
                )
            }
        }
    }
}