package edu.card.clarity.presentation.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.card.clarity.ui.theme.DarkAccentBlue
import edu.card.clarity.ui.theme.LightBlue

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    transparency: Float = 1f,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, DarkAccentBlue),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = LightBlue.copy(alpha = transparency)
        ),
        enabled = enabled
    ) {
        Box(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(
                text = text,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}