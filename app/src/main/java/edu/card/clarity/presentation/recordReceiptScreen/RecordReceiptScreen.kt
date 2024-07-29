package edu.card.clarity.presentation.recordReceiptScreen

import android.Manifest
import android.app.DatePickerDialog
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.presentation.common.CustomButton
import edu.card.clarity.presentation.common.DatePickerField
import edu.card.clarity.presentation.common.DropdownMenu
import edu.card.clarity.presentation.common.TextField
import edu.card.clarity.ui.theme.CardClarityTheme
import edu.card.clarity.ui.theme.CardClarityTypography
import edu.card.clarity.ui.theme.DarkAccentBlue
import java.util.Calendar

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RecordReceiptScreen(
    navController: NavController,
    viewModel: RecordReceiptViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val allCards by viewModel.allCards.collectAsState()
    val context = LocalContext.current
    val cameraError = uiState.cameraError

    if (cameraError != null) {
        ErrorDialog(error = cameraError, onDismiss = viewModel::resetCameraError)
    }

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                viewModel.onDateChange("$year-${month + 1}-$dayOfMonth")
            },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
    }

    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    CardClarityTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 32.dp, vertical = 40.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Filled.AddCircle, contentDescription = "Add Icon", tint = DarkAccentBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Record a Receipt",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    style = CardClarityTypography.titleLarge,
                    color = Color.Black
                )
            }
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.showCamera) {
                CameraCapture(
                    onImageCaptured = viewModel::onImageCaptured,
                    onError = { exception: Throwable ->
                        viewModel.onCameraError("Failed to capture image: ${exception.message}")
                    }
                )
            } else {
                uiState.photoPath?.let { path ->
                    val imageBitmap = BitmapFactory.decodeFile(path).asImageBitmap()
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = "Captured Receipt",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
                CustomButton(
                    text = "Scan your receipt",
                    onClick = {
                        if (cameraPermissionState.status.isGranted) {
                            viewModel.openCamera()
                        } else {
                            cameraPermissionState.launchPermissionRequest()
                        }
                    }
                )
                Text("Detected information:")
                DatePickerField(
                    date = uiState.date,
                    label = "Date",
                    onClick = { datePickerDialog.show() }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    label = "Total Amount",
                    text = uiState.totalAmount,
                    placeholderText = "Enter total amount",
                    onTextChange = viewModel::onTotalAmountChange
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    label = "Merchant",
                    text = uiState.merchant,
                    placeholderText = "Enter merchant",
                    onTextChange = viewModel::onMerchantChange
                )
                Spacer(modifier = Modifier.height(8.dp))
                DropdownMenu(
                    label = "Select Card Used",
                    options = allCards.map { it.name },
                    selectedOption = uiState.selectedCard?.name ?: "Select a card",
                    onOptionSelected = { viewModel.onCardSelected(allCards[it]) }
                )
                Spacer(modifier = Modifier.height(8.dp))
                DropdownMenu(
                    label = "Select Purchase Type",
                    options = PurchaseType.entries.map { it.name },
                    selectedOption = uiState.selectedPurchaseType,
                    onOptionSelected = { viewModel.onPurchaseTypeSelected(PurchaseType.entries[it].name) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomButton(
                    text = "Add Receipt",
                    onClick = {
                        if (uiState.selectedCard != null) {
                            Log.d("receipt", uiState.selectedCard!!.name)
                            viewModel.addReceipt()
                            navController.popBackStack()
                        }
                    },
                    enabled = uiState.selectedCard != null
                )
                Spacer(modifier = Modifier.height(42.dp))
            }
        }
    }
}

@Composable
fun ErrorDialog(error: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Error") },
        text = { Text(text = error) },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text("OK")
            }
        }
    )
}