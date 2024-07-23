package edu.card.clarity.presentation.recordReceiptScreen

import android.Manifest
import android.app.DatePickerDialog
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import edu.card.clarity.enums.CardNetworkType
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.presentation.common.DatePickerField
import edu.card.clarity.presentation.common.DropdownMenu
import edu.card.clarity.presentation.common.TextField
import edu.card.clarity.ui.theme.CardClarityTheme
import java.util.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RecordReceiptScreen(viewModel: RecordReceiptViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val showCamera by viewModel.showCamera.collectAsState()
    val cameraError by viewModel.cameraError.collectAsState()

    if (cameraError != null) {
        ErrorDialog(error = cameraError!!, onDismiss = { viewModel.resetCameraError() })
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
        ) {
            Text(
                text = "Record a Receipt",
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (showCamera) {
                CameraCapture(
                    onImageCaptured = viewModel::onImageCaptured,
                    onError = { exception ->
                        viewModel.onCameraError("Failed to capture image: ${exception.message}")
                    }
                )
            } else {

                Button(
                    onClick = {
                        if (cameraPermissionState.status.isGranted) {
                            viewModel.openCamera()
                        } else {
                            cameraPermissionState.launchPermissionRequest()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Scan your receipt")
                }

                // display receipt
                uiState.photoPath?.let { path ->
                    val imageBitmap = BitmapFactory.decodeFile(path).asImageBitmap()
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = "Captured Receipt",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(top = 16.dp)
                    )
                }
                LazyColumn {
                    item {
                        Text("Detected information:")
                    }
                    item {
                        DatePickerField(
                            date = uiState.date,
                            label = "Date",
                            onClick = { datePickerDialog.show() }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    item {
                        TextField(
                            label = "Total Amount",
                            text = uiState.totalAmount,
                            placeholderText = "Enter total amount",
                            onTextChange = viewModel::onTotalAmountChange
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    item {
                        TextField(
                            label = "Merchant",
                            text = uiState.merchant,
                            placeholderText = "Enter merchant",
                            onTextChange = viewModel::onMerchantChange
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    item {
                        DropdownMenu(
                            label = "Card Type",
                            options = CardNetworkType.entries.map { it.name },
                            selectedOption = uiState.selectedCard,
                            onOptionSelected = { viewModel.onCardSelected(CardNetworkType.entries[it].name) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    item {
                        DropdownMenu(
                            label = "Purchase Type",
                            options = PurchaseType.entries.map { it.name },
                            selectedOption = uiState.selectedPurchaseType,
                            onOptionSelected = { viewModel.onPurchaseTypeSelected(PurchaseType.entries[it].name) }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    item {
                        Button(
                            onClick = viewModel::addReceipt,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Add Receipt")
                        }
                    }
                }
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

@Preview(showBackground = true)
@Composable
fun RecordReceiptScreenPreview() {
    // mock data for preview
    val mockViewModel = RecordReceiptViewModel().apply {
        onDateChange("2024-07-20")
        onTotalAmountChange("45.99")
        onMerchantChange("Walmart")
        onCardSelected("Visa")
        onPurchaseTypeSelected("Groceries")
    }

    RecordReceiptScreen(viewModel = mockViewModel)
}
