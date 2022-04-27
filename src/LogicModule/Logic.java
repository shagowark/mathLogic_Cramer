package LogicModule;

import util.ArrayUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;


public class Logic {
    public static void getResultByInverseMatrixFile(String fileNameInput, String fileNameOutput) throws Exception {
        int[][] matrixA = ArrayUtils.readIntArray2FromFile(fileNameInput);
        int[][] matrixB = ArrayUtils.readIntArray2FromFile(fileNameInput);
        checkIfArrayIsNull(matrixA);
        checkIfArrayIsNull(matrixB);

        if (solveDeterminant(matrixA) == 0){
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileNameOutput));
            writer.write("Определитель равен 0, обратной матрицы не существует \n");
            writer.flush();
            return;
        }

        double[][] result = multiplyMatrixMatrix(solveMatrix(matrixA), matrixB);
        writeIntoFile(matrixA, matrixB, result, fileNameOutput);
    }

    public static double[][] getResultByInverseMatrixFrame(int[][] matrixA, int[][] matrixB) {
        return multiplyMatrixMatrix(solveMatrix(matrixA), matrixB);
    }

    public static double[][] getResultByKramerFrame(int[][] matrixA, int[][] matrixB) {
        double[][] result = new double[matrixA[0].length][1];
        for (int i = 0; i < matrixA[0].length; i++){
            int[][] tempMatrixA = new int[matrixA.length][matrixA[0].length];
            for (int r = 0; r < tempMatrixA.length; r++){
                for (int c = 0; c < tempMatrixA[0].length; c++){
                    tempMatrixA[r][c] = matrixA[r][c];
                }
            }
            for (int j = 0; j < tempMatrixA.length; j++){
                tempMatrixA[j][i] = matrixB[j][0];
            }
            result[i][0] = (double) solveDeterminant(tempMatrixA) / solveDeterminant(matrixA);
        }

        return result;
    }

    public static double[][] getResultByGaussFrame(int[][] extendedMatrix) {
        double[][] extendedMatrixDouble = new double[extendedMatrix.length][extendedMatrix[0].length];
        for (int i = 0; i < extendedMatrix.length; i++){
            for (int j = 0; j < extendedMatrix[0].length; j++){
                extendedMatrixDouble[i][j] = extendedMatrix[i][j];
            }
        }
        return solveByGauss(extendedMatrixDouble);
    }

    public static double[][] solveByGauss(double[][] extendedMatrix) {
        double[][] result = new double[extendedMatrix.length][1];

        // прямой ход
        int currentX = 0;
        for (int i = 0; i < extendedMatrix.length; i++){
            double del = extendedMatrix[i][currentX];
            for (int j = 0; j < extendedMatrix[0].length; j++){
                extendedMatrix[i][j] /= del;
            }

            if (i == extendedMatrix.length - 1){
                continue;
            }

            for (int row = i+1; row < extendedMatrix.length; row++){
                double coef = extendedMatrix[row][currentX] / extendedMatrix[i][currentX];
                for (int col = 0; col < extendedMatrix[0].length; col++){
                    extendedMatrix[row][col] -= extendedMatrix[i][col] * coef;
                }
            }

            currentX++;
        }

        // обратный ход
        currentX = extendedMatrix[0].length - 2;
        for (int i = extendedMatrix.length - 1 ; i > 0; i--) {

            for (int row = i-1; row > -1; row--){
                double coef = extendedMatrix[row][currentX] / extendedMatrix[i][currentX];
                for (int col = 0; col < extendedMatrix[0].length; col++){
                    extendedMatrix[row][col] -= extendedMatrix[i][col] * coef;
                }
            }

            currentX--;
        }

        // запись результатов
        for (int i = 0; i < extendedMatrix.length; i++){
            result[i][0] = extendedMatrix[i][extendedMatrix[0].length-1];
        }

        return result;
    }

    public static double[][] solveMatrix(int[][] mat){
        return multiplyMatrixNumber(getTransposedMatrix(getTempMatrix(mat)), 1.0 / solveDeterminant(mat));
    }

    public static int solveDeterminant(int[][] det){
        if (det.length == 1){
            return det[0][0];
        }
        if (det.length == 2){
            return det[0][0]*det[1][1] - det[0][1]*det[1][0];
        }
        if (det.length == 3){
            return det[0][0]*det[1][1]*det[2][2]+det[0][1]*det[1][2]*det[2][0]+det[1][0]*det[0][2]*det[2][1]
                    -det[0][2]*det[1][1]*det[2][0]-det[0][1]*det[1][0]*det[2][2]-det[0][0]*det[1][2]*det[2][1];
        }

        int sum = 0;
        for (int i = 0; i < det.length; i++){
            sum += Math.pow(-1, i+1+1) * det[i][0] * solveDeterminant(getMinor(det, i, 0));
        }

        return sum;
    }


    public static int[][] getTempMatrix(int[][] mat){
        int[][] tempMatrix = new int[mat.length][mat.length];

        for (int i = 0; i< mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                tempMatrix[i][j] = (int) (Math.pow(-1, i+j+2) * solveDeterminant(getMinor(mat, i, j)));
            }
        }

        return tempMatrix;
    }

    public static int[][] getTransposedMatrix(int[][] mat){
        int[][] transposedMatrix = new int[mat.length][mat.length];

        for(int i=0; i<mat.length; i++) {
            for(int j=0; j<mat[i].length; j++) {
                transposedMatrix[j][i] = mat[i][j];
            }
        }

        return transposedMatrix;
    }

    public static int[][] getMinor(int[][] matrix, int rowNumber, int columnNumber){
        int[][] minor = new int [matrix.length - 1][matrix.length - 1];
        int r = 0;
        for (int i = 0; i < matrix.length; i++){
            if (i == rowNumber){
                continue;
            }

            int c = 0;
            for (int j = 0; j < matrix.length; j++){
                if (j == columnNumber){
                    continue;
                }
                minor[r][c] = matrix[i][j];

                c++;
            }

            r++;
        }

        return minor;
    }


    public static double[][] multiplyMatrixNumber(int[][] mat, double num){
        double[][] newMat = new double[mat.length][mat.length];
        for (int i = 0; i< mat.length; i++){
            for (int j = 0; j < mat.length; j++){
                newMat[i][j] = mat[i][j] * num;
            }
        }

        return newMat;
    }

    public static double[][] multiplyMatrixMatrix(double[][] mat1, int[][] mat2){
        double[][] res = new double[mat1.length][mat2[0].length];

        for (int i = 0; i < res.length; i++){
            for (int j = 0; j < res[0].length; j++){
                res[i][j] =  Math.round(multiplyCell(mat1, mat2, i, j) * 100.00) / 100.00;
            }
        }

        return res;
    }

    public static double multiplyCell(double[][] mat1, int[][] mat2, int row, int col){
        double cell = 0;
        for (int i = 0; i < mat2.length; i++){
            cell += mat1[row][i] * mat2[i][col];
        }

        return cell;
    }

    public static void writeIntoFile(int[][] mat1,int[][] mat2, double[][] check, String fileName) throws Exception{
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        printMatrix(mat1, writer);
        printMatrix(mat2, writer);
        printMatrix(check, writer);
    }

    public static void printMatrix(int[][] mat, BufferedWriter writer) throws Exception{
        for (int[] line : mat){
            writer.write(Arrays.toString(line));
            writer.write("\n");
        }
        writer.write(" \n");
        writer.flush();
    }


    public static void printMatrix(double[][] mat, BufferedWriter writer) throws Exception{
        for (double[] line : mat){
            writer.write(Arrays.toString(line));
            writer.write("\n");
        }
        writer.write(" \n");
        writer.flush();
    }

    public static void checkIfArrayIsNull(int [][] array) throws Exception{
        if (array == null){
            throw new Exception("Отсутствует input файл");
        }
    }

    public static void checkIfArrayIsCorrect (int [][] array) throws Exception {
        if (array.length < 1 || array[0].length < 1) {
            throw new Exception("Input файл пустой");
        }
    }

    public static void checkIfArrayIsSquare (int [][] array) throws Exception {
        for (int[] line : array) {
            if (line.length != array.length) {
                throw new Exception("Матрица должна быть квадратной");
            }
        }
    }
}
