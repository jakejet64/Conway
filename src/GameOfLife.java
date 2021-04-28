import java.util.HashMap;

public class GameOfLife {
    private boolean[][] board;
    private int size;
    private HashMap<Character, String> hexToBin
            = new HashMap<Character, String>();
    private HashMap<String, Character> binToHex
            = new HashMap<String, Character>();

    public GameOfLife(int size){
        initHashMaps();
        this.size = size;
        board = new boolean[size][size];
        for(int row = 0; row < size; row++){
            for(int col = 0; col < size; col++){
                board[row][col] = false;
            }
        }
        for(int row = 1; row < (size - 1); row++){
            for(int col = 1; col < (size - 1); col++){
                if(Math.random() > .6){
                    board[row][col] = true;
                }
            }
        }
    }

    public GameOfLife(boolean[][] board){
        initHashMaps();
        this.size = board.length;
        this.board = board;
    }

    public GameOfLife(String[] board){
        initHashMaps();
        this.size = board.length;
        this.board = new boolean[size][size];
        for(int row = 0; row < board.length; row++){
            this.board[row] = HexToBinArr(board[row]);
        }
    }

    private void initHashMaps(){
        hexToBin.put('0', "0000"); binToHex.put("0000", '0');
        hexToBin.put('1', "0001"); binToHex.put("0001", '1');
        hexToBin.put('2', "0010"); binToHex.put("0010", '2');
        hexToBin.put('3', "0011"); binToHex.put("0011", '3');
        hexToBin.put('4', "0100"); binToHex.put("0100", '4');
        hexToBin.put('5', "0101"); binToHex.put("0101", '5');
        hexToBin.put('6', "0110"); binToHex.put("0110", '6');
        hexToBin.put('7', "0111"); binToHex.put("0111", '7');
        hexToBin.put('8', "1000"); binToHex.put("1000", '8');
        hexToBin.put('9', "1001"); binToHex.put("1001", '9');
        hexToBin.put('A', "1010"); binToHex.put("1010", 'A');
        hexToBin.put('B', "1011"); binToHex.put("1011", 'B');
        hexToBin.put('C', "1100"); binToHex.put("1100", 'C');
        hexToBin.put('D', "1101"); binToHex.put("1101", 'D');
        hexToBin.put('E', "1110"); binToHex.put("1110", 'E');
        hexToBin.put('F', "1111"); binToHex.put("1111", 'F');
    }

    public int getAlive(){
        int ret = 0;
        for(int row = 1; row < (size - 1); row++){
            for(int col = 1; col < (size - 1); col++){
                if(board[row][col]){
                    ret++;
                }
            }
        }
        return ret;
    }

    public void Run(int runs){
        int[][] neighbors;
        for(int i = 0; i < runs; i++){
            neighbors = generateNeighborsGrid();
            for(int row = 1; row < (size - 1); row++){
                for(int col = 1; col < (size - 1); col++){
                    if(neighbors[row][col] == 2){
                        // doNothing();
                    }else if(neighbors[row][col] == 3){
                        board[row][col] = true;
                    }else{
                        board[row][col] = false;
                    }
                }
            }
        }
    }

    public void RunPrint(int runs, boolean hex){
        int[][] neighbors;
        for(int i = 0; i < runs; i++){
            neighbors = generateNeighborsGrid();
            for(int row = 1; row < (size - 1); row++){
                for(int col = 1; col < (size - 1); col++){
                    if(neighbors[row][col] == 2){
                        // doNothing();
                    }else if(neighbors[row][col] == 3){
                        board[row][col] = true;
                    }else{
                        board[row][col] = false;
                    }
                }
            }

            if(hex){
                for(int row = 0; row < size; row++){
                    System.out.println(BinArrToHex(board[row]));
                }
            }else{
                for(int row = 0; row < size; row++){
                    for(int col = 0; col < size; col++){
                        if(board[row][col]){
                            System.out.print("*");
                        }else{
                            System.out.print("'");
                        }
                    }
                    System.out.println();
                }
            }
            System.out.println();
        }
    }

    private int[][] generateNeighborsGrid(){
        int[][] ret = new int[size][size];
        // iterate through the board except dead border
        for(int row = 1; row < (size - 1); row++){
            for(int col = 1; col < (size - 1); col++){
                //count the neighbors of each space
                int neighbors = 0;
                for(int checkRow = -1; checkRow <= 1; checkRow++){
                    for(int checkCol = -1; checkCol <= 1; checkCol++){
                        if(!(checkRow == 0 && checkCol == 0)){
                            if(board[row + checkRow][col + checkCol]){
                                neighbors++;
                            }
                        }
                    }
                }
                ret[row][col] = neighbors;
            }
        }
        return ret;
    }

    private boolean[] HexToBinArr(String in){
        in = in.toUpperCase();
        String binary = "";

        char ch;

        for (int i = 0; i < in.length(); i++) {
            ch = in.charAt(i);
            if (hexToBin.containsKey(ch))
                binary += hexToBin.get(ch);
            else {
                System.out.println("Invalid Hex Input");
            }
        }

        boolean[] ret = new boolean[binary.length()];
        for(int i = 0; i < binary.length(); i++){
            if(binary.charAt(i) == '0'){
                ret[i] = false;
            }else{
                ret[i] = true;
            }
        }

        return ret;
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