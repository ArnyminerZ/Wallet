package com.arnyminerz.wallet.ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Unarchive
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arnyminerz.wallet.R
import com.arnyminerz.wallet.pkpass.data.Pass
import com.arnyminerz.wallet.ui.elements.PassViewer

private const val OPERATION_NONE = 0
private const val OPERATION_ARCHIVE = 1
private const val OPERATION_DELETE = 2

@Composable
@ExperimentalMaterial3Api
fun PassesViewer(
    passes: List<Pass>,
    filterArchived: Boolean,
    paddingValues: PaddingValues,
    onArchive: (Pass) -> Unit,
    onDelete: (Pass) -> Unit,
) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        val items = if (filterArchived)
            passes.filter { it.archived }
        else
            passes.filterNot { it.archived }

        items(items) { pass ->
            val animVisibleState = remember { MutableTransitionState(true) }
            var operation by remember { mutableStateOf(OPERATION_NONE) }

            if (!animVisibleState.targetState && !animVisibleState.currentState)
                when (operation) {
                    OPERATION_ARCHIVE -> onArchive(pass)
                    OPERATION_DELETE -> onDelete(pass)
                }

            var isDeleting by remember { mutableStateOf(false) }
            if (isDeleting)
                AlertDialog(
                    onDismissRequest = { isDeleting = false },
                    title = { Text(stringResource(R.string.delete_dialog_title)) },
                    text = { Text(stringResource(R.string.delete_dialog_message)) },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                operation = OPERATION_DELETE
                                animVisibleState.targetState = false
                            },
                        ) { Text(stringResource(R.string.action_delete)) }
                    },
                    dismissButton = {
                        TextButton(onClick = { isDeleting = false }) {
                            Text(stringResource(R.string.action_cancel))
                        }
                    },
                )


            AnimatedVisibility(
                visibleState = animVisibleState,
                enter = fadeIn(tween(durationMillis = 200)),
                exit = slideOutHorizontally(tween(200)) { if (filterArchived) -it else it }
            ) {
                PassViewer(
                    pass = pass,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    actions = {
                        IconButton(
                            onClick = { isDeleting = true },
                        ) {
                            Icon(
                                Icons.Outlined.Delete,
                                stringResource(R.string.action_delete),
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                        IconButton(
                            onClick = {
                                operation = OPERATION_ARCHIVE
                                animVisibleState.targetState = false
                            },
                        ) {
                            if (pass.archived)
                                Icon(
                                    Icons.Outlined.Unarchive,
                                    stringResource(R.string.action_unarchive),
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            else
                                Icon(
                                    Icons.Outlined.Archive,
                                    stringResource(R.string.action_archive),
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                        }
                    },
                )
            }
        }
        item {
            AnimatedVisibility(
                items.isEmpty(),
                enter = slideInVertically { -it },
                exit = slideOutVertically { -it },
            ) {
                Text(
                    stringResource(R.string.passes_empty),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}
