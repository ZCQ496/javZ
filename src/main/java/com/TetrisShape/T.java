package com.TetrisShape;

import com.TetrisApp.Tetris;
import com.TetrisBlock.Cell;
import com.TetrisBlock.Tetromino;

/**
 * @author: zcq
 * @date: 2023/4/15 15:27
 * @ClassName: I
 */
public class T extends Tetromino {
    public T() {
        cells[0] = new Cell(0,4, Tetris.T);
        cells[1] = new Cell(0,3, Tetris.T);
        cells[2] = new Cell(0,5, Tetris.T);
        cells[3] = new Cell(1,4, Tetris.T);

        states = new State[4];
        states[0] = new State(0,0,0,-1,0,1,1,0);
        states[1] = new State(0,0,-1,0,1,0,0,-1);
        states[2] = new State(0,0,0,1,0,-1,-1,0);
        states[3] = new State(0,0,1,0,-1,0,0,1);
    }
}
