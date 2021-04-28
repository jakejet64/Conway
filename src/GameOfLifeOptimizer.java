import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;

public class GameOfLifeOptimizer {
    private int gameSize;
    private int answerSize;

    private boolean[][] bestAnswer;
    private int bestFitness;

    private HashMap<String, Character> binToHex
            = new HashMap<String, Character>();

    // constructor to generate random starting answer
    public GameOfLifeOptimizer(int gameSize, int answerSize){
        initHashMap();
        this.gameSize = gameSize;
        this.answerSize = answerSize;
        bestAnswer = generateNewAnswer();
        bestFitness = fitness(bestAnswer);
    }

    // constructor to set first board to be an input
    // note: doesn't check the correctness of board
    public GameOfLifeOptimizer(int answerSize, boolean[][] firstBoard){
        initHashMap();
        this.gameSize = firstBoard.length;
        this.answerSize = answerSize;
        bestAnswer = firstBoard;
        bestFitness = fitness(bestAnswer);
    }

    private void initHashMap(){
        binToHex.put("0000", '0');
        binToHex.put("0001", '1');
        binToHex.put("0010", '2');
        binToHex.put("0011", '3');
        binToHex.put("0100", '4');
        binToHex.put("0101", '5');
        binToHex.put("0110", '6');
        binToHex.put("0111", '7');
        binToHex.put("1000", '8');
        binToHex.put("1001", '9');
        binToHex.put("1010", 'A');
        binToHex.put("1011", 'B');
        binToHex.put("1100", 'C');
        binToHex.put("1101", 'D');
        binToHex.put("1110", 'E');
        binToHex.put("1111", 'F');
    }

    public void optimize(int hours){
        LocalTime startTime = LocalTime.now();
        LocalTime endTime = startTime.plusHours(hours);

        while(endTime.isAfter(LocalTime.now())){
            boolean[][] newAnswer = generateNewAnswer();
            int newAnswerFitness = fitness(newAnswer);
            if(newAnswerFitness > bestFitness){
                bestFitness = newAnswerFitness;
                bestAnswer = newAnswer;
                int minutes = (int) startTime.until(LocalTime.now(), ChronoUnit.MINUTES);
                System.out.println("New best answer found with fitness: "
                        + bestFitness + "\nAfter: " + minutes + " minutes");
                System.out.println(Arrays.toString(format(bestAnswer)));
                System.out.println();
            }
        }

    }

    private String[] format(boolean[][] binArr){
        String[] returnArr = new String[binArr.length];
        for(int row = 0; row < binArr.length; row++){
            returnArr[row] = BinArrToHex(binArr[row]);
        }
        return returnArr;
    }

    private boolean[][] withSpacing(boolean[][] answer){
        boolean[][] returnAnswer = new boolean[gameSize][gameSize];

        for(int row = 0; row < gameSize; row++){
            for(int col = 0; col < gameSize; col++){
                returnAnswer[row][col] = false;
            }
        }
        int spacing = (gameSize - answerSize) / 2;

        for(int row = spacing; row < (spacing + answerSize); row++){
            for(int col = spacing; col < (spacing + answerSize); col++){
                returnAnswer[row][col] =
                        answer[(row - spacing)][(col - spacing)];
            }
        }


        return returnAnswer;
    }

    private boolean[][] generateNewAnswer(){
        boolean[][] board = new boolean[answerSize][answerSize];
        for(int row = 0; row < answerSize; row++){
            for(int col = 0; col < answerSize; col++){
                if(Math.random() >= .6){
                    board[row][col] = true;
                }
            }
        }
        return board;
    }

    public int fitness(boolean[][] board){
        GameOfLife testingGame = new GameOfLife(withSpacing(board));
        testingGame.Run(1000);
        return testingGame.getAlive();
    }

    private String BinArrToHex(boolean[] in){
        String binary = "";

        for(int i = 0; i < in.length; i++){
            if(in[i]){
                binary += "1";
            }else{
                binary += "0";
            }
        }
        String ret = "";

        for(int i = 0; i < binary.length(); i += 4){
            String st = binary.substring(i, (i+4));
            // no need to check contains key,
            // this method is only used internally
            ret += binToHex.get(st);
        }

        return ret;
    }
}