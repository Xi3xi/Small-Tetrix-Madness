package src.utility;

import src.Tetrises.Tetris;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistic {
    public static final String fileName = "Statistics.txt";
    private String difficulty;
    private List<Integer> roundScores = new ArrayList<Integer>();
    private List<Map<String, Integer>> blocks = new ArrayList<Map<String, Integer>>();

    private String[] types = new String[]{"I", "J", "L", "O", "S", "T", "Z", "Plus", "P", "Q"};
    Path file = Paths.get("Statistics.txt");


    public Statistic(Tetris tetris) {
        switch (tetris.getDifficulty()) {
            case "easy":
                difficulty = "Easy";
                break;
            case "medium":
                difficulty = "Medium";
                break;
            case "madness":
                difficulty = "Madness";
                break;
        }
    }

    public void updateScore(int round, int score) {
        // if it is the first score in the given round
        if (roundScores.size() == round) {
            roundScores.add(round,score);
        } else {
            roundScores.set(round, score);
        }
    }
/**Using the method to record the played blocks in rounds */
    public void recordBlocks(int round, String blockType) {
        Map<String, Integer> b;

        if (blocks.size() == round) {
            b = new HashMap<>();
            b.put(blockType, 1);
            blocks.add(round, b);
        } else {
            b = blocks.get(round);
            b.put(blockType, b.getOrDefault(blockType, 0) + 1);
            blocks.set(round, b);
        }
    }
    /**Using the method to print the game record to the file */
    public void printStatistic() {

        List<String> statistic = new ArrayList<>();

        // calculate the average score
        int average = (int)roundScores.stream().mapToDouble(d -> d).average().orElse(0.0);
        int roundIndex = 0;
        statistic.add("Difficulty: " + difficulty);
        statistic.add("Average score per round: " + average);

        for(;roundIndex < roundScores.size(); roundIndex ++){
            statistic.add("------------------------------------------");

            statistic.add("Round #" + (roundIndex + 1));
            statistic.add("Score: " + roundScores.get(roundIndex));

            //write the block's behaviours in the file.
            Map<String, Integer> b;
            b = blocks.get(roundIndex);

            for(int typeIndex = 0; typeIndex < types.length; typeIndex++){
                String t = types[typeIndex];
                if( t == "Plus"){
                    t = "+";
                }
                if(b.get(t) == null){
                    statistic.add(t + ": " + 0);
                }else{
                    statistic.add(t + ": " + b.get(t));
                }
            }
        }
        // format output and write to file
        try {
            Files.write(file, statistic, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
