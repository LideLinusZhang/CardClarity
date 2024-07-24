package edu.card.clarity.presentation.purchaseBenefitsScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import edu.card.clarity.location.GeolocationInference

@Composable
fun GeolocationPopup(
    geolocationInference: GeolocationInference?,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (geolocationInference != null) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = "Nearby Merchant Detected",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    Text(
                        text = "We detected that you're near ",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    )
                    Text(
                        text = geolocationInference.merchantName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Would you like to shop in this purchase type?",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = onConfirm,
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    Text("No")
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = MaterialTheme.colorScheme.background,
            tonalElevation = 8.dp
        )
    } else {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = "Loading",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                )
            },
            text = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
                ) {
                    CircularProgressIndicator()
                }
            },
            confirmButton = {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    Text("Dismiss")
                }
            },
            dismissButton = {},
            shape = RoundedCornerShape(16.dp),
            containerColor = MaterialTheme.colorScheme.background,
            tonalElevation = 8.dp
        )
    }
}
