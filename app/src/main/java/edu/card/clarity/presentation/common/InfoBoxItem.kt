package edu.card.clarity.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.card.clarity.ui.theme.CardClarityTypography
import edu.card.clarity.ui.theme.LightPurple
import edu.card.clarity.ui.theme.Purple40

@Composable
fun InfoBoxItem(mainTitle: String, subtitle: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                color = LightPurple,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(56.dp)
                    .background(color = Purple40)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = mainTitle,
                    style = CardClarityTypography.titleMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = subtitle,
                    style = CardClarityTypography.bodyLarge,
                    color = Color.DarkGray
                )
            }
        }
    }
}