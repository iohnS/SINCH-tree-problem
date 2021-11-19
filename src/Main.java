package src;

public class Main {
    public static void main (String[] args){
        Solver solver = new Solver();
        solver.setup(6,3);
        solver.plantTrees();
        solver.solve();
        System.out.println(solver.totalHeight);
        int[][] matrix = solver.getMatrix();
        
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}
