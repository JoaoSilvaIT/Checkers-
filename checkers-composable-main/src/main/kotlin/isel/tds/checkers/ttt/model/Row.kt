package isel.tds.checkers.ttt.model

@JvmInline
value class Row(val index: Int) {
    val digit: Char
        get() = '1' + (BOARD_DIM - 1 - index)

    init {
        require(index in 0 until BOARD_DIM) { "Invalid row index: $index" }
    }

    companion object {
        val values: List<Row> = List(BOARD_DIM) { Row(it) }
    }
}

fun Char.toRowOrNull(): Row? {
    val index = BOARD_DIM - (this - '1') - 1
    return if (index in 0 until BOARD_DIM) Row(index) else null
}

fun Int.toRowOrNull(): Row? {
    return if (this in 0 until BOARD_DIM) Row(this) else null
}