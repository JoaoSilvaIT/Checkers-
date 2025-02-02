package isel.tds.checkers.ttt.ui

import isel.tds.checkers.ttt.model.*

fun Clash.show() {
    val clash = this as? ClashRun ?: return
    println("Clash id: ${clash.id}")
    clash.game.show(player)
}

fun Game.show(player: TeamType) = board.show(player)

val sepLine = "  +" + "-".repeat(BOARD_DIM*2 + 1) + "+"

fun Board.show(player: TeamType) {
    println(sepLine)
    for (row in Row.values) {
        print("${row.digit} | ")
        for (col in Column.values) {
            val square = Square(row, col)
            var piece = white.pieces[square]
            if (piece != null) {
                if (piece == Piece.QUEEN)
                    print("W")
                else
                    print("w")
            } else {
                piece = black.pieces[square]
                if (piece != null) {
                    if (piece == Piece.QUEEN)
                        print("B")
                    else
                        print("b")
                } else {
                    if (square.black)
                        print("-")
                    else
                        print(" ")
                }
            }
            print(" ")
        }

        if (row.digit == '7') {
            when (this) {
                is Board.BoardWin -> print("|  Winner = $winner")
                is Board.BoardRun -> print("|  Turn = $turn")
                is Board.BoardDraw -> print("|  Tie")
            }
        } else if (row.digit == '8') {
            print("|  Player = $player")
        } else {
            print("|")
        }
        println()
    }

    println(sepLine)

    print("    ") // Initial spacing to align with columns
    Column.values.forEach { col -> print("${col.symbol} ") }
    println()
}