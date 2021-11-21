package src;

import java.util.List;

import java.util.LinkedList;

public class Solver {	
    private int width;
    private int height;
    private int[][] matrix;
    public int finalHeight;
    private List<int []> outPutList = new LinkedList<>();
    List<Integer> list;

    public Solver(){
    }

    // width and height of yard is passed,
    // where width is west-east and height is north-south
    public void setup(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public enum Direction {
		EAST(0),
		NORTH(1),
		WEST(2),
		SOUTH(3);

		private final int dir;

		private Direction(int dir) {
			this.dir = dir;
		}

		public int toInt() {
			return this.dir;
		}

		public Direction opposite() {
			return Direction.values()[(this.dir+2) % 4];
		}
	}

    public int[][] getMatrix(){
        return matrix;
    }

    public int[][] solve(int[][] matrix){
        this.matrix = matrix;
        return solve();
    }

    private int[][] solve(){
        int treeHeight = 0;
        int frow = 0;
        int fcol = 0;
        while(treeHeight < 2){
            for(int row = 0; row < matrix.length; row++){
                for(int col = 0; col < matrix[0].length; col++){
                    treeHeight = matrix[row][col];
                }
            }
        }
        list = recursiveSolve(frow, fcol, treeHeight);
        return matrix;
    }


    /**
     * 
     * REKURSIONEN MÅSTE TA MED SIN EGNA OUTMATRIX 
     * 
     * Basfall:
     * B1: Listan är slut
     * B2: Inga fler träd att hugga. 
     * 
     * 1. Kolla basfall 
     * 2. Kolla om platsen vi är på är ett träd eller inte. 
     *      2.1. Om det INTE är ett träd så försöker vi hitta ett träd. 
     *      2.2. Om det är ett träd -> 3.
     * 3. Så vill vi kolla vilka håll trädet kan fällas. Fäll sedan trädet åt det hållet. 
     * 
     * 1. För varje rekursiv vill jag först kolla hitta alla cuttable direcations. Sen cutta i alla de directions. Sen fortsätta till nästa träd. 
     */

    private List<Integer> recursiveSolve(int row, int col, int treeHeight){
        List<Integer> heightList = new LinkedList<>();
        for(Direction d : cuttableTreeDirections(row, col, treeHeight)){
            heightList.add(recursiveSolve(row, col, treeHeight, d));
        }
        return heightList;
    }

    private int recursiveSolve(int row, int col, int totalHeight, Direction dir){
        if(col >= width-1){
            row += 1;
            col = 0;
        }
        if(row >= height-1){return totalHeight;}

        if(matrix[row][col] < 2){
            return recursiveSolve(row, col+1, totalHeight, dir);
        }

        System.out.println("Row: " + row + "  Col: " + col);
        cutTree(row, col, matrix[row][col], dir);

        List<Direction> directions = cuttableTreeDirections(row, col, matrix[row][col]);
        if(!directions.isEmpty()){
            for(Direction d : directions){
                return recursiveSolve(row, col+1, totalHeight, d);
            }
        }

        return totalHeight;
    }


    private List<Direction> cuttableTreeDirections(int row, int col, int treeHeight){
        List<Direction> directions = new LinkedList<>();
        for(Direction d : Direction.values()){
            if(!hitTree(row, col, treeHeight, d) && !hitFence(row, col, treeHeight, d)){
                directions.add(d);
            }
        }
        return directions;
    }

    private Direction cuttableTreeDirection(int row, int col, int treeHeight){
        for(Direction d : Direction.values()){
            if(!hitTree(row, col, treeHeight, d) && !hitFence(row, col, treeHeight, d)){
                return d;
            }
        }
        return null;
    }

    private int[] cutTree(int row, int col, int treeHeight, Direction dir){
        if(dir == null){return new int[0];}
        finalHeight += treeHeight;
        for(int i = 0; i < treeHeight; i++){
            switch (dir){
                case NORTH:
                    matrix[row-i][col] = -1;
                    break;
                case SOUTH:
                    matrix[row+i][col] = -1;
                    break;
                case WEST:
                    matrix[row][col-i] = -1;
                    break;
                case EAST:
                    matrix[row][col+i] = -1;
                    break;
            }
        }
        return new int []{row, col, dir.toInt()};
    }

   
    private boolean hitFence(int row, int col, int treeHeight, Direction dir){
        switch (dir){
            case NORTH:
                return row-treeHeight+1 < 0;
            case SOUTH:
                return row+treeHeight > height;
            case WEST:
                return col-treeHeight+1 < 0;
            case EAST:
                return col+treeHeight > width;
            default:
                return false;
        }
    }

    private boolean inGarden(int row, int col){
        return row >= 0 && row < height && col >= 0 && col < width;
    }

    private boolean hitTree(int row, int col, int treeHeight, Direction dir){
        for(int i = 1; i < treeHeight; i++){
            switch (dir){
                case NORTH:
                    if(inGarden(row-i, col) && matrix[row-i][col] != 0){
                        return true;
                    }
                    break;
                case SOUTH:
                    if(inGarden(row+i, col) && matrix[row+i][col] != 0){
                        return true;
                    }
                    break;
                case WEST:
                    if(inGarden(row, col-i) && matrix[row][col-i] != 0){
                        return true;
                    }
                    break;
                case EAST:
                    if(inGarden(row, col+i) && matrix[row][col+i] != 0){
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    private int[][] solve2() {
        for(int row = 0; row < matrix.length; row++){
            for(int col = 0; col < matrix[0].length; col++){
                int treeHeight = matrix[row][col];
                if(treeHeight > 1){
                    Direction cutDirection = cuttableTreeDirection(row, col, treeHeight);
                    cutTree(row, col, treeHeight, cutDirection);
                }
            }
        }
        return outPutList.toArray(new int[outPutList.size()][]);
    }

    private void cutTree2(int row, int col, int treeHeight, Direction dir){
        if(dir == null){return;}
        finalHeight += treeHeight;
        for(int i = 0; i < treeHeight; i++){
            switch (dir){
                case NORTH:
                    matrix[row-i][col] = -1;
                    break;
                case SOUTH:
                    matrix[row+i][col] = -1;
                    break;
                case WEST:
                    matrix[row][col-i] = -1;
                    break;
                case EAST:
                    matrix[row][col+i] = -1;
                    break;
            }
        }
        outPutList.add(new int []{row, col, dir.toInt()});
    }


}






    //Vi kan checka om den krockar mot staket eller träd mot north. Om den gör det så köra opposite. Annars Kör west och ananrs opposite. 
    /**if(hitFence(row, col, treeHeight, dir) && hitTree(row, col, treeHeight, dir)){
    if(!canCutTree(row, col, treeHeight, dir.opposite())){
        return canCutTree(row, col, treeHeight, Direction.values()[(dir.ordinal()+1)]);
    }else{
        return false;
    }
}else{
    return true;
}*/


/**
 * 
 * 1. If the spot we are on is bigger than 1 there is a tree and we want to try and cut it.
 * 2. -1 represents fallen tree, 0 represents grass, 1 represents stone and any value above 1 is a tree. 
 * 3. If canCutTree returns the Direction NULL then there isn't any direction we can cut the tree. 
 * 
 */