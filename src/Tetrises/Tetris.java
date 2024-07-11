package src.Tetrises;// Tetris.java

import ch.aplu.jgamegrid.*;
import src.*;
import src.blocks.*;
import src.utility.Statistic;

import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Tetris extends JFrame implements GGActListener {

    public static final int DEFAULT_AUTO_SPEED = 10;
    public static final int DEFAULT_SPEED = 100;
    public static final int DEFAULT_SLOWDOWN = 5;
    private static String difficulty; // record the game level
    private MoveBlock moveBlock = new MoveBlock(this); //manipulate the block's movement
    private int score = 0;
    public static int round = 0;
    private Statistic statistic; //record game stats and print into console
    private int slowDown = DEFAULT_SLOWDOWN;
    private Random random = new Random(0);
    private TetrisGameCallback gameCallback;
    private boolean isAuto = false;
    private int seed = 30006;
    // For testing mode, the block will be moved automatically based on the blockActions.
    // L is for Left, R is for Right, T is for turning (rotating), and D for down
    private String[] blockActions = new String[10];
    private int blockActionIndex = 0;

    // Initialise object
    private void initWithProperties(Properties properties) {
        this.seed = Integer.parseInt(properties.getProperty("seed", "30006"));
        random = new Random(seed);
        isAuto = Boolean.parseBoolean(properties.getProperty("isAuto"));
        String blockActionProperty = properties.getProperty("autoBlockActions", "");
        blockActions = blockActionProperty.split(",");

        difficulty = properties.getProperty("difficulty", ""); //read difficulty
        statistic = new Statistic(this); //initialize recorder
    }

    public Tetris(TetrisGameCallback gameCallback, Properties properties) {
        // Initialise value
        initWithProperties(properties);
        this.gameCallback = gameCallback;
        blockActionIndex = 0;

        // Set up the UI components. No need to modify the UI Components
        tetrisComponents = new TetrisComponents();
        tetrisComponents.initComponents(this);
        gameGrid1.addActListener(this);
        gameGrid1.setSimulationPeriod(getSimulationTime());

        // Add the first block to start
        moveBlock.setBlockCurrent(createRandomTetrisBlock()); // Polymorphically call sub methods
        record(moveBlock.getBlockCurrent()); //record the first block
        gameGrid1.addActor(moveBlock.getBlockCurrent(), new Location(6, 0));
        gameGrid1.doRun();

        // Do not lose keyboard focus when clicking this window
        gameGrid2.setFocusable(false);
        setTitle("Tetris Madness");
        score = 0;
        showScore(score);
    }

    /**This method in tetris class cannot create blocks.The child classes needs to override this method to have its own
    implementation of creating blocks*/
    public Block createRandomTetrisBlock() {
        BlockCreator creator = new BlockCreator();
        Block[] blocks = creator.createBlock(random, this);
        setPreview(blocks[1]);
        testAutoMove(blocks[0]);
        slowDown = moveBlock.changeFallingSpeed(score);

        //record(moveBlock.getBlockCurrent());
        return blocks[0];
    }

    /** set blocks in the preview section*/
    public void setPreview(Block t) {
        if (moveBlock.getBlockPreview() != null) {
            moveBlock.getBlockPreview().removeSelf();
        }
        t.display(gameGrid2, new Location(2, 1));
        moveBlock.setBlockPreview(t);
    }

    /** check whether it is an auto game*/
    public void testAutoMove(Block t) {
        // Test if the game is in auto test mode
        String currentBlockMove = "";
        if (blockActions.length > blockActionIndex) {
            currentBlockMove = blockActions[blockActionIndex];
        }
        blockActionIndex++;

        if (isAuto) {
            t.setAutoBlockMove(currentBlockMove);
        }
        slowDown = moveBlock.setSpeed(score, slowDown);
        t.setSlowDown(slowDown);
    }

    /** record all the related data of current block in the file*/
    public void setCurrentTetrisBlock(Block current) {
        gameCallback.changeOfBlock(moveBlock.getBlockCurrent());
        moveBlock.setBlockCurrent(current);

        //record every block in the statistic file
        record(moveBlock.getBlockCurrent());
    }

    public void act() {
        removeFilledLine();
        moveBlock.moveBlock(gameGrid1.getKeyCode());
        //System.out.println("current slowoown is " + slowDown);
    }

    private void removeFilledLine() {
        for (int y = 0; y < gameGrid1.nbVertCells; y++) {
            boolean isLineComplete = true;
            TetroBlock[] blocks = new TetroBlock[gameGrid1.nbHorzCells]; // One line
            // Calculate if a line is complete
            for (int x = 0; x < gameGrid1.nbHorzCells; x++) {
                blocks[x] = (TetroBlock) gameGrid1.getOneActorAt(new Location(x, y), TetroBlock.class);
                if (blocks[x] == null) {
                    isLineComplete = false;
                    break;
                }
            }
            if (isLineComplete) {
                // If a line is complete, we remove the component block of the shape that
                // belongs to that line
                for (int x = 0; x < gameGrid1.nbHorzCells; x++)
                    gameGrid1.removeActor(blocks[x]);
                ArrayList<Actor> allBlocks = gameGrid1.getActors(TetroBlock.class);
                for (Actor a : allBlocks) {
                    int z = a.getY();
                    if (z < y)
                        a.setY(z + 1);
                }
                gameGrid1.refresh();
                score ++;
                statistic.updateScore(round, score);
                gameCallback.changeOfScore(score);
                showScore(score);
                slowDown = moveBlock.setSpeed(score, slowDown);
            }
        }
    }

    // Show Score and Game Over
    private void showScore(final int score) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                scoreText.setText(score + " points");
            }
        });
    }

    public void gameOver() {
        gameGrid1.addActor(new Actor("sprites/gameover.gif"), new Location(5, 5));
        gameGrid1.doPause();
        if (isAuto) {
            System.exit(0);
        }
        //record the data of game in the statistic file after each round of game
        statistic.printStatistic();
    }

    // Start a new game round
    public void startBtnActionPerformed(java.awt.event.ActionEvent evt) {
        gameGrid1.doPause();
        gameGrid1.removeAllActors();
        gameGrid2.removeAllActors();
        gameGrid1.refresh();
        gameGrid2.refresh();
        GameGrid.delay(getDelayTime());
        blockActionIndex = 0;
        moveBlock.setBlockCurrent(createRandomTetrisBlock());
        gameGrid1.addActor(moveBlock.getBlockCurrent(), new Location(6, 0));
        gameGrid1.doRun();
        gameGrid1.requestFocus();
        score = 0;
        showScore(score);
        slowDown = moveBlock.setSpeed(score, slowDown);

        //after clicking the start bottom, a new round started
        round ++;
    }

    // Different speed for manual and auto mode
    public int getSimulationTime() {
        if (isAuto) {
            return DEFAULT_AUTO_SPEED;
        } else {
            return DEFAULT_SPEED;
        }
    }
    /** record all the game related data in the file*/
    public void record(Block block){
        statistic.updateScore(round, score);
        statistic.recordBlocks(round, block.getClass().getSimpleName());
        statistic.printStatistic();
    }

    private int getDelayTime() {
        if (isAuto) {
            return 200;
        } else {
            return 2000;
        }
    }

    public Random getRandom() {
        return this.random;
    }

    public static String getDifficulty() {
        return difficulty;
    }

    public MoveBlock getMoveBlock() {
        return moveBlock;
    }

    // AUTO GENERATED - do not modify//GEN-BEGIN:variables
    public ch.aplu.jgamegrid.GameGrid gameGrid1;
    public ch.aplu.jgamegrid.GameGrid gameGrid2;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JPanel jPanel2;
    public javax.swing.JPanel jPanel3;
    public javax.swing.JPanel jPanel4;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTextArea jTextArea1;
    public javax.swing.JTextField scoreText;
    public javax.swing.JButton startBtn;
    private TetrisComponents tetrisComponents;
    // End of variables declaration//GEN-END:variables
}
