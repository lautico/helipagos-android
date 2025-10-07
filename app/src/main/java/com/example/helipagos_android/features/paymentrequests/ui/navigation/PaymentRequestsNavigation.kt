package com.example.helipagos_android.features.paymentrequests.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.helipagos_android.features.paymentrequests.ui.detail.PaymentDetailScreen
import com.example.helipagos_android.features.paymentrequests.ui.list.PaymentRequestsScreen

@Composable
fun PaymentRequestsNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "payment_requests"
    ) {
        composable("payment_requests") {
            PaymentRequestsScreen(
                onPaymentRequestClick = { paymentId ->
                    navController.navigate("payment_detail/$paymentId")
                }
            )
        }
        
        composable("payment_detail/{paymentId}") { backStackEntry ->
            val paymentId = backStackEntry.arguments?.getString("paymentId") ?: ""
            PaymentDetailScreen(
                paymentId = paymentId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}