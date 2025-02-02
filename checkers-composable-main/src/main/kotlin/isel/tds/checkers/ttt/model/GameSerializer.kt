package isel.tds.checkers.ttt.model

import isel.tds.checkers.storage.Serializer

object GameSerializer: Serializer<Game> {
    override fun serialize(data: Game) =
        BoardSerializer.serialize(data.board)

    override fun deserialize(text: String): Game {
        return Game(BoardSerializer.deserialize(text))
    }
}

object BoardSerializer: Serializer<Board> {
    override fun serialize(data: Board): String =
        when (data) {
            is Board.BoardRun -> "RUN ${data.turn}"
            is Board.BoardWin -> "WIN ${data.winner}"
            is Board.BoardDraw -> "DRAW "
        } + "\n" +
        TeamSerializer.serialize(data.white) + "\n" +
        TeamSerializer.serialize(data.black)


    override fun deserialize(text: String): Board {
        val (board, white, black) = text.split("\n")
        val teamWhite = TeamSerializer.deserialize(white)
        val teamBlack = TeamSerializer.deserialize(black)
        val (type, team) = board.split(" ")
        return when (type) {
            "RUN" -> Board.BoardRun(
                teamWhite,
                teamBlack,
                team.single().toTeamType()
            )
            "WIN" -> Board.BoardWin(
                teamWhite,
                teamBlack,
                team.single().toTeamType()
            )
            "DRAW" -> Board.BoardDraw(
                teamWhite,
                teamBlack
            )
            else -> error("Invalid board type $type")
        }

    }
}

object TeamSerializer: Serializer<Team> {
    override fun serialize(data: Team): String {
        return data.team.toString() + " | " + data.pieces.entries.joinToString(" ") { (pos,player) -> "$pos:$player" }
    }

    override fun deserialize(text: String): Team {
        val (left, right) = text.split(" | ")
        return Team(
            left.single().toTeamType(),
            if (right.isEmpty()) emptyMap() else
                right.split(" ").map {
                    it.split(":")
                }.associate { (pos, type) ->
                    pos.toSquare() to type.single().toPiece()
                }
        )
    }
}