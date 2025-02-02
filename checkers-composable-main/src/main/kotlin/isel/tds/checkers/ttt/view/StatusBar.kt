package isel.tds.checkers.ttt.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import isel.tds.checkers.ttt.model.*

@Composable
fun StatusBar(board: Board?, you: TeamType?, name: Name?) {
    Row(
        modifier = Modifier
            .width(GRID_WIDTH + BORDER_SIZE.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        name?.let {
            Text("Game:${name}", style = MaterialTheme.typography.h6, color = Color.White)
        }
        you?.let {
            Text("You: ${you.name}", style = MaterialTheme.typography.h6, color = Color.White)
        }
        val text = when (board) {
            null -> "Start a new game"
            is Board.BoardRun -> if (you == board.turn.team) "Your turn" else "Waiting..."
            is Board.BoardWin -> "Winner: ${board.winner}"
            is Board.BoardDraw -> "Draw"
        }
        Text(text, style = MaterialTheme.typography.h6, color = Color.White)
    }
}