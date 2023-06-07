package com.nikhil.here.myalarammanager.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nikhil.here.myalarammanager.domain.alarm.AlarmData
import com.nikhil.here.myalarammanager.domain.alarm.AlarmMode

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AlarmListItem(
    modifier: Modifier = Modifier,
    alarmData: AlarmData
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                1.dp,
                shape = RoundedCornerShape(12.dp),
                color = Color.Black
            )
            .padding(horizontal = 8.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = alarmData.title, style = MaterialTheme.typography.titleSmall)
            Text(text = alarmData.dateTimeString, style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(4.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            when (alarmData.alarmMode) {
                AlarmMode.ALARM_MANAGER -> {
                    Text(
                        text = "isExact : ${alarmData.isExact}",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )

                    Text(
                        text = "allowWhileIdle : ${alarmData.allowWhileIdle}",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }

                AlarmMode.WORK_MANAGER -> {

                }
            }

            Text(
                text = "delay: ${alarmData.triggerDelay?.let {
                    (it / (60 * 1000))
                }} min (${alarmData.triggerDelay})",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                modifier = Modifier.padding(vertical = 2.dp)
            )

            Text(
                text = "mode: ${alarmData.alarmMode}", style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                modifier = Modifier.padding(vertical = 2.dp)
            )

            alarmData.scheduleMetaData?.let { scheduleMetaData ->
                Divider()
                Text(
                    text = "Schedule Meta Data", style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "isInDozeMode: ${scheduleMetaData.isInDozeMode}",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1
                    )

                    Text(
                        text = "StandByBucket: ${scheduleMetaData.appStandbyBucket}",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            alarmData.executionMetaData?.let { executionMetaData ->
                Divider()

                Text(
                    text = "Execution Meta Data", style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(8.dp))


                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "isInDozeMode: ${executionMetaData.isInDozeMode}",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1
                    )

                    Text(
                        text = "StandByBucket: ${executionMetaData.appStandbyBucket}",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

        }

        Spacer(modifier = Modifier.height(2.dp))
        if (alarmData.isTriggered) {
            SuggestionChip(
                onClick = { },
                label = {
                    Text(text = "Triggered")
                },
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = Color.Green.copy(
                        alpha = 0.6f
                    ),
                    labelColor = Color.Black,
                )
            )
        } else {
            SuggestionChip(
                onClick = { },
                label = {
                    Text(text = "Pending")
                },
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = Color.Red.copy(
                        alpha = 0.6f
                    ),
                    labelColor = Color.Black,
                )
            )
        }
    }

}