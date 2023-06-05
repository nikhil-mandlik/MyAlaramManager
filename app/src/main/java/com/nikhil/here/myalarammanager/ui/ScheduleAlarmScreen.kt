package com.nikhil.here.myalarammanager.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.provider.Settings.ACTION_NOTIFICATION_ASSISTANT_SETTINGS
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.nikhil.here.myalarammanager.domain.alarm.AlarmData
import com.nikhil.here.myalarammanager.domain.alarm.AlarmMode
import com.nikhil.here.myalarammanager.ui.extensions.showToast
import java.text.SimpleDateFormat
import java.util.Locale

private const val TAG = "ScheduleAlarmScreen"

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleAlarmScreen(
    mainViewModel: MainViewModel,
    navigateBack: () -> Unit
) {

    val context = LocalContext.current

    var isPostNotificationPermGranted by remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(false)
        } else {
            mutableStateOf(true)
        }
    }

    val exactAlarmPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { isGranted ->
            isPostNotificationPermGranted = isGranted.all { it.value }
        }
    )

    val postNotifPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { isGranted ->
            isPostNotificationPermGranted = isGranted.all { it.value }
        }
    )



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "SCHEDULE ALARM", style = MaterialTheme.typography.labelMedium)

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        val datePickerState =
            rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
        val timePickerState = rememberTimePickerState()

        var showDatePickerDialog by remember {
            mutableStateOf(false)
        }

        var showTimePickerDialog by remember {
            mutableStateOf(false)
        }

        val dateFormatter = remember {
            SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        }

        val completeDateFormatter = remember {
            SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        }

        val selectedDate by remember {
            derivedStateOf {
                datePickerState.selectedDateMillis?.let { selectedDateMillis ->
                    dateFormatter.format(selectedDateMillis)
                } ?: "SELECT DATE"
            }
        }

        val selectedTime by remember {
            derivedStateOf {
                "${String.format("%02d", timePickerState.hour)}:${
                    String.format(
                        "%02d",
                        timePickerState.minute
                    )
                }:00"
            }
        }

        val completeDate by remember {
            derivedStateOf {
                try {
                    val output = completeDateFormatter.parse("$selectedDate $selectedTime")
                    output.time
                } catch (e: Exception) {
                    null
                }
            }
        }

        var title by remember {
            mutableStateOf(TextFieldValue(text = ""))
        }

        var isExact by remember {
            mutableStateOf(false)
        }

        var allowWhileIdle by remember {
            mutableStateOf(false)
        }

        TextField(
            value = title,
            onValueChange = {
                title = it
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "Enter Alarm Title")
            }
        )

        Divider(modifier = Modifier.padding(vertical = 16.dp))


        if (showDatePickerDialog) {
            DatePickerDialog(
                onDismissRequest = {
                    showDatePickerDialog = false
                },
                confirmButton = {
                    OutlinedButton(onClick = {
                        showDatePickerDialog = false
                    }) {
                        Text(text = "CONFIRM")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = {
                        showDatePickerDialog = false
                    }) {
                        Text(text = "DISMISS")
                    }
                },
                content = {
                    DatePicker(
                        state = datePickerState,
                        title = {
                            Text(text = "SELECT Alarm Date")
                        },
                    )
                }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = selectedDate, style = MaterialTheme.typography.titleLarge)
            Button(onClick = {
                showDatePickerDialog = true
            }) {
                Text(text = "Select Date")
            }
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        if (showTimePickerDialog) {
            Dialog(
                onDismissRequest = {
                    showTimePickerDialog = false
                },
                content = {
                    TimeInput(
                        state = timePickerState,
                        Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(16.dp)
                    )
                }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = selectedTime, style = MaterialTheme.typography.titleLarge)
            Button(onClick = {
                showTimePickerDialog = true
            }) {
                Text(text = "Select Time")
            }
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = isExact, onCheckedChange = {
                isExact = it
            })
            Text(text = "Schedule Exact Alarm")
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = allowWhileIdle, onCheckedChange = {
                allowWhileIdle = it
            })
            Text(text = "Allow While idle")
        }


        Spacer(modifier = Modifier.height(16.dp))



        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)) {
            Button(
                onClick = {
                    completeDate?.let { timestamp ->

                        val openPostNotificationSettings =
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                shouldShowRequestPermissionRationale(
                                    context as Activity,
                                    Manifest.permission.POST_NOTIFICATIONS
                                )
                            } else {
                                false
                            }

                        if (mainViewModel.hasExactAlarmPermission()) {
                            if (isPostNotificationPermGranted) {
                                mainViewModel.scheduleNotification(
                                    alarm = AlarmData(
                                        triggerTimestamp = timestamp,
                                        title = title.text,
                                        isExact = isExact,
                                        allowWhileIdle = allowWhileIdle,
                                        dateTimeString = "$selectedDate $selectedTime",
                                        alarmMode = AlarmMode.ALARM_MANAGER
                                    )
                                )
                                context.showToast(
                                    message = "Alarm Scheduled"
                                )
                                navigateBack()
                            } else {
                                if (openPostNotificationSettings.not() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    postNotifPermissionLauncher.launch(
                                        listOf(Manifest.permission.POST_NOTIFICATIONS).toTypedArray()
                                    )
                                } else {
                                    context.startActivity(Intent(ACTION_NOTIFICATION_ASSISTANT_SETTINGS))
                                }
                            }
                        } else {
                            context.startActivity(Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                        }
                    } ?: run {
                        context.showToast(
                            message = "Please select date and time"
                        )
                    }
                }
            ) {
                Text(text = "Schedule AlarmManager")
            }

            Button(
                onClick = {
                    completeDate?.let { timestamp ->

                        val openPostNotificationSettings =
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                shouldShowRequestPermissionRationale(
                                    context as Activity,
                                    Manifest.permission.POST_NOTIFICATIONS
                                )
                            } else {
                                false
                            }

                        if (isPostNotificationPermGranted) {
                            mainViewModel.scheduleNotificationThroughWorkManager(
                                alarm = AlarmData(
                                    triggerTimestamp = timestamp,
                                    title = title.text,
                                    isExact = isExact,
                                    allowWhileIdle = allowWhileIdle,
                                    dateTimeString = "$selectedDate $selectedTime",
                                    alarmMode = AlarmMode.WORK_MANAGER
                                )
                            )
                            context.showToast(
                                message = "Alarm Scheduled with work manager"
                            )
                            navigateBack()
                        } else {
                            if (openPostNotificationSettings.not() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                postNotifPermissionLauncher.launch(
                                    listOf(Manifest.permission.POST_NOTIFICATIONS).toTypedArray()
                                )
                            } else {
                                context.startActivity(Intent(ACTION_NOTIFICATION_ASSISTANT_SETTINGS))
                            }
                        }
                    } ?: run {
                        context.showToast(
                            message = "Please select date and time"
                        )
                    }
                }
            ) {
                Text(text = "Schedule WorkManager")
            }

        }



    }
}