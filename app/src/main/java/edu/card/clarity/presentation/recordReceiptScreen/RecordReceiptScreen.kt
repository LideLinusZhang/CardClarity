package edu.card.clarity.presentation.recordReceiptScreen

import android.Manifest
import android.app.DatePickerDialog
import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import edu.card.clarity.presentation.common.ImageDialog
import edu.card.clarity.presentation.common.TextField
import edu.card.clarity.ui.theme.CardClarityTheme
import edu.card.clarity.ui.theme.CardClarityTypography
import edu.card.clarity.ui.theme.DarkAccentBlue
import java.util.Calendar

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RecordReceiptScreen(
    navController: NavController,
    viewModel: RecordReceiptScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val allCardNames by viewModel.allCardNames.collectAsState()
    val context = LocalContext.current
    val cameraError = uiState.cameraError
    var showImage by remember { mutableStateOf(false) }

    if (showImage && uiState.photoPath != null) {
        ImageDialog(
            photoPath = uiState.photoPath!!,
            onClose = { showImage = false }
        )
    }

    if (cameraError != null) {
        ErrorDialog(error = cameraError, onDismiss = viewModel::resetCameraError)
    }

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                viewModel.updateDate("$year-${month + 1}-$dayOfMonth")
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
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

            // Show camera
            if (uiState.showCamera) {
                CameraCapture(
                    onImageCaptured = viewModel::onImageCaptured,
                    onError = { exception: Throwable ->
                        viewModel.onCameraError("Failed to capture image: ${exception.message}")
                    }
                )
            }

            // Show "View Receipt" and "Rescan Receipt" buttons
            if (uiState.photoPath != null) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    CustomButton(
                        onClick = { showImage = !showImage },
                        text = "View Receipt",
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    CustomButton(
                        onClick = {
                            if (cameraPermissionState.status.isGranted) {
                                viewModel.openCamera()
                            } else {
                                cameraPermissionState.launchPermissionRequest()
                            }
                        },
                        text = "Rescan Receipt",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Show "Scan Receipt" button
            else {
                CustomButton(
                    text = "Scan receipt",
                    onClick = {
                        if (cameraPermissionState.status.isGranted) {
                            viewModel.openCamera()
                        } else {
                            cameraPermissionState.launchPermissionRequest()
                        }
                    }
                )

            }

            Text("Detected information:")
            DatePickerField(
                date = uiState.date,
                label = "Date",
                onClick = { datePickerDialog.show() }
            )
            TextField(
                label = "Total Amount",
                text = uiState.totalAmount,
                placeholderText = "Enter total amount",
                onTextChange = viewModel::updateTotalAmount
            )
            TextField(
                label = "Merchant",
                text = uiState.merchant,
                placeholderText = "Enter merchant",
                onTextChange = viewModel::updateMerchant
            )
            DropdownMenu(
                label = "Select Card Used",
                options = allCardNames,
                selectedOption = uiState.selectedCreditCardName ?: "Select a card",
                onOptionSelected = viewModel::updateSelectedCreditCard
            )
            DropdownMenu(
                label = "Select Purchase Type",
                options = PurchaseType.entries.map { it.name },
                selectedOption = uiState.selectedPurchaseType ?: "Select a purchase type",
                onOptionSelected = viewModel::updateSelectedPurchaseType
            )
            CustomButton(
                text = "Add Receipt",
                onClick = {
                        Log.d("receipt", uiState.selectedCreditCardName!!)
                        viewModel.addReceipt()
                        navController.popBackStack()
                },
                enabled = uiState.selectedCreditCardName != null && uiState.selectedPurchaseType != null
            )
//                Spacer(modifier = Modifier.height(42.dp))
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