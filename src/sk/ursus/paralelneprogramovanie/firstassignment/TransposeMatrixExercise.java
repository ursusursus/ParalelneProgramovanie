package sk.ursus.paralelneprogramovanie.firstassignment;

import java.util.Arrays;
import java.util.Random;

import mpi.MPI;

import sk.ursus.paralelneprogramovanie.Exercise;

public class TransposeMatrixExercise implements Exercise {

	private static final Random random = new Random();
	private static final int ROOT = 0;
	private static final int TAG = 1234;

	@Override
	public void run(String[] args) {
		MPI.Init(args);

		int rank = MPI.COMM_WORLD.Rank();
		int size = MPI.COMM_WORLD.Size();
		if (size != 3) {
			System.out.println("There are exactly 3 processes required");
			return;
		}

		if (rank == ROOT) {
			int rows = 4;
			int columns = 3;
			int[] matrix = new int[rows * columns];
			generateMatrix(matrix, 4, 4);
			if (matrix.length % 2 != 0) {
				System.out.println("Matrix rows must be even");
				return;
			}

			System.out.println("Original matrix");
			printMatrix(matrix);

			/* int[][] transposedMatrix = transposeMatrix(matrix);
			System.out.println("Transposed Matrix");
			printMatrix(transposedMatrix); */

			Object[] arrayHalves = divideMatrixByRowsToHalves(matrix);
			int[][] firstHalf = (int[][]) arrayHalves[0];
			int[][] secondHalf = (int[][]) arrayHalves[1];

			int[] dimensions = new int[] {
					firstHalf.length, firstHalf[0].length
			};

			MPI.COMM_WORLD.Send(dimensions, 0, dimensions.length, MPI.INT, 1, TAG);
			MPI.COMM_WORLD.Send(firstHalf, 0, firstHalf.length, MPI.INT, 1, TAG);

			MPI.COMM_WORLD.Send(dimensions, 0, dimensions.length, MPI.INT, 2, TAG);
			MPI.COMM_WORLD.Send(secondHalf, 0, secondHalf.length * secondHalf[0].length, MPI.INT, 2, TAG);

			/* int[][] transposedFirstHalf = transposeMatrix(firstHalf);
			int[][] transposedSecondHalf = transposeMatrix(secondHalf);

			System.out.println("Joined Matrix");
			int[][] joinedMatrix = joinMatrices(transposedFirstHalf, transposedSecondHalf);
			printMatrix(joinedMatrix); */

		} else {
			int[] dimensions = new int[2];
			MPI.COMM_WORLD.Recv(dimensions, 0, dimensions.length, MPI.INT, ROOT, TAG);

			int[][] subMatrix = new int[dimensions[0]][dimensions[1]];
			MPI.COMM_WORLD.Recv(subMatrix, 0, subMatrix.length, MPI.OBJECT, ROOT, TAG);

			printMatrix(subMatrix);
		}

		MPI.Finalize();
	}

	private int[][] joinMatrices(int[][] firstHalf, int[][] secondHalf) {
		int[][] matrix = new int[firstHalf.length][firstHalf[0].length + secondHalf[0].length];
		System.out.println(matrix.length + " - " + matrix[0].length);

		int columnsHalf = matrix[0].length / 2;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (j < columnsHalf) {
					matrix[i][j] = firstHalf[i][j];
				} else {
					matrix[i][j] = secondHalf[i][j % columnsHalf];
				}
			}
		}
		return matrix;
	}

	private static void printMatrix(int[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println(" ");
	}

	private static void generateMatrix(int[] matrix, int rows, int columns) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				int number = random.nextInt() % 10;
				if (number < 0) {
					number *= -1;
				}
				matrix[i * rows + j] = number;
			}
		}
	}

	private static int[][] transposeMatrix(int[][] matrix) {
		int[][] transposedMatrix = new int[matrix[0].length][matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				transposedMatrix[j][i] = matrix[i][j];
			}
		}

		return transposedMatrix;
	}

	private static Object[] divideMatrixByRowsToHalves(int[][] matrix) {
		// int[][] firstHalf = Arrays.copyOfRange(matrix, 0, matrix.length / 2);
		// int[][] secondHalf = Arrays.copyOfRange(matrix, matrix.length / 2, matrix.length);
		int rowsHalf = matrix.length / 2;
		int[][] firstHalf = new int[rowsHalf][matrix[0].length];
		int[][] secondHalf = new int[rowsHalf][matrix[0].length];

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (i < rowsHalf) {
					firstHalf[i][j] = matrix[i][j];
				} else {
					secondHalf[i % rowsHalf][j] = matrix[i][j];
				}
			}
		}

		return new Object[] { firstHalf, secondHalf };
	}

}
