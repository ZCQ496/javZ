package com.TetrisShape;

import com.TetrisApp.Tetris;
import com.TetrisBlock.Cell;
import com.TetrisBlock.Tetromino;

/**
 * @author: zcq
 * @date: 2023/4/15 15:27
 * @ClassName: I
 */
public class J extends Tetromino {
    public J() {
        cells[0] = new Cell(0, 4, Tetris.J);
        cells[1] = new Cell(0, 3, Tetris.J);
        cells[2] = new Cell(0, 5, Tetris.J);
        cells[3] = new Cell(1, 5, Tetris.J);

        //两种旋转状态
        states = new State[4];
        //初始化两种状态的相对坐标
        states[0] = new State(0,0,0,-1,0,1,1,1);
        states[1] = new State(0,0,-1,0,1,0,1,-1);
        states[2] = new State(0,0,0,1,0,-1,-1,-1);
        states[3] = new State(0,0,1,0,-1,0,-1,1);
    }
}
