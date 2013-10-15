package sk.ursus.paralelneprogramovanie.firstassignment;
import sk.ursus.paralelneprogramovanie.Exercise;
import mpi.MPI;


public class FindMaximumExercise implements Exercise {
	
	public static final int ROOT = 0;
	private static final int BUFFER_LENGTH = 5;

	@Override
	public void run(String[] args) {
		MPI.Init(args);

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
		
		MPI.Finalize();
	}

}
