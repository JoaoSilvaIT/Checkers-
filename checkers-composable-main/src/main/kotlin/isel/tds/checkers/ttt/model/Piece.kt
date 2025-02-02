package isel.tds.checkers.ttt.model

enum class Piece(val char: Char) {
    QUEEN('Q'),
    PAWN('p');

    override fun toString(): String {
        return char.toString()
    }

    companion object {
        private val map = Piece.entries.associateBy { it.char }
        operator fun get(value: Char) = map[value]
    }
}

fun Char.toPieceOrNull(): Piece? {
    return Piece[this]
}

fun Char.toPiece(): Piece {
    return toPieceOrNull() ?: throw IllegalArgumentException("Invalid piece string: $this")
}