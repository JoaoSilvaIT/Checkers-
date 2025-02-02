package isel.tds.checkers.ttt.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import isel.tds.checkers.ttt.model.*

val CELL_SIZE = 50.dp
val GRID_WIDTH = CELL_SIZE * BOARD_SIZE
val BORDER_SIZE = 20

@Composable
fun GameLayout(
    board: Board?,
    playerTeam: TeamType?,
    selectedSquare: Square?,
    showTargets: Boolean,
    possibleMoves: List<Square>?,
    onClickCell: (Square, TeamType?) -> Unit
) {
    Row {
        Column(verticalArrangement = Arrangement.SpaceBetween) {
            repeat(BOARD_SIZE + 1) { row ->
                val first = row == 0
                Box(
                    modifier = Modifier
                        .height((if (first) BORDER_SIZE.dp else CELL_SIZE))
                        .width(BORDER_SIZE.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (!first) {
                        val text = when (playerTeam) {
                            TeamType.WHITE, null -> (BOARD_SIZE + 1 - row)
                            TeamType.BLACK -> row
                        }.toString()

                        Text(
                            text,
                            style = MaterialTheme.typography.caption,
                            color = Color.White
                        )
                    }
                }
            }
        }
        Column {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                repeat(BOARD_SIZE) { col ->
                    Box(
                        modifier = Modifier
                            .width(CELL_SIZE)
                            .height(BORDER_SIZE.dp),
                        //.background(if (col % 2 == 1) Color.Blue else Color.Yellow),
                        contentAlignment = Alignment.Center
                    ) {
                        val text = when (playerTeam) {
                            TeamType.WHITE, null -> ('a' + col)
                            TeamType.BLACK -> ('h' - col)
                        }.toString()

                        Text(
                            text,
                            lineHeight = BORDER_SIZE.sp,
                            style = MaterialTheme.typography.caption,
                            color = Color.White
                        )
                    }
                }
            }
            Row {
                GridView(board, playerTeam, selectedSquare, showTargets, possibleMoves, onClickCell)
            }
        }
        Box(
            Modifier
                .width(BORDER_SIZE.dp)
                .height(BORDER_SIZE.dp + GRID_WIDTH)
                .fillMaxSize()
        )
    }
}