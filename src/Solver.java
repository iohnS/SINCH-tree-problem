package src;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;

public class Solver {	
    private int width;
    private int height;
    private int[][] startingMatrix;
    List<int[]> bestMove = new ArrayList<>();
    public int bestScore = 0;
    HashMap<Integer, List<int[]>> solutionMap = new HashMap<>();

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
        return startingMatrix;
    }

    public int[][] solve(int[][] matrix){
        this.startingMatrix = matrix;
        pleaseWork(0,0,0, matrix, new LinkedList<int[]>());
        return bestMove.toArray(new int[0][]);
    }

    private void pleaseWork(int row, int col, int totalHeight, int[][] matrix, List<int[]> moves){
        if(col >= width){
            row += 1;
            col = 0;
        }
        if(row >= height){
            if(bestScore < totalHeight){
                bestScore = totalHeight;
                bestMove = moves;
            }
            return;
        }

        int currentHeight = matrix[row][col];
        if(currentHeight < 2){
            pleaseWork(row, col+1, totalHeight, matrix, new ArrayList<>(moves));
            return;
        }

        List<Direction> directions = cuttableTreeDirections(row, col, currentHeight, matrix);
        if(!directions.isEmpty()){
            totalHeight += currentHeight;
        }
        directions.add(null);
        for(Direction d : directions){
            cutTree(row, col, currentHeight, d, matrix, moves);
            pleaseWork(row, col+1, totalHeight, matrix, new ArrayList<>(moves));
        }
    }

    private void cutTree(int row, int col, int treeHeight, Direction dir, int[][] matrix, List<int[]> moves){
        if(dir == null){return;}
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
        moves.add(new int []{row, col, dir.toInt()});
    }

    private List<Direction> cuttableTreeDirections(int row, int col, int treeHeight, int[][] matrix){
        List<Direction> directions = new LinkedList<>();
        for(Direction d : Direction.values()){
            if(!hitTree(row, col, treeHeight, d, matrix) && !hitFence(row, col, treeHeight, d)){
                directions.add(d);
            }
        }
        return directions;
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

    private boolean hitTree(int row, int col, int treeHeight, Direction dir, int[][] matrix){
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

}