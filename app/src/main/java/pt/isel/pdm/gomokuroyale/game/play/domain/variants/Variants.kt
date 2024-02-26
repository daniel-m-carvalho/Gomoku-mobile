package pt.isel.pdm.gomokuroyale.game.play.domain.variants

import pt.isel.pdm.gomokuroyale.game.play.domain.board.BoardDim

enum class Variants(
    val boardDim: BoardDim,
    val openingRule: OpeningRule,
    val playingRule: PlayingRule,
    val tip: String = ""
) : VariantLogic {

    STANDARD(
        boardDim = BoardDim.STANDARD,
        openingRule = OpeningRule.STANDARD,
        playingRule = PlayingRule.STANDARD,
        tip = "Standard variant is a variant where the first player can place a stone anywhere on the board."
    ),
    SWAP(
        boardDim = BoardDim.STANDARD,
        openingRule = OpeningRule.SWAP,
        playingRule = PlayingRule.STANDARD,
        tip = "Swap variant is a variant where the second player can choose to swap the first player's first move with his own first move."
    ),
    RENJU(
        boardDim = BoardDim.STANDARD,
        openingRule = OpeningRule.STANDARD,
        playingRule = PlayingRule.THREE_AND_THREE,
        tip = "Renju variant is a variant where the first player cannot place a stone in a position that would create a three-in-a-row."
    ),
    CARO(
        boardDim = BoardDim.STANDARD,
        openingRule = OpeningRule.STANDARD,
        playingRule = PlayingRule.STANDARD,
        tip = "Caro variant is a variant where the first player cannot place a stone in a position that would create a three-in-a-row."
    ),
    PENTE(
        boardDim = BoardDim.MODIFIED,
        openingRule = OpeningRule.STANDARD,
        playingRule = PlayingRule.STANDARD,
        tip = "Pente variant is a variant where the first player cannot place a stone in a position that would create a three-in-a-row."
    ),
    OMOK(
        boardDim = BoardDim.MODIFIED,
        openingRule = OpeningRule.STANDARD,
        playingRule = PlayingRule.THREE_AND_THREE,
        tip = "Omok variant is a variant where the first player cannot place a stone in a position that would create a three-in-a-row."
    ),
    NINUKI_RENJU(
        boardDim = BoardDim.STANDARD,
        openingRule = OpeningRule.STANDARD,
        playingRule = PlayingRule.THREE_AND_THREE,
        tip = "Ninuki Renju variant is a variant where the first player cannot place a stone in a position that would create a three-in-a-row."
    );
}
