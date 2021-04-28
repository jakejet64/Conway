import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;

public class GameOfLifeGAOptimizer {
    private int gameSize;
    private int answerSize;

    private int populationSize;
    private int nextGenerationSize;

    private boolean[][][] nextGeneration;
    private int[] nextGenFitnesses;
    private boolean[][][] population;
    private int[] fitnesses;

    private HashMap<String, Character> binToHex
            = new HashMap<String, Character>();

    // constructor to generate random starting answer
    public GameOfLifeGAOptimizer(int gameSize, int answerSize, int populationSize, int nextGenerationSize){
        initHashMap();
        this.populationSize = populationSize;
        this.gameSize = gameSize;
        this.answerSize = answerSize;
        this.nextGenerationSize = nextGenerationSize;
        nextGeneration = new boolean[nextGenerationSize][answerSize][answerSize];
        nextGenFitnesses = new int[nextGenerationSize];
        population = new boolean[populationSize][answerSize][answerSize];
        fitnesses = new int[populationSize];
        for(int i = 0; i < populationSize; i++){
            population[i] = generateNewAnswer();
            fitnesses[i] = fitness(population[i]);
        }
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

        int generation = 0;
        while(endTime.isAfter(LocalTime.now())){
            for(int i = 0; i < nextGenerationSize; i++){
                if(i % 3 == 0){
                    nextGeneration[i] = mutate(population[(int)(Math.random() * populationSize)]);
                }else if(i % 3 == 1){
                    nextGeneration[i] = crossOver(population[(int)(Math.random() * populationSize)],
                            population[(int)(Math.random() * populationSize)]);
                }else{
                    nextGeneration[i] = generateNewAnswer();
                }
                nextGenFitnesses[i] = fitness(nextGeneration[i]);
            }

            int changes = 0;
            while(getSmallestPopFit() < getLargestNextGenFit()){
                int nextGenIndex = getLargestNextGenFitIndex();
                int popIndex = getSmallestPopFitIndex();

                population[popIndex] = nextGeneration[nextGenIndex];
                fitnesses[popIndex] = nextGenFitnesses[nextGenIndex];
                nextGenFitnesses[nextGenIndex] = Integer.MIN_VALUE;
                changes++;
            }
            generation++;
            int diversified = checkDiversity();
            if(changes != 0){
                System.out.println("Generation: " + generation);
                System.out.println("Swaps: " + changes);
                System.out.println("Diversification replacements: " + diversified);
                System.out.println("Average population fitness: " + getAveragePopulationFitness());
                System.out.println("Current best fitness: " + getLargestPopFit());
                System.out.println("With solution: " + Arrays.toString(format(population[getLargestPopFitIndex()])));
                System.out.println();
            }
        }

    }

    private int checkDiversity(){
        int replaced = 0;
        for(int i = 0; i < (populationSize - 1); i++){
            for(int j = (i + 1); j < populationSize; j++){
                if(Arrays.deepEquals(population[i], population[j])){
                    population[j] = generateNewAnswer();
                    replaced++;
                }
            }
        }
        return replaced;
    }

    private String getAveragePopulationFitness() {
        int total = 0;
        for(int i = 0; i < populationSize; i++){
            total += fitnesses[i];
        }
        return "" + (total / populationSize);
    }

    private int getLargestPopFitIndex() {
        int largestFit = Integer.MIN_VALUE;
        int largestFitIndex = -1;
        for(int i = 0; i < populationSize; i++){
            if(fitnesses[i] > largestFit){
                largestFit = fitnesses[i];
                largestFitIndex = i;
            }
        }
        return largestFitIndex;
    }

    public int getLargestPopFit(){
        int largestFit = Integer.MIN_VALUE;
        for(int i = 0; i < populationSize; i++){
            if(fitnesses[i] > largestFit){
                largestFit = fitnesses[i];
            }
        }
        return largestFit;
    }

    public int getLargestNextGenFitIndex(){
        int largestFit = Integer.MIN_VALUE;
        int largestFitIndex = -1;
        for(int i = 0; i < nextGenerationSize; i++){
            if(nextGenFitnesses[i] > largestFit){
                largestFit = nextGenFitnesses[i];
                largestFitIndex = i;
            }
        }
        return largestFitIndex;
    }

    public int getSmallestPopFitIndex(){
        int smallestFit = Integer.MAX_VALUE;
        int smallestFitIndex = -1;
        for(int i = 0; i < populationSize; i++){
            if(fitnesses[i] < smallestFit){
                smallestFit = fitnesses[i];
                smallestFitIndex = i;
            }
        }
        return smallestFitIndex;
    }

    public int getSmallestPopFit(){
        int smallestFit = Integer.MAX_VALUE;
        for(int i = 0; i < populationSize; i++){
            if(fitnesses[i] < smallestFit){
                smallestFit = fitnesses[i];
            }
        }
        return smallestFit;
    }

    public int getLargestNextGenFit(){
        int largestFit = Integer.MIN_VALUE;
        for(int i = 0; i < nextGenerationSize; i++){
            if(nextGenFitnesses[i] > largestFit){
                largestFit = nextGenFitnesses[i];
            }
        }
        return largestFit;
    }

    private boolean[][] mutate(boolean[][] in){
        boolean[][] ret = new boolean[answerSize][answerSize];
        for(int row = 0; row < answerSize; row++){
            for(int col = 0; col < answerSize; col++){
                // for each cell, there is a 70% chance that
                // it will remain as it is within the input
                if(in[row][col]){
                    if(Math.random() < .7){
                        ret[row][col] = true;
                    }else{
                        ret[row][col] = false;
                    }
                }else{
                    if(Math.random() < .7){
                        ret[row][col] = false;
                    }else{
                        ret[row][col] = true;
                    }
                }
            }
        }
        return ret;
    }

    private boolean[][] crossOver(boolean[][] inOne, boolean[][] inTwo){
        boolean[][] ret = new boolean[answerSize][answerSize];
        for(int row = 0; row < answerSize; row++){
            for(int col = 0; col < answerSize; col++){
                // exclusive or
                if(inOne[row][col] && inTwo[row][col]){
                    ret[row][col] = false;
                }else if(inOne[row][col] || inTwo[row][col]){
                    ret[row][col] = true;
                }else{
                    ret[row][col] = false;
                }
            }
        }
        return ret;
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