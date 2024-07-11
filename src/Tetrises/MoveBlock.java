package src.Tetrises;

import ch.aplu.jgamegrid.Location;
import src.Tetrises.Tetris;
import src.blocks.Block;
import src.utility.Statistic;

import java.awt.event.KeyEvent;
/** The class is used for tetris game to manipulate block's movement.*/
public class MoveBlock {

    public static final double SIMPLE_RATE = 1.0;
    public static final int DEFAULT_SLOWDOWN = 5;
    public static final double MEDIUM_RATE = 0.8;
    public static double MADNESS_RATE;
    private Tetris tetris;
    private Block blockCurrent = null;
    private Block blockPreview = null;

    /***Handle user input to move block. Arrow left to move left, Arrow right to move right, Arrow up to rotate and
     Arrow down for going down*/
    public MoveBlock(Tetris tetris) {
        this.tetris = tetris;
    }

    public void moveBlock(int keyEvent) {
        switch (keyEvent) {
            case KeyEvent.VK_UP:
                getBlockCurrent().rotate();
                break;
            case KeyEvent.VK_LEFT:
                getBlockCurrent().left();
                break;
            case KeyEvent.VK_RIGHT:
                getBlockCurrent().right();
                break;
            case KeyEvent.VK_DOWN:
                getBlockCurrent().drop();
                break;
            default:
                return;
        }
    }

    //change falling speed of each block
    public int changeFallingSpeed(int score){
        int slowDown = (int)(DEFAULT_SLOWDOWN * getRate(score));
        slowDown = setSpeed(score, slowDown);
        return slowDown;
    }

    // Set speed of tetrisBlocks including the rate of the speed.
    public int setSpeed(int score, int slowDown) {

        double rate = getRate(score);

        if (score > 10)
            slowDown = (int) (4 * rate);
        if (score > 20)
            slowDown = (int) (3 * rate);
        if (score > 30)
            slowDown = (int) (2 * rate);
        if (score > 40)
            slowDown = (int) (1 * rate);
        if (score > 50)
            slowDown = 0;

        return slowDown;
    }
    //Get the rate of block's moving speed according to the game difficulty
    public double getRate(int score){
        switch(tetris.getDifficulty()){
            case "easy":
                return SIMPLE_RATE;
            case "medium":
                return MEDIUM_RATE;
            case "madness": {
                int currentDefault = DEFAULT_SLOWDOWN - (score / 10);

                //range of speed is [S, 2 * S]
                int S = currentDefault / 2;
                int slowDown = S + tetris.getRandom().nextInt(S);

                MADNESS_RATE = (double) slowDown / currentDefault;
                return MADNESS_RATE;
            }
            default:
                return 1.0;
        }
    }

    /** using rotation to decide whether blocks in this game can rotate*/
    public boolean rotation() {
        if(tetris.getDifficulty().equals("madness")) {
            return false;
        }else{
            return true;
        }
    }

    public void setBlockPreview(Block blockPreview) {
        this.blockPreview = blockPreview;
    }

    public Block getBlockPreview() {
        return blockPreview;
    }

    public void setBlockCurrent(Block blockCurrent) {
        this.blockCurrent = blockCurrent;
    }

    public Block getBlockCurrent() {
        return blockCurrent;
    }
}
