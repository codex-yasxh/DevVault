package com.aditya.devvault.presentation.projects

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aditya.devvault.domain.model.Platform
import com.aditya.devvault.domain.model.Project
import com.aditya.devvault.domain.model.ProjectStatus
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close


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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = formState.name,
                onValueChange = { newName ->
                    formState = formState.copy(name = newName)
                },
                label = { Text("Project Name") }
            )
            OutlinedTextField(
                value = formState.description,
                onValueChange = { newDescription ->
                    formState = formState.copy(description = newDescription)
                },
                label = { Text("Project Description") }
            )

            Text("Status")

            SingleChoiceSegmentedButtonRow {
                ProjectStatus.entries.forEachIndexed { index, status ->

                    SegmentedButton(
                        selected = formState.status == status,
                        onClick = {
                            formState = formState.copy(status = status)
                        },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = ProjectStatus.entries.size
                        )
                    ) {
                        Text(status.name)
                    }
                }
            }

            Text("Platforms")

            MultiChoiceSegmentedButtonRow {
                Platform.entries.forEachIndexed { index, platform ->

                    SegmentedButton(
                        checked = platform in formState.platforms,
                        onCheckedChange = { checked ->

                            val updatedPlatforms =
                                if (checked) {
                                    formState.platforms + platform
                                } else {
                                    formState.platforms - platform
                                }

                            formState = formState.copy(
                                platforms = updatedPlatforms
                            )
                        },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = Platform.entries.size
                        )
                    ) {
                        Text(platform.name)
                    }
                }
            }

            var techInput by remember {
                mutableStateOf("")
            }

            OutlinedTextField(
                value = techInput,
                onValueChange = {
                    techInput = it
                },
                label = {
                    Text("Tech Stack")
                }
            )

            Button(
                onClick = {

                    if (techInput.isNotBlank()) {

                        formState = formState.copy(
                            techStack = formState.techStack + techInput.trim()
                        )

                        techInput = ""
                    }

                }
            ) {
                Text("Add")
            }

            FlowRow {

                formState.techStack.forEach { tech ->

                    AssistChip(
                        onClick = {},
                        label = {
                            Text(tech)
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {

                                    formState = formState.copy(
                                        techStack = formState.techStack - tech
                                    )

                                }
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Remove"
                                )
                            }
                        }
                    )

                }
            }


            OutlinedTextField(
                value = formState.decisionNote,
                onValueChange = { newDecisionNote ->
                    formState = formState.copy(decisionNote = newDecisionNote)
                },
                label = { Text("Decision Note") }
            )
            OutlinedTextField(
                value = formState.githubUrl,
                onValueChange = { newGithubUrl ->
                    formState = formState.copy(githubUrl = newGithubUrl)
                },
                label = { Text("GitHub URL") }
            )
            OutlinedTextField(
                value = formState.liveUrl,
                onValueChange = { newLiveUrl ->
                    formState = formState.copy(liveUrl = newLiveUrl)
                },
                label = { Text("Live URL") }
            )


            Button(onClick = {
                onSave(formState.toProject(originalStartedAt = projectToEdit?.startedAt))
            }) {
                Text("Save")
            }


        }
    }
}