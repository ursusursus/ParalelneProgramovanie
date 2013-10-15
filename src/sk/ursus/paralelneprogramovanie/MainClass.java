package sk.ursus.paralelneprogramovanie;
import java.util.Random;

import sk.ursus.paralelneprogramovanie.firstassignment.FindMaximumExercise;

import mpi.MPI;

public class MainClass {

	public static final int ROOT = 0;
	private static final int BUFFER_LENGTH = 5;

	public static void main(String[] args) throws Exception {
		/* MPI.Init(args);

		int rank = MPI.COMM_WORLD.Rank();
		int size = MPI.COMM_WORLD.Size();

		int[] buffer = new int[BUFFER_LENGTH];
		if(rank == ROOT) {
			buffer[0] = 0;
			buffer[1] = 1;
			buffer[2] = 2;
			buffer[3] = 3;
			buffer[4] = 4;
		}
		
		MPI.COMM_WORLD.Bcast(buffer, 0, BUFFER_LENGTH, MPI.INT, ROOT);
		for (int i = 0; i < buffer.length; i++) {
			System.out.println("Rank(" + rank + ") " + buffer[i]); 
		}
		
		MPI.Finalize(); */
		new FindMaximumExercise().run(args);
	}

	private static void printMatrix(int[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	private static int[][] generateMatrix(int rows, int columns) {
		Random random = new Random();
		int[][] matrix = new int[rows][columns];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				int number = random.nextInt() % 10;
				if (number < 0) {
					number *= -1;
				}
				matrix[i][j] = number;
			}
		}
		return matrix;
	}

}
