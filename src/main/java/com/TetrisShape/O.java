package com.TetrisShape;

import com.TetrisApp.Tetris;
import com.TetrisBlock.Cell;
import com.TetrisBlock.Tetromino;

/**
 * @author: zcq
 * @date: 2023/4/15 15:27
 * @ClassName: I
 */
public class O extends Tetromino {
    public O() {
        cells[0] = new Cell(0, 4, Tetris.O);
        cells[1] = new Cell(0, 5, Tetris.O);
        cells[2] = new Cell(1, 4, Tetris.O);
        cells[3] = new Cell(1, 5, Tetris.O);

        //无旋转
        states = new State[0];
    }
}
