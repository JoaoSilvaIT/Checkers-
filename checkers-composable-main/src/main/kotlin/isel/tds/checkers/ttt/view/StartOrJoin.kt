package isel.tds.checkers.ttt.view

import androidx.compose.material.*
import androidx.compose.runtime.*
import isel.tds.checkers.ttt.model.Name


@Composable
fun StartOrJoinDialog(
    onCancel: () -> Unit,
    onAction: (Name) -> Unit
) {
    var name by remember { mutableStateOf("") }  // Name in edition
    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Text(
                text = "Game to Join/Start",
                style = MaterialTheme.typography.h5
            )
        },
        text = {
            OutlinedTextField(value = name,
                onValueChange = { name = it },
                label = { Text("Name of game") }
            )
        },
        confirmButton = {
            TextButton(enabled = Name.isValid(name),
                onClick = { onAction(Name(name)) }
            ) { Text("Join/Start") }
        },
        dismissButton = {
            TextButton(onClick = onCancel) { Text("cancel") }
        }
    )
}