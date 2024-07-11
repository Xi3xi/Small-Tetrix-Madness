package src.blocks;

import ch.aplu.jgamegrid.Location;
import src.Tetrises.Tetris;
import src.TetroBlock;

public class Q extends Block {
    private static final int blockId = 6;
    private static final String blockName = "Q";
    private static final int blockNum = 5;

    public Q(Tetris tetris) {
        super(blockName, blockNum, tetris);
    }

    public void setRotation() {
        Location[][] r = super.getR();

        r[0][0] = new Location(new Location(0, 0));
        r[1][0] = new Location(new Location(1, 0));
        r[2][0] = new Location(new Location(1, 1));
        r[3][0] = new Location(new Location(0, 1));
        r[4][0] = new Location(new Location(1, 2));
        // rotId 1
        r[0][1] = new Location(new Location(0, 0));
        r[1][1] = new Location(new Location(0, 1));
        r[2][1] = new Location(new Location(-1, 1));
        r[3][1] = new Location(new Location(-1, 0));
        r[4][1] = new Location(new Location(-2, 1));
        // rotId 2
        r[0][2] = new Location(new Location(0, 0));
        r[1][2] = new Location(new Location(-1, 0));
        r[2][2] = new Location(new Location(-1, -1));
        r[3][2] = new Location(new Location(0, -1));
        r[4][2] = new Location(new Location(-1, -2));
        // rotId 3
        r[0][3] = new Location(new Location(0, 0));
        r[1][3] = new Location(new Location(0, -1));
        r[2][3] = new Location(new Location(1, -1));
        r[3][3] = new Location(new Location(1, 0));
        r[4][3] = new Location(new Location(2, -1));

        for (int i = 0; i < r.length; i++)
            blocks.add(new TetroBlock(blockId, r[i]));
    }
}
