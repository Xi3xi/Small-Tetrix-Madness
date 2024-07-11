package src.Tetrises;

import src.Tetrises.Tetris;
import src.blocks.*;
import java.util.Random;

/** Automatically generate a block of different type in various game level(difficulty).
 * When adding new shape of blocks, this class should be modified*/
public class BlockCreator {

    /**In this method, we will create two blocks, one for the game section, another one for the preview section */
    public Block[] createBlock(Random random, Tetris tetris){
        Block[] blocks = new Block[2];

        //randomly generate the type of the block.
        String blockType = randomBlock(tetris.getDifficulty(), random);

        //create the corresponding type of block.
        blocks[0] = getBlockType(blockType, tetris);
        blocks[1] = getBlockType(blockType, tetris);

        return blocks;
    }

    /** Randomly generate different type of blocks according to the difficulty and given random number*/
    private String randomBlock(String difficulty, Random random){
        String blockType;
        String baseType[] = {"I", "J", "L", "O", "S", "T", "Z",};
        int baseTypeNum = 7;
        String complexType[] = {"P", "Q", "Plus"};
        int complexTypeNum = 3;
        //If there will be more type, we can add here.

        if(difficulty.equals("easy")){
            int type = random.nextInt(baseTypeNum);
            blockType = baseType[type];
        }else{
            int type = random.nextInt(baseTypeNum + complexTypeNum);;
            if(type >= baseTypeNum){
                blockType = complexType[type - baseTypeNum];
            }else{
                blockType = baseType[type];
            }
        }
        return blockType;
    }

    /**create specific block according to the random given block type.*/
    private Block getBlockType(String blockType, Tetris tetris){
        Block block;

        switch(blockType){
            case "J":
                block = new J(tetris);
                break;
            case "L":
                block = new L(tetris);
                break;
            case "O":
                block = new O(tetris);
                break;
            case "S":
                block = new S(tetris);
                break;
            case "T":
                block = new T(tetris);
                break;
            case "Z":
                block = new Z(tetris);
                break;
            case "P":
                block = new P(tetris);
                break;
            case "Q":
                block = new Q(tetris);
                break;
            case "Plus":
                block = new Plus(tetris);
                break;
            default:
                block = new I(tetris);
        }
        return block;
    }
}
