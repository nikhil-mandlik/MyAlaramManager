package com.nikhil.here.myalarammanager.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
            .padding(8.dp)
    ) {
        Text(text = alarmData.title, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = alarmData.dateTimeString, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "isExact : ${alarmData.isExact}, allowWhileIdle : ${alarmData.allowWhileIdle}",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = "delay : ${alarmData.triggerDelay}", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(8.dp))
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