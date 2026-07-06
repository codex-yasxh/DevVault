package com.aditya.devvault.presentation.projects

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aditya.devvault.domain.model.Project

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProjectSheet(
    projectToEdit: Project?,
    onDismiss: () -> Unit,
    onSave: (Project) -> Unit
) {
    var formState by remember {
        mutableStateOf(
            projectToEdit?.toFormState() ?: ProjectFormState()
        )
    }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = formState.name,
                onValueChange = { newName ->
                    formState = formState.copy(name = newName)
                },
                label = { Text("Project Name") }
            )

            Button(onClick = {
                onSave(formState.toProject(originalStartedAt = projectToEdit?.startedAt))
            }) {
                Text("Save")
            }
        }
    }
}