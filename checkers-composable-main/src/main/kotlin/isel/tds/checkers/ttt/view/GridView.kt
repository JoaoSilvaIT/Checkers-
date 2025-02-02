package isel.tds.checkers.ttt.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import isel.tds.checkers.ttt.model.*


@Composable
fun GridView(
    board: Board?,
    playerTeam: TeamType?,
    selectedSquare: Square?,
    showTargets: Boolean,
    possibleMoves: List<Square>?,
    onClickCell: (Square, TeamType?) -> Unit
) {
    Column(
        modifier = Modifier.size(GRID_WIDTH).background(Color.Black),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(BOARD_SIZE) { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(BOARD_SIZE) { col ->
                    val pos = when (playerTeam) {
                        TeamType.WHITE, null -> Square(Row(row), Column(col))
                        TeamType.BLACK -> Square(Row(BOARD_SIZE - row - 1), Column(BOARD_SIZE - col - 1))
                    }
                    val piece = board?.pieces?.get(pos)
                    var team: TeamType? = null
                    if (board != null) {
                        team = if (pos in board.white.pieces) TeamType.WHITE else TeamType.BLACK
                    }

                    var modifier = Modifier.size(CELL_SIZE)
                        .background(if (pos.black) Color.DarkGray else Color.LightGray)

                    if (showTargets && possibleMoves != null && pos in possibleMoves)
                        modifier = modifier
                            .padding((8).dp)
                            .background(Color.Green.copy(alpha = 0.3F), CircleShape)
                    else if (selectedSquare != null && pos == selectedSquare) {
                        modifier = modifier.background(Color.Red, CircleShape)
                    }
                    PieceView(
                        100.dp,
                        piece,
                        team,
                        onClick = { onClickCell(pos, team) },
                        modifier
                    )
                }
            }
        }
    }

}