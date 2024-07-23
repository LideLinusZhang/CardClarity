package edu.card.clarity.presentation.purchaseBenefitsScreen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import edu.card.clarity.R
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.location.GeolocationInference
import edu.card.clarity.presentation.utils.Destinations

@Composable
fun PurchaseScreen(navController: NavController) {
    val geolocationViewModel: GeolocationViewModel = hiltViewModel()
    val geolocationInference by geolocationViewModel.geolocationInference.collectAsState()
    val context = LocalContext.current

    var permissionsGranted by remember { mutableStateOf(false) }
    var permissionDenied by remember { mutableStateOf(false) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissionsGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        permissionDenied = !permissionsGranted
    }

    LaunchedEffect(Unit) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED -> {
                permissionsGranted = true
            }
            else -> {
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    if (permissionsGranted) {
        LaunchedEffect(Unit) {
            geolocationViewModel.fetchGeolocationInference()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 32.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Best Card for Every Purchase",
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Categories:",
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        CategoryGrid(navController)

        if (permissionsGranted && geolocationInference != null) {
            val inference = geolocationInference?.firstOrNull()
            if (inference != null) {
                GeolocationPopup(inference)
            }
        }

        if (permissionDenied) {
            Text(
                text = "Location permissions are required to use this feature.",
                color = Color.Red,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun GeolocationPopup(inference: GeolocationInference) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable { },
        color = Color.Transparent
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "You are near ${inference.merchantName}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = { /* TO DO: Handle Yes action */ }) {
                            Text("Yes")
                        }
                        Button(onClick = { /* TO DO: Handle No action */ }) {
                            Text("No")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryGrid(navController: NavController) {
    val categories = PurchaseType.entries
    val displayNames = mapOf(
        PurchaseType.HomeImprovement to "Home Improvement"
    )

    LazyColumn {
        items(categories.chunked(2)) { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (category in rowItems) {
                    CategoryCard(category = category, displayNames) {
                        navController.navigate("${Destinations.PURCHASE_OPTIMAL_BENEFITS}/${category.name}")
                    }
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.size(150.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun CategoryCard(category: PurchaseType, displayNames: Map<PurchaseType, String>, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(8.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .size(150.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val imageRes = when (category) {
                PurchaseType.Pharmacy -> R.drawable.pharmacy
                PurchaseType.Entertainment -> R.drawable.entertainment
                PurchaseType.Furniture -> R.drawable.furniture
                PurchaseType.Gas -> R.drawable.gas
                PurchaseType.Hotel -> R.drawable.hotel
                PurchaseType.HomeImprovement -> R.drawable.home_improvement
                PurchaseType.Groceries -> R.drawable.groceries
                PurchaseType.Restaurants -> R.drawable.restaurants
                PurchaseType.Travel -> R.drawable.travel
                PurchaseType.Others -> R.drawable.others
            }
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = category.name,
                    modifier = Modifier.padding(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = displayNames[category] ?: category.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

@Composable
@Preview
fun PurchaseScreenPreview() {
    val navController = rememberNavController()
    PurchaseScreen(navController)
}
