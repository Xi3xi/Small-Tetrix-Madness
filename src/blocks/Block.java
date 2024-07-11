package src.blocks;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;
import src.Moveable;
import src.Tetrises.Tetris;
import src.TetroBlock;

import java.util.ArrayList;

/** using the abstract class "Block" to store the common methods of different tetris blocks.*/
public abstract class Block extends Actor implements Moveable {
    private final String blockName;
    private Location[][] r;

    protected Tetris tetris;
    private boolean isStarting = true;
    private int rotId = 0;
    private int nb;
    protected ArrayList<TetroBlock> blocks = new ArrayList<TetroBlock>();
    private Block nextTetrisBlock;
    private String autoBlockMove = "";
    private int autoBlockIndex = 0;

    public Block(String blockName, int blockNum, Tetris tetris) {
        super();
        this.blockName = blockName;
        this.r = new Location[blockNum][blockNum];
        this.tetris = tetris;
        setRotation();
    }
    /** Using setRotation method to demonstrate the rotation behaviour of each type of blocks*/
    public abstract void setRotation();

    // The game is called in a run loop, this method for a block is called every 1/30 seconds as the starting point
    public void act()
    {
        if (isStarting) {
            for (TetroBlock a : blocks) {
                Location loc = new Location(getX() + a.getRelLoc(0).x, getY() + a.getRelLoc(0).y);
                gameGrid.addActor(a, loc);
            }
            isStarting = false;
            nb = 0;
        } else if (nb >= blocks.size() && canAutoPlay()) {
            autoMove();
        } else {
            setDirection(90);
            if (nb == 1)
                nextTetrisBlock = tetris.createRandomTetrisBlock();

            if (!advance()) {
                if (nb == 0) // Game is over when tetrisBlock cannot fall down
                    tetris.gameOver();
                else {
                    setActEnabled(false);
                    gameGrid.addActor(nextTetrisBlock, new Location(6, 0));
                    tetris.setCurrentTetrisBlock(nextTetrisBlock);
                }
            }
            nb++;
        }
    }

    // Based on the input in the properties file, the block can move automatically
    @Override
    public void autoMove() {
        String moveString = autoBlockMove.substring(autoBlockIndex, autoBlockIndex + 1);
        switch (moveString) {
            case "L":
                left();
                break;
            case "R":
                right();
                break;
            case "T":
                rotate();
                break;
            case "D":
                drop();
                break;
        }

        autoBlockIndex++;
    }

    // Check if the block can be played automatically based on the properties file
    private boolean canAutoPlay() {
        if (autoBlockMove != null && !autoBlockMove.equals("")) {
            if (autoBlockMove.length() > autoBlockIndex) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    // implement methods in the Moveable interface to move the block left, right, drop and rotate the block.
    @Override
    public void left()
    {
        if (isStarting)
            return;
        setDirection(180);
        advance();
    }
    @Override
    public void right()
    {
        if (isStarting)
            return;
        setDirection(0);
        advance();
    }
    @Override
    public void rotate()
    {
        if (isStarting)
            return;

        int oldRotId = rotId; // Save it
        rotId++;
        if (rotId == 4)
            rotId = 0;

        if (canRotate(rotId))
        {
            for (TetroBlock a : blocks)
            {
                Location loc = new Location(getX() + a.getRelLoc(rotId).x, getY() + a.getRelLoc(rotId).y);
                a.setLocation(loc);
            }
        }
        else
            rotId = oldRotId;  // Restore

    }

    private boolean canRotate(int rotId)
    {
        // Check for every rotated tetroBlock within the tetrisBlock
        for (TetroBlock a : blocks)
        {
            Location loc =
                    new Location(getX() + a.getRelLoc(rotId).x, getY() + a.getRelLoc(rotId).y);
            if (!gameGrid.isInGrid(loc))  // outside grid->not permitted
                return false;
            TetroBlock block =
                    (TetroBlock)(gameGrid.getOneActorAt(loc, TetroBlock.class));
            if (blocks.contains(block))  // in same tetrisBlock->skip
                break;
            if (block != null)  // Another tetroBlock->not permitted
                return false;
        }
        //check whether game can rotate
        return true && tetris.getMoveBlock().rotation();
    }
    @Override
    public void drop()
    {
        if (isStarting)
            return;
        setSlowDown(0);
    }

    // Override Actor.setDirection()
    public void setDirection(double dir)
    {
        super.setDirection(dir);
        for (TetroBlock a : blocks)
            a.setDirection(dir);
    }

    @Override
    // Override Actor.move() and Moveable interface
    public void move()
    {
        if (isRemoved())
            return;
        super.move();
        for (TetroBlock a : blocks)
        {
            if (a.isRemoved())
                break;
            a.move();
        }
    }

    // Logic to check if the block has been removed (as winning a line) or drop to the bottom
    private boolean advance()
    {
        boolean canMove = false;
        for (TetroBlock a: blocks) {
            if (!a.isRemoved()) {
                canMove = true;
            }
        }
        for (TetroBlock a : blocks)
        {
            if (a.isRemoved())
                continue;
            if (!gameGrid.isInGrid(a.getNextMoveLocation()))
            {
                canMove = false;
                break;
            }
        }

        for (TetroBlock a : blocks)
        {
            if (a.isRemoved())
                continue;
            TetroBlock block =
                    (TetroBlock)(gameGrid.getOneActorAt(a.getNextMoveLocation(),
                            TetroBlock.class));
            if (block != null && !blocks.contains(block))
            {
                canMove = false;
                break;
            }
        }

        if (canMove)
        {
            move();
            return true;
        }
        return false;
    }

    // Override Actor.removeSelf()
    public void removeSelf()
    {
        super.removeSelf();
        for (TetroBlock a : blocks)
            a.removeSelf();
    }

    public String toString() {
        return "For testing, do not change: Block: " + blockName + ". Location: " + blocks + ". Rotation: " + rotId;
    }
    public void display(GameGrid gg, Location location)
    {
        for (TetroBlock a : blocks) {
            Location loc = new Location(location.x + a.getRelLoc(0).x, location.y + a.getRelLoc(0).y);
            gg.addActor(a, loc);
        }
    }
    public void setAutoBlockMove(String autoBlockMove) {
        this.autoBlockMove = autoBlockMove;
    }

    //return the location of blocks
    public Location[][] getR() {
        return this.r;
    }

}
