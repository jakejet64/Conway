public class GameOfLifeOptimizerTester {
    public static void main(String[] args){
        GameOfLifeGAOptimizer testOptimizer = new GameOfLifeGAOptimizer(32, 8, 10, 2);
        testOptimizer.optimize(2);
    }
}
