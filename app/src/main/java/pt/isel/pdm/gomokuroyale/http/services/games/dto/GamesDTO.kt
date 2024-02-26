package pt.isel.pdm.gomokuroyale.http.services.games.dto

import pt.isel.pdm.gomokuroyale.authentication.domain.User
import pt.isel.pdm.gomokuroyale.game.play.domain.board.Board
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variants


// region Input Models
data class GamePlayInputModel(val row: Int, val column: Int)

data class GameMatchmakingInputModel(val variant: String)

// endregion

// region Output Models
data class GameRoundOutputModel(val game: GameOutputModel)

data class GameMatchmakingOutputModel(val message: String, val idType: String, val id: Int)

data class GameOutputModel(
    val id: Int,
    val board: Board,
    val userBlack: User,
    val userWhite: User,
    val state: String,
    val variant: Variant,
    val created: String
)

data class GameGetByIdOutputModel(val game: GameOutputModel, val pollingTimeOut: Int)

data class GameMatchmakingStatusOutputModel(
    val mid: Int,
    val uid: Int,
    val gid: Int?,
    val state: String,
    val variant: String,
    val created: String,
    val pollingTimeOut: Int
)

data class GameGetAllByUserOutputModel (
    val uid : Int,
    val page : Int,
    val pageSize : Int,
)

data class SurrenderGameOutputModel(val message: String)

data class CancelMatchmakingOutputModel(val message: String)

data class GetVariantsOutputModel(val variants: List<Variant>)

// endregion