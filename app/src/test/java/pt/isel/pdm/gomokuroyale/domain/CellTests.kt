package pt.isel.pdm.gomokuroyale.domain

import org.junit.*
import org.junit.Test
import pt.isel.pdm.gomokuroyale.game.play.domain.board.Cell
import pt.isel.pdm.gomokuroyale.game.play.domain.board.Column
import pt.isel.pdm.gomokuroyale.game.play.domain.board.Row

class CellTests {

    @Test
    fun testCell() {
        val cell = Cell(0, 0)
        Assert.assertEquals(Row(0), cell.row)
        Assert.assertEquals(Column('A'), cell.col)
    }

    @Test
    fun testCellEquals() {
        val cell1 = Cell(1, 1)
        val cell2 = Cell(1, 1)
        Assert.assertEquals(cell1, cell2)
    }
}