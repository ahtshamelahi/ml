// Provides the Executors class, which creates thread pools
// such as a cached thread pool.
import java.util.concurrent.Executors;

// Provides the ExecutorService interface for managing tasks
// and controlling the execution of worker threads.
import java.util.concurrent.ExecutorService;

// Provides time units such as SECONDS, MILLISECONDS, and MINUTES.
// Used to specify the timeout in awaitTermination().
import java.util.concurrent.TimeUnit;

// Provides the Random class for generating random numbers
// to fill the source matrix.
import java.util.Random;

public class MMWithThreadExecService 
{
    // static allows all threads to access the same source and result matrices.
    // Otherwise, instance variables would belong to individual objects.
    static int[][] sMat = new int[3][3]; // Source matrix
    static int[][] rMat = new int[3][3]; // Result matrix (sMat * sMat)

    public static void main(String[] arg)
    {
        Random rand = new Random();

        // Fill the source matrix with random numbers from 0 to 9.
        for (int i = 0; i < sMat.length; i++)
            for (int j = 0; j < sMat[i].length; j++)
                sMat[i][j] = rand.nextInt(10);

        // Print the source matrix.
        for (int i = 0; i < sMat.length; i++)
        {
            for (int j = 0; j < sMat[i].length; j++)
                System.out.print(sMat[i][j] + " ");
            System.out.println();
        }

        // Create an array that can store three MatrixThread task objects.
        // This does NOT create three threads yet.
        MatrixThread[] tArray = new MatrixThread[3];

        // Create three tasks.
        // Each task receives both matrices and a different row number (i).
        // Task 0 calculates row 0, task 1 calculates row 1,
        // and task 2 calculates row 2 of the result matrix.
        for (int i = 0; i < tArray.length; ++i)
            tArray[i] = new MatrixThread(sMat, rMat, i);

        // Create an ExecutorService to manage worker threads.
        // A cached thread pool can create new threads when needed
        // and reuse previously created idle threads.
        ExecutorService eS = Executors.newCachedThreadPool();

        // Submit all three tasks to the executor.
        // The executor runs each task's run() method on a worker thread.
        // The tasks can execute concurrently, so their work may overlap.
        for (int i = 0; i < tArray.length; ++i)
            eS.execute(tArray[i]);

        // Stop accepting new tasks, but allow submitted tasks to finish.
        // This does NOT immediately stop the running threads.
        eS.shutdown();

        try
        {
            // Make the main thread wait for the executor to terminate.
            // Wait for at most 5 seconds, not necessarily the full 5 seconds.
            // If all tasks finish, the method returns true.
            // If the timeout expires first, it returns false.
            // If the waiting is interrupted, it throws InterruptedException.
            eS.awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException e)
        {
            // Handle the exception if the main thread's waiting is interrupted.
            // Print information about the exception.
            e.printStackTrace();
        }

        // Print the result matrix after waiting.
        // Note: If the 5-second timeout expires before all tasks finish,
        // the result matrix might not be complete.
        System.out.println("\nThe result of MxM is ");

        for (int i = 0; i < tArray.length; ++i)
        {
            for (int j = 0; j < tArray.length; ++j)
                System.out.printf("%3d ", rMat[i][j]);

            System.out.println();
        }
    }
}