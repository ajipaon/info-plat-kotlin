package com.paondev.infoplat.ui.screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.paondev.infoplat.data.api.JabarPajakResponse
import com.paondev.infoplat.navigation.VehicleDetailDestination
import com.paondev.infoplat.ui.components.CaptchaInputScreen
import com.paondev.infoplat.ui.components.ErrorState
import com.paondev.infoplat.ui.components.LicensePlateHeroDetail
import com.paondev.infoplat.ui.components.TaxStatusCard
import com.paondev.infoplat.ui.components.VehicleDetailSkeleton
import com.paondev.infoplat.ui.components.VehicleSpecificationCard
import com.paondev.infoplat.ui.viewmodel.VehicleDetailUiState
import com.paondev.infoplat.ui.viewmodel.VehicleDetailViewModel



@Composable
fun VehicleDetailScreen(
    navController: NavController,
    viewModel: VehicleDetailViewModel = hiltViewModel(),
    jabarPajakData: JabarPajakResponse? = null
) {
    val backStackEntry = navController.currentBackStackEntry
    val uiState by viewModel.uiState.collectAsState()
    
    // Parse parameters and fetch data
    LaunchedEffect(backStackEntry) {
        // First, check if pre-converted data is provided (backward compatibility)
        if (jabarPajakData != null) {
            // Use pre-converted data directly
            return@LaunchedEffect
        }
        
        // Check if encoded data is provided (backward compatibility)
        val encodedData = backStackEntry?.arguments?.getString("data")
        if (encodedData != null) {
            val decodedData = VehicleDetailDestination.parseData(encodedData)
            return@LaunchedEffect
        }
        
        // New flow: Parse plate parameters and fetch data
        val plateParams = VehicleDetailDestination.parsePlateParameters(
            provinceCode = backStackEntry?.arguments?.getString("provinceCode"),
            headPlat = backStackEntry?.arguments?.getString("headPlat"),
            bodyPlat = backStackEntry?.arguments?.getString("bodyPlat"),
            tailPlat = backStackEntry?.arguments?.getString("tailPlat"),
            noRangka = backStackEntry?.arguments?.getString("noRangka"),
            noNik = backStackEntry?.arguments?.getString("noNik")
        )
        
        if (plateParams != null) {
            viewModel.fetchVehicleData(
                provinceCode = plateParams.provinceCode,
                headPlat = plateParams.headPlat,
                bodyPlat = plateParams.bodyPlat,
                tailPlat = plateParams.tailPlat,
                noRangka = plateParams.noRangka,
                noNik = plateParams.noNik
            )
        }
    }
    
    // Use data from state
    val vehicleData = when (val state = uiState) {
        is VehicleDetailUiState.Success -> state.data.data
        else -> null
    }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        when (uiState) {
            is VehicleDetailUiState.Loading -> {
                item {
                    VehicleDetailSkeleton()
                }
            }
            is VehicleDetailUiState.NeedCaptcha -> {
                item {
                    CaptchaInputScreen(
                        navController = navController,
                        viewModel = viewModel,
                        captchaData = (uiState as VehicleDetailUiState.NeedCaptcha).captchaData
                    )
                }
            }
            is VehicleDetailUiState.Error -> {
                item {
                    ErrorState(message = (uiState as VehicleDetailUiState.Error).message)
                }
            }
            is VehicleDetailUiState.Success -> {
                val data = (uiState as VehicleDetailUiState.Success).data.data
                if (data != null) {
                    item {
                        LicensePlateHeroDetail(data.noPolisi, data.infoPkbPnpb?.wilayah)
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        VehicleSpecificationCard(data)
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        TaxStatusCard(data)
                    }
                }
            }
        }

//        item {
//            Spacer(modifier = Modifier.height(24.dp))
//            Button(
//                onClick = { },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp)
//                    .height(56.dp)
//                    .shadow(
//                        4.dp,
//                        RoundedCornerShape(12.dp),
//                        spotColor = MaterialTheme.colorScheme.tertiary
//                    ),
//                shape = RoundedCornerShape(12.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = MaterialTheme.colorScheme.tertiary,
//                    contentColor = MaterialTheme.colorScheme.onPrimary
//                )
//            ) {
//                Icon(
//                    Icons.Outlined.History,
//                    contentDescription = null,
//                    tint = MaterialTheme.colorScheme.onPrimary
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(
//                    "View Tax History",
//                    fontWeight = FontWeight.Bold,
//                    color = MaterialTheme.colorScheme.onPrimary
//                )
//            }
//
//            Spacer(modifier = Modifier.height(32.dp))
//        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun VehicleDetailScreenPreview() {
//    InfoPlatTheme {
//        VehicleDetailScreen()
//    }
//}
