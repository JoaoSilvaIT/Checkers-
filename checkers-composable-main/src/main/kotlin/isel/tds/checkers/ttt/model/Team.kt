package isel.tds.checkers.ttt.model

typealias Positions = Map<Square, Piece>

enum class TeamType(private val char: Char) {
    BLACK('b'),
    WHITE('w');

    override fun toString(): String {
        return char.toString()
    }

    companion object {
        private val map = TeamType.entries.associateBy { it.char }
        operator fun get(value: Char) = map[value]
    }
}

fun Char.toTeamTypeOrNull(): TeamType? {
    return TeamType[this]
}

fun Char.toTeamType(): TeamType {
    return toTeamTypeOrNull() ?: throw IllegalArgumentException("Invalid piece string: $this")
}

class Team(val team: TeamType, val pieces: Positions) {

    override fun toString() = team.toString()

    companion object {
        private val values = mutableMapOf<TeamType, Team>()

        operator fun invoke(teamType: TeamType): Team {
            return values.getOrElse(teamType) {
                println("Created new TEAM for $teamType")
                val pieces = mutableMapOf<Square, Piece>()

                Square.values.forEach {
                    if (it.black && ((teamType == TeamType.BLACK && it.row.index in 0..2) || teamType == TeamType.WHITE && it.row.index in BOARD_DIM - 3 .. BOARD_DIM))
                        pieces[it] = Piece.PAWN
                }

                return Team(teamType, pieces) .also { values[teamType] = it }
            }
        }
    }
}