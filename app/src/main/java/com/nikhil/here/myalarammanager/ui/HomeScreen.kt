package com.nikhil.here.myalarammanager.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    navigateToScheduleAlarm: () -> Unit,
    mainViewModel: MainViewModel
) {
    val mainState by mainViewModel.container.stateFlow.collectAsState()



    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = navigateToScheduleAlarm,

                ) {
                Text(text = "Schedule Alarm")
            }

            Button(onClick = {
                mainViewModel.deleteTriggeredAlarms()
            }) {
                Text(text = "Delete Alarms")
            }
        }

        Divider(modifier = Modifier.padding(16.dp))
        LazyColumn {
            items(
                items = mainState.alarms,
            ) { item ->
                AlarmListItem(
                    alarmData = item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}