// L.java
package src.blocks;
import ch.aplu.jgamegrid.*;
import src.Tetrises.Tetris;
import src.TetroBlock;

public class L extends Block {
  private static final int blockId = 2;
  private static final String blockName = "L";
  private static final int blockNum = 4;

  public L(Tetris tetris) {
    super(blockName, blockNum, tetris);
  }

  public void setRotation() {
    Location[][] r = super.getR();
    // rotId 0
    r[0][0] = new Location(new Location(1, 0));
    r[1][0] = new Location(new Location(0, 0));
    r[2][0] = new Location(new Location(-1, 0));
    r[3][0] = new Location(new Location(-1, 1));
    // rotId 1
    r[0][1] = new Location(new Location(0, 1));
    r[1][1] = new Location(new Location(0, 0));
    r[2][1] = new Location(new Location(0, -1));
    r[3][1] = new Location(new Location(-1, -1));
    // rotId 2
    r[0][2] = new Location(new Location(-1, 0));
    r[1][2] = new Location(new Location(0, 0));
    r[2][2] = new Location(new Location(1, 0));
    r[3][2] = new Location(new Location(1, -1));
    // rotId 3
    r[0][3] = new Location(new Location(0, -1));
    r[1][3] = new Location(new Location(0, 0));
    r[2][3] = new Location(new Location(0, 1));
    r[3][3] = new Location(new Location(1, 1));

    for (int i = 0; i < r.length; i++)
      blocks.add(new TetroBlock(blockId, r[i]));
  }
}
