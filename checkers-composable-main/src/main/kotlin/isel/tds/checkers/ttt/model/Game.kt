package isel.tds.checkers.ttt.model

data class Game (val board: Board) {

    companion object {
        operator fun invoke() : Game {
            return Game(Board())
        }
    }

    fun play(from: Square, to: Square): Game {
        checkNotNull(board) { "No board" }
        val newBoard = board.play(from, to)

        return this.copy(board=newBoard)
    }

    fun getPlays(from: Square): List<Square>? {
        checkNotNull(board) { "No board" }
        return board.getPlays(from)
    }
}