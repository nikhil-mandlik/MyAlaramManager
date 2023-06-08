package com.nikhil.here.myalarammanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.nikhil.here.myalarammanager.ui.DozeModeAndAppStandByChecker
import com.nikhil.here.myalarammanager.ui.HomeScreen
import com.nikhil.here.myalarammanager.ui.MainViewModel
import com.nikhil.here.myalarammanager.ui.ScheduleAlarmScreen
import com.nikhil.here.myalarammanager.ui.navigation.NavGraph
import com.nikhil.here.myalarammanager.ui.theme.MyAlaramManagerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val mainViewModel by viewModels<MainViewModel>()


    @Inject
    lateinit var dozeModeAndAppStandByChecker: DozeModeAndAppStandByChecker

    @OptIn(ExperimentalMaterialNavigationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAlaramManagerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val bottomSheetNavigator = rememberBottomSheetNavigator()
                    val navController = rememberNavController(bottomSheetNavigator)

                    ModalBottomSheetLayout(bottomSheetNavigator = bottomSheetNavigator) {
                        NavHost(navController = navController, startDestination = NavGraph.HomeScreen.route) {
                            composable(route = NavGraph.HomeScreen.route) {
                                HomeScreen(
                                    navigateToScheduleAlarm = {
                                        navController.navigate(NavGraph.ScheduleAlarmScreen.route)
                                    },
                                    mainViewModel = mainViewModel
                                )
                            }

                            composable(route = NavGraph.ScheduleAlarmScreen.route) {
                                ScheduleAlarmScreen(
                                    mainViewModel = mainViewModel,
                                    navigateBack = {
                                        navController.popBackStack()
                                    },
                                    dozeModeAndAppStandByChecker = dozeModeAndAppStandByChecker
                                )
                            }
                        }
                    }
                }
            }
        }
    }


}
