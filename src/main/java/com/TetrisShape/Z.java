package com.TetrisShape;

import com.TetrisApp.Tetris;
import com.TetrisBlock.Cell;
import com.TetrisBlock.Tetromino;

/**
 * @author: zcq
 * @date: 2023/4/15 15:27
 * @ClassName: I
 */
public class Z extends Tetromino {
    public Z() {
        cells[0] = new Cell(1,4, Tetris.Z);
        cells[1] = new Cell(0,3, Tetris.Z);
        cells[2] = new Cell(0,4, Tetris.Z);
        cells[3] = new Cell(1,5, Tetris.Z);

        states = new State[2];
        states[0] = new State(0,0,-1,-1,-1,0,0,1);
        states[0] = new State(0,0,-1,1,0,1,1,0);
    }
}
