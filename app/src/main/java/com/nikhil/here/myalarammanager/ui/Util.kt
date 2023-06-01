package com.nikhil.here.myalarammanager.ui

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

class MultiplePermissionRequest(
    private val context: Context,
    val permissions: List<String>,
    val launchPermissionRequest: () -> Unit,
) {
    val isGranted: Boolean
        get() = permissions.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
}

@Composable
fun rememberMultiplePermissionRequest(permissions: List<String>, onPermissionRequestUpdate: (Boolean) -> Unit): MultiplePermissionRequest {
    val context = LocalContext.current

    val rememberPermissionUpdate = rememberUpdatedState(newValue = onPermissionRequestUpdate)

    val activityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { isGranted ->
        if (isGranted.any { !it.value }) {
            rememberPermissionUpdate.value.invoke(false)
        } else {
            rememberPermissionUpdate.value.invoke(true)
        }
    }

    val multiplePermissionsState = remember(context) {
        MultiplePermissionRequest(context, permissions) { activityResultLauncher.launch(permissions.toTypedArray()) }
    }

    return multiplePermissionsState
}