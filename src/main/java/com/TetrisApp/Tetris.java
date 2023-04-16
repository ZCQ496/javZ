package com.TetrisApp;

import com.TetrisBlock.Cell;
import com.TetrisBlock.Tetromino;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author: zcq
 * @date: 2023/4/15 14:45
 * @ClassName: Tetris
 */
public class Tetris extends JPanel {
    //正在下落
    private Tetromino currentOne = Tetromino.randomOne();
    //将要下落
    private Tetromino nextOne = Tetromino.randomOne();
    //游戏主页面
    private Cell[][] wall = new Cell[18][9];
    //单元格值
    private static final int CELL_SIZE = 48;

    //分数池
    int[] scores_pool = {0, 1, 2, 5, 10};
    //当前游戏分数
    private int totalScore = 0;
    //当前消除行数
    private int totalLine = 0;

    //游戏三种状态 游戏中，暂停，结束
    public static final int PLING = 0;
    public static final int STOP = 1;
    public static final int OVER = 2;

    //当前游戏状态
    private int game_state;
    //显示游戏状态
    String[] show_state = {"P[pause]", "C[continue]", "S[replay]"};

    //载入方块图片
    public static BufferedImage I;
    public static BufferedImage J;
    public static BufferedImage L;
    public static BufferedImage O;
    public static BufferedImage S;
    public static BufferedImage T;
    public static BufferedImage Z;
    public static BufferedImage background;


    static {
        try {
            I = ImageIO.read(new File("src/main/resources/images/I.png"));
            J = ImageIO.read(new File("src/main/resources/images/J.png"));
            L = ImageIO.read(new File("src/main/resources/images/L.png"));
            O = ImageIO.read(new File("src/main/resources/images/O.png"));
            S = ImageIO.read(new File("src/main/resources/images/S.png"));
            T = ImageIO.read(new File("src/main/resources/images/T.png"));
            Z = ImageIO.read(new File("src/main/resources/images/Z.png"));
            background = ImageIO.read(new File("src/main/resources/images/background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, null);
        //平移坐标轴
        g.translate(22, 15);
        //绘制游戏主区域
        paintWall(g);
        //绘制下落的四方格
        paintCurrentOne(g);
        //绘制下一个将下落的四方格
        paintNextOne(g);
        //绘制游戏得分
        paintScore(g);
        //绘制当前游戏状态
        paintState(g);
    }

    public void start() {
        game_state = PLING;
        KeyListener l = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                switch (code) {
                    case KeyEvent.VK_DOWN:
                        sortDropActive();
                        break;
                    case KeyEvent.VK_LEFT:
                        moveleftActive();
                        break;
                    case KeyEvent.VK_RIGHT:
                        moveRightActive();
                        break;
                    case KeyEvent.VK_UP:
                        rotateRightActive();
                        break;
                    case KeyEvent.VK_SPACE:
                        handDropActive();
                        break;
                    case KeyEvent.VK_P:
                        //判断当前状态
                        if (game_state == PLING)
                            game_state = STOP;
                        break;
                    case KeyEvent.VK_C:
                        if (game_state == STOP)
                            game_state = PLING;
                        break;
                    case KeyEvent.VK_S:
                        //重新开始
                        game_state = PLING;
                        wall = new Cell[18][9];
                        currentOne = Tetromino.randomOne();
                        nextOne = Tetromino.randomOne();
                        totalScore = 0;
                        totalLine = 0;
                        break;
                }
            }
        };

        //将窗口设置为焦点
        this.addKeyListener(l);
        this.requestFocus();

        while (true) {
            if (game_state == PLING) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (canDrop()) {
                    currentOne.moveDrop();
                } else {
                    landToWall();
                    destroyLine();
                    if (isGameOver()) {
                        game_state = OVER;
                    } else {
                        //没结束
                        currentOne = nextOne;
                        nextOne = Tetromino.randomOne();
                    }
                }
            }
            repaint();
        }
    }

    //顺时针旋转
    public void rotateRightActive() {
        currentOne.rotateRight();
        if (outOfBounds() || coincide()) {
            currentOne.rotateLeft();
        }
    }

    //瞬间下落
    public void handDropActive() {
        while (true) {
            if (canDrop()) {
                currentOne.moveDrop();
            } else {
                break;
            }
        }
        //嵌入墙中
        landToWall();
        destroyLine();
        if (isGameOver()) {
            game_state = OVER;
        } else {
            currentOne = nextOne;
            nextOne = Tetromino.randomOne();
        }
    }

    //按键下落
    public void sortDropActive() {
        if (canDrop()) {
            currentOne.moveDrop();
        } else {
            landToWall();
            destroyLine();
            if (isGameOver()) {
                game_state = OVER;
            } else {
                currentOne = nextOne;
                nextOne = Tetromino.randomOne();
            }
        }
    }

    //嵌入墙
    private void landToWall() {
        Cell[] cells = currentOne.cells;
        for (Cell cell : cells) {
            int row = cell.getRow();
            int col = cell.getCol();
            wall[row][col] = cell;
        }
    }


    //判断四方格能否下落
    public boolean canDrop() {
        Cell[] cells = currentOne.cells;
        for (Cell cell : cells) {
            int row = cell.getRow();
            int col = cell.getCol();
            //判断是否到底
            if (row == wall.length - 1) {
                return false;
            } else if (wall[row + 1][col] != null) {
                return false;
            }
        }
        return true;
    }

    //消除行
    public void destroyLine() {
        int line = 0;
        Cell[] cells = currentOne.cells;
        for (Cell cell : cells) {
            int row = cell.getRow();
            if (isFullLine(row)) {
                line++;
                for (int i = row; i > 0; i--) {
                    System.arraycopy(wall[i - 1], 0, wall[i], 0, wall[0].length);
                }
                wall[0] = new Cell[9];
            }
        }
        totalScore += scores_pool[line];
        //总行数
        totalLine += line;
    }


    //判断当前行是否已经满了
    public boolean isFullLine(int row) {
        Cell[] cells = wall[row];
        for (Cell cell : cells) {
            if (cell == null) return false;
        }
        return true;
    }

    //判断游戏是否结束
    public boolean isGameOver() {
        Cell[] cells = nextOne.cells;
        for (Cell cell : cells) {
            int row = cell.getRow();
            int col = cell.getCol();
            if (wall[row][col] != null) {
                return true;
            }
        }
        return false;
    }

    private void paintState(Graphics g) {
        if (game_state == PLING) {
            g.drawString(show_state[PLING], 500, 660);
        } else if (game_state == STOP) {
            g.drawString(show_state[STOP], 500, 660);
        } else {
            g.drawString(show_state[OVER], 500, 660);
            g.setColor(Color.RED);
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 60));
            g.drawString("GAME OVER!", 30, 400);
        }
    }

    private void paintScore(Graphics g) {
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        g.drawString("分数: " + totalScore, 500, 250);
        g.drawString("行数: " + totalLine, 500, 430);
    }

    private void paintNextOne(Graphics g) {
        Cell[] cells = nextOne.cells;
        for (Cell cell : cells) {
            int x = cell.getCol() * CELL_SIZE + 370;
            int y = cell.getRow() * CELL_SIZE + 25;
            g.drawImage(cell.getImage(), x, y, null);
        }
    }

    private void paintCurrentOne(Graphics g) {
        Cell[] cells = currentOne.cells;
        for (Cell cell : cells) {
            int x = cell.getCol() * CELL_SIZE;
            int y = cell.getRow() * CELL_SIZE;
            g.drawImage(cell.getImage(), x, y, null);
        }
    }

    private void paintWall(Graphics g) {
        for (int i = 0; i < wall.length; i++) {
            for (int j = 0; j < wall[i].length; j++) {
                int x = j * CELL_SIZE;
                int y = i * CELL_SIZE;
                Cell cell = wall[i][j];
                if (cell == null) {
                    g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                } else {
                    g.drawImage(cell.getImage(), x, y, null);
                }
            }
        }
    }

    //判断是否出界
    public boolean outOfBounds() {
        Cell[] cells = currentOne.cells;
        for (Cell cell : cells) {
            int col = cell.getCol();
            int row = cell.getRow();
            if (row < 0 || row > wall.length - 1 || col < 0 || col > wall[0].length - 1) {
                return true;
            }
        }
        return false;
    }

    public void moveleftActive() {
        currentOne.moveLeft();
        if (outOfBounds() || coincide()) {
            currentOne.moveRight();
        }
    }

    public void moveRightActive() {
        currentOne.moveRight();
        if (outOfBounds() || coincide()) {
            currentOne.moveLeft();
        }
    }

    //判断是否重合
    public boolean coincide() {
        Cell[] cells = currentOne.cells;
        for (Cell cell : cells) {
            int row = cell.getRow();
            int col = cell.getCol();
            if (wall[row][col] != null) {
                return true;
            }
        }
        return false;
    }

    //启动类
    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Tetris");

        Tetris panel = new Tetris();
        jFrame.add(panel);
        jFrame.setVisible(true);
        jFrame.setSize(810, 940);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel.start();
    }
}
