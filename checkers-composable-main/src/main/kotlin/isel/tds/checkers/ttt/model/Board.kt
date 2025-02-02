package isel.tds.checkers.ttt.model

import kotlin.math.abs
import kotlin.math.max
import isel.tds.checkers.AppProperties

val BOARD_SIZE = AppProperties.p.getProperty("BOARD_SIZE")?.toInt() ?: 8
val BOARD_CELLS = BOARD_SIZE * BOARD_SIZE

typealias Moves = List<Pair<Square, Square>>
typealias Direction = Pair<Int, Int>
typealias Directions =  List<Direction>

operator fun Direction.times(mult: Int): Direction = Direction(this.first * mult, this.second * mult)

sealed class Board private constructor(val white: Team, val black: Team) {
    val pieces
        get() = white.pieces + black.pieces

    class BoardRun(white: Team, black: Team, turn: TeamType, val forceCaptureFrom: Square? = null): Board(white, black) {
        val turn: Team = if (turn == white.team) white else black
        val nextTurn = if (turn == white.team) black else white
    }

    class BoardWin(white: Team, black: Team, winner: TeamType): Board(white, black) {
        val winner: Team = if (winner == white.team) white else black
    }

    class BoardDraw(white: Team, black: Team): Board(white, black)

    companion object {
        operator fun invoke(): Board {
            return BoardRun(
                white = Team(TeamType.WHITE),
                black = Team(TeamType.BLACK),
                turn = TeamType.WHITE
            )
        }
    }

    fun play(from: Square, to: Square): Board {
        check(this is BoardRun) { "Game is over" }
        require(to !in pieces) { "Destination square is not empty" }
        require(forceCaptureFrom == null || forceCaptureFrom == from) { "You must continue from the last capture"}
        require(from in turn.pieces) { "Start square does not have a piece" }

        return move(from, to)
    }

    fun getPlays(from: Square): List<Square>? {
        check(this is BoardRun) { "Game is over" }

        if (forceCaptureFrom != null && forceCaptureFrom != from) return null
        if (from !in turn.pieces) return null

        return getMoves(from)
    }

    private fun BoardRun.getMoves(from: Square): List<Square> {
        val captures: Moves = getCaptures()

        if (captures.isNotEmpty()) {
            return captures
                .mapNotNull { pair -> if (pair.first == from) pair.second else null }
        } else {
            val moves = from.getPossibleMoves(turn.pieces.getValue(from), turn.team)
            return getMoves(moves, turn.pieces.getValue(from))
                .mapNotNull { pair -> if (pair.first == from) pair.second else null}
        }
    }

    private fun BoardRun.move(from: Square, to: Square): Board {
        val captures: Moves = getCaptures()
        if (captures.isNotEmpty()) {
            require(Pair(from, to) in captures) { "There is a mandatory capture" }
            val capturedPieces = getCapturedPieces(from, to)
            val newPieces = updatedPieces(from, to, capturedPieces)
            val moves = to.getPossibleMoves(turn.pieces.getValue(from), turn.team)
            return if (getCaptures(moves).isNotEmpty())
                update(newPieces, to)
            else
                update(newPieces)
        } else {
            val moves = from.getPossibleMoves(turn.pieces.getValue(from), turn.team)
            val validMoves = getMoves(moves, turn.pieces.getValue(from))
            if (Pair(from, to) in validMoves) {
                val newPieces = updatedPieces(from, to)
                return update(newPieces)
            } else
                error("Not a valid move")
        }


    }

    protected fun Board.update(newPieces: Pair<Positions, Positions>, forceFrom: Square? = null): Board {
        when (this) {
            is BoardRun -> {
                val turn = if (forceFrom == null) nextTurn else turn
                val (white, black) = newPieces

                val newBoard = BoardRun(Team(TeamType.WHITE, white), Team(TeamType.BLACK, black), turn.team, forceFrom)
                val whiteMoves = newBoard.getMovesOrCaptures(newBoard.white)
                val blackMoves = newBoard.getMovesOrCaptures(newBoard.black)
                return when {
                    white.isEmpty() && black.isNotEmpty() || whiteMoves.isEmpty() -> BoardWin(newBoard.white, newBoard.black, newBoard.white.team)
                    black.isEmpty() && white.isNotEmpty() || blackMoves.isEmpty() -> BoardWin(newBoard.white, newBoard.black, newBoard.black.team)
                    white.isEmpty() && black.isEmpty() -> BoardDraw(newBoard.white, newBoard.black)
                    else -> newBoard
                }

            }
            is BoardWin, is BoardDraw -> error("Game is over")
        }
    }

    private fun BoardRun.updatedPieces(from: Square, to: Square, captures: List<Square>? = null) : Pair<Positions, Positions> {
        var piece = turn.pieces[from]
        require(piece != null) { "Start square has no piece"}
        require(nextTurn.pieces[to] == null) { "Destination square has a piece" }

        if ( turn == white) {
            if (to.row.index == 0)
                piece = Piece.QUEEN


            val newWhite = turn.pieces - from + Pair(to, piece)
            val newBlack = nextTurn.pieces - (captures ?: emptyList())

            return Pair(newWhite, newBlack)
        } else {
            if (to.row.index == 7)
                piece = Piece.QUEEN

            val newBlack = turn.pieces - from + Pair(to, piece)
            val newWhite = nextTurn.pieces - (captures ?: emptyList())

            return Pair(newWhite, newBlack)
        }
    }

    private fun BoardRun.getCaptures(): Moves {
        val captures = mutableListOf<Pair<Square, Square>>()
        turn.pieces.forEach { (square, piece) ->
            val moves = square.getPossibleMoves(piece, turn.team)
            captures += getCaptures(moves)
        }
        return captures
    }

    fun BoardRun.getMovesOrCaptures(team: Team): Moves {
        val movesOrCaptures = mutableListOf<Pair<Square, Square>>()
        team.pieces.forEach { (square, piece) ->
            val moves = square.getPossibleMoves(piece, team.team)
            movesOrCaptures += getMovesOrCaptures(moves, piece)
        }

        return movesOrCaptures
    }

    private fun BoardRun.getMovesOrCaptures(moves: Moves, piece: Piece): Moves {
        return moves.filter { (from, to) ->
            val captures = this.getCapturedPieces(from, to)
            from.dist(to) > 1 && captures.isNotEmpty() ||
            piece == Piece.PAWN && from.dist(to) == 1 ||
            piece == Piece.QUEEN && captures.isEmpty()
        }
    }

    private fun BoardRun.getCaptures(moves: Moves): Moves {
        return moves.filter { (from, to) ->
            from.dist(to) > 1 && this.getCapturedPieces(from, to).isNotEmpty()
        }
    }

    private fun BoardRun.getMoves(moves: Moves, piece: Piece): Moves {
        return moves.filter { (from, to) ->
            piece == Piece.PAWN && from.dist(to) == 1 || piece == Piece.QUEEN && this.getCapturedPieces(from, to).isEmpty()
        }
    }

    private fun BoardRun.getCapturedPieces(from: Square, to: Square): List<Square> {
        val dir = Direction(
            if (to.row.index > from.row.index) 1 else -1,
            if (to.column.index > from.column.index) 1 else -1
        )

        var midSquare: Square? = from
        val list = mutableListOf<Square>()

        while(midSquare != to && midSquare != null) {
            midSquare += dir

            if (midSquare == null)
                break;

            if (midSquare in turn.pieces)
                return emptyList()
            else if (midSquare in pieces - turn.pieces)
                list.add(midSquare)
        }

        return list
    }

    private fun Square.getPossibleMoves(piece: Piece, team: TeamType): Moves {
        val directions = piece.getBaseDirections(team)
        val maxDistance = if (piece == Piece.PAWN) 2 else null
        return directions
            .flatMap { this.getSquaresInDirection(it, maxDistance) }
            .filter { it !in pieces }
            .map { Pair(this, it) }
    }

    private fun Square.getSquaresInDirection(direction: Direction, maxDistance: Int?): List<Square> {
        var positions = mutableListOf<Square>()
        var mult = 1
        var newSquare: Square? = this
        while ((maxDistance == null || mult in 1..maxDistance) && newSquare != null) {
            newSquare = (this + direction * mult)?.also {
                positions.add(it)
            }
            mult += 1
        }

        return positions
    }

    private fun Square.dist(to: Square): Int {
        return max(abs(to.row.index - this.row.index), abs(to.column.index - to.column.index))
    }

    private fun Piece.getBaseDirections(team: TeamType): Directions {
        val side = if (team == TeamType.WHITE) -1 else 1
        return when (this) {
            Piece.PAWN -> listOf(Direction(1 * side, -1), Direction(1 * side, 1))
            Piece.QUEEN -> listOf(Direction(1, 1), Direction(-1, 1), Direction(1, -1), Direction(-1, -1))
        }
    }
}