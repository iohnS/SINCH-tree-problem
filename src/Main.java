package src;

public class Main {
    
    public static void main (String[] args){
        int[][] example1 = {
            {6,0,0,0,0,0},
            {0,0,0,0,0,0},
            {0,3,0,3,0,3}
        };
        int[][] example2 = {
            {0,4,0,0,0,5},
            {0,0,2,0,3,0},
            {0,0,0,1,0,0},
            {0,0,2,1,0,0},
            {0,4,0,0,3,0},
            {6,0,0,0,0,5}
        };
        Solver solver = new Solver();
        solver.setup(6, 3);
        int[][] outMatrix = solver.solve(example1);
        
        System.out.println(solver.bestScore);
        int[][] matrix = solver.getMatrix();
        
        for (int i = 0; i < outMatrix.length; i++) {
            for (int j = 0; j < outMatrix[i].length; j++) {
                System.out.print(outMatrix[i][j] + " ");
            }
            System.out.println();
        }

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}
