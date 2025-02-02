package isel.tds.checkers

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import isel.tds.checkers.ttt.model.Game
import isel.tds.checkers.ttt.view.*
import isel.tds.checkers.ttt.viewmodel.AppViewModel
import isel.tds.galo.storage.MongoDriver

@Composable
@Preview
fun FrameWindowScope.GridApp(driver: MongoDriver, onExit: () -> Unit) {

    val scope = rememberCoroutineScope()
    val vm: AppViewModel = remember { AppViewModel(driver, scope) }

    MaterialTheme {
        MenuBar {
            Menu("Game") {
                Item("Start/Join game", onClick = vm::openStartOrJoinDialog)
                Item("Refresh", enabled = vm.hasClash, onClick = vm::refresh)
                Item("Exit", onClick = onExit)
            }

            Menu("Options") {
                CheckboxItem("Show targets", checked = vm.showTargets, onCheckedChange = vm::enableShowTargets)
                CheckboxItem("Auto-refresh", checked = vm.autoRefresh, onCheckedChange = vm::enableAutoRefresh)
            }
        }
        Box(
            //color = Color.DarkGray
            modifier = Modifier
                .background(Color(40, 40, 40))
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(verticalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxHeight()) {
                GameLayout(
                    vm.board,
                    vm.sidePlayer,
                    vm.selectedSquare,
                    vm.showTargets,
                    vm.possibleMoves,
                    onClickCell = vm::tryPlay
                )
                StatusBar(vm.board, vm.sidePlayer, vm.name)
            }

            if (vm.showStartJoinDialog) {
                StartOrJoinDialog(
                    onCancel = vm::closeStartOrJoinDialog,
                    onAction = vm::joinOrStart
                )
            }

            vm.errorMessage?.let { ErrorDialog(it, onClose = vm::hideError) }
        }
    }
}

fun main() = MongoDriver("checkers").use { driver ->
    application {
        Window(
            onCloseRequest = ::exitApplication,
            state = WindowState(
                size = DpSize(
                    GRID_WIDTH + BORDER_SIZE.dp * 2 + 16.dp,
                    GRID_WIDTH + BORDER_SIZE.dp * 2 + 70.dp
                )
            ),
            title = "Checkers"
        ) {
            GridApp(driver, ::exitApplication)
        }
    }
}