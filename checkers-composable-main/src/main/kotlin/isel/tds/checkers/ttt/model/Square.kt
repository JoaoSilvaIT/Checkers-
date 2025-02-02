package isel.tds.checkers.ttt.model

data class Square private constructor(val row: Row, val column: Column) {
    val index: Int
        get() = row.index * BOARD_DIM + column.index

    val black: Boolean
        get() = (row.index + column.index) % 2 == 1

    override fun toString(): String {
        return "${row.digit}${column.symbol}"
    }

    operator fun plus(value: Direction): Square? {
        val row = (this.row.index + value.first).toRowOrNull() ?: return null
        val col = (this.column.index + value.second).toColumnOrNull() ?: return null
        return Square(row, col)
    }

    companion object {
        val values: List<Square> = Row.values.flatMap { row ->
            Column.values.map { col -> Square(row, col) }
        }

        operator fun invoke(row: Row, column: Column): Square {
            return values.first { it.row == row && it.column == column }
        }
    }
}

fun String.toSquareOrNull(): Square? {
    if (length != 2) return null
    val row = this[0].toRowOrNull() ?: return null
    val col = this[1].toColumnOrNull() ?: return null
    return Square(row, col)
}

fun String.toSquare(): Square {
    return toSquareOrNull() ?: throw IllegalArgumentException("Invalid square string: $this")
}