package pt.isel.pdm.gomokuroyale.util.deserializers

import com.google.gson.*
import pt.isel.pdm.gomokuroyale.game.play.domain.board.*
import pt.isel.pdm.gomokuroyale.game.play.domain.board.Cell
import pt.isel.pdm.gomokuroyale.game.play.domain.board.Piece
import java.lang.reflect.Type

class BoardDeserializer : JsonDeserializer<Board> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Board {
        val jsonObject = json.asJsonObject

        val turn: String? = jsonObject.get("turn")?.asString
        val winner: String? = jsonObject.get("winner")?.asString
        val movesElement = jsonObject.get("moves").asJsonObject
        val moves = mutableMapOf<Cell, Piece>()
        for ((cell, player) in movesElement.entrySet()) {
            val (rowPart, colPart) = if (cell.length == 2) Pair(cell[0].toString(), cell[1].toString())
            else Pair(cell.take(2), cell.drop(2))
            val row = rowPart.toInt() - 1
            val col = colPart[0].code - 'A'.code
            val piece = Piece.valueOf(player.asString)
            moves[Cell(row, col)] = piece
        }

        return if (turn != null)
            if (moves.isEmpty())
                BoardOpen(moves, Piece.valueOf(turn))
            else
                BoardRun(moves, Piece.valueOf(turn))
        else if (winner != null)
            BoardWin(moves, Piece.valueOf(winner))
        else
            BoardDraw(moves)
    }
}