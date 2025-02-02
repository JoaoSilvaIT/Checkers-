package isel.tds.checkers.ttt.model

@JvmInline
value class Column(val index: Int) {
    val symbol: Char
        get() = 'a' + index

    init {
        require(index in 0 until BOARD_DIM) { "Invalid column index: $index" }
    }

    companion object {
        val values: List<Column> = List(BOARD_DIM) { Column(it) }
    }
}

fun Char.toColumnOrNull(): Column? {
    val index = this - 'a'
    return if (index in 0 until BOARD_DIM) Column(index) else null
}

fun Int.toColumnOrNull(): Column? {
    return if (this in 0 until BOARD_DIM) Column(this) else null
}