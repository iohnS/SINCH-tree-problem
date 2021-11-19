package src;

import java.util.Arrays;

public class Solver {	
    private int width;
    private int height;
    private int[][] matrix;
    public int totalHeight;

    public Solver(){
        setup(6, 3);
        plantTrees();
    }

    // width and height of yard is passed,
    // where width is west-east and height is north-south
    public void setup(int width, int height) {
        this.matrix = new int[height][width];
        this.width = width;
        this.height = height;
    }

    public void plantTrees(){
        for(int i = 0; i < matrix.length; i++){
            Arrays.fill(matrix[i], 0);
        }
        matrix[0][0] = 6;
        matrix[2][1] = 3;
        matrix[2][3] = 3;
        matrix[2][5] = 3;
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

    // removed int[][] matrix from solve() parameter. 
    public int[][] solve() {
        for(int row = 0; row <- matrix.length; row++){
            for(int col = 0; col <- matrix[0].length; col++){
                int treeHeight = matrix[row][col];
                if(treeHeight > 1){
                    Direction cutDirection = cuttableTreeDirection(row, col, treeHeight);
                    cutTree(row, col, treeHeight, cutDirection);
                }
            }
        }

        return matrix;
    }

    public Direction cuttableTreeDirection(int row, int col, int treeHeight){
        for(Direction d : Direction.values()){
            if(!(hitTree(row, col, treeHeight, d) && hitFence(row, col, treeHeight, d))){
                return d;
            }
        }
        return null;
    }

    public void cutTree(int row, int col, int treeHeight, Direction dir){
        if(dir == null){return;}
        totalHeight += treeHeight;
        for(int i = 0; i < treeHeight; i++){
            switch (dir){
                case NORTH:
                    matrix[row-i][col] = -1;
                case SOUTH:
                    matrix[row+i][col] = -1;
                case WEST:
                    matrix[row][col-i] = -1;
                case EAST:
                    matrix[row][col+i] = -1;
            }
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

    

    //The tree hits the fence when row - treeheight < 0 (negative)
    public boolean hitFence(int row, int col, int treeHeight, Direction dir){
        switch (dir){
            case NORTH:
                return row-treeHeight < 0;
            case SOUTH:
                return row+treeHeight > height;
            case WEST:
                return col-treeHeight < 0;
            case EAST:
                return col+treeHeight > width;
            default:
                return false;
        }
    }

    //kolla i varje direction om det finns träd/stenar tills längden h. 
    public boolean hitTree(int row, int col, int treeHeight, Direction dir){
        for(int i = 0; i < treeHeight; i++){
            switch (dir){
                case NORTH:
                    //if there is a tree or rock the value of the would be 
                    if(matrix[row-i][col] != 0){
                        return true;
                    }
                case SOUTH:
                    if(matrix[row+i][col] != 0){
                        return true;
                    }
                case WEST:
                    if(matrix[row][col-i] != 0){
                        return true;
                    }
                case EAST:
                    if(matrix[row][col+i] != 0){
                        return true;
                    }
            }
        }
        return false;

    }

}


/**
 * 
 * 1. If the spot we are on is bigger than 1 there is a tree and we want to try and cut it.
 * 2. -1 represents fallen tree, 0 represents grass, 1 represents stone and any value above 1 is a tree. 
 * 3. If canCutTree returns the Direction NULL then there isn't any direction we can cut the tree. 
 * 
 */