import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

public class FA24_BCS_086 {

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Please provide number_of_Players initital_balance cut_Off_Value zakat_on_off");
            System.exit(1);
        }

        int numPlayers=Integer.parseInt(args[0]);
        int initialBalance=Integer.parseInt(args[1]);
        int cutOffValue=Integer.parseInt(args[2]);
        int zakat=Integer.parseInt(args[3]);
        int rounds=0;

        //Find the number of processors available to Java
        int numOfThreads=Runtime.getRuntime().availableProcessors();

        //Create a fixed thread pool with the specified number of threads
        ExecutorService executor=Executors.newFixedThreadPool(numOfThreads);

        System.out.println("\nThe machine that I am running on has "+numOfThreads+" cores\n");

        // Initialize the balances of each player
        int[] balances=new int[numPlayers];

        for (int i=0; i<numPlayers; i++) {
            balances[i]=initialBalance;
        }

        //Create a separate lock for each player to protect shared balances
        Object[] playerLocks=new Object[numPlayers];

        for (int i=0; i<numPlayers; i++) {
            playerLocks[i]=new Object();
        }

        // Initialize the random number generator
        Random rand=new Random();

        // Keep playing until only one player remains
        while (numPlayers>1) {

            //Save the number of players participating in the current round
            final int playersInRound=numPlayers;

            System.out.printf("\n\n-------------This is round# %d----------------\n\n",++rounds);

            //Create a list to store the Future objects of submitted game tasks
            List<Future<?>> futures=new ArrayList<>();

            //Submit one game task for each player
            for (int i=0; i<playersInRound; i++) {

                //Save the player index for this particular task
                final int player1=i;

                //Submit the game as a separate task to the thread pool
                Future<?> future=executor.submit(new Runnable() {

                    @Override
                    public void  run() {

                        //Get the worker thread currently executing this game
                        Thread currentThread=Thread.currentThread();

                        //Get the    name of the current worker thread
                        String threadName=currentThread.getName();

                        //Display the thread name and the time the game starts
                        System.out.println(threadName+" is going to start game at "+System.currentTimeMillis());

                        //Choose a random starting point for opponent selection
                        int start=rand.nextInt(playersInRound);

                        //Search for an eligible opponent
                        for (int offset=0; offset<playersInRound; offset++) {

                            int opponent=(start+offset)%playersInRound;

                            //A player cannot play against themselves
                            if (opponent==player1) {
                                continue;
                            }

                            //Acquire locks in a consistent order to avoid deadlocks
                            int firstLock=Math.min(player1,opponent);
                            int secondLock=Math.max(player1,opponent);

                            //Protect both players' balances while calculating and applying the bet
                            synchronized (playerLocks[firstLock]) {
                                synchronized (playerLocks[secondLock]) {

                                    //Skip eliminated players when Zakat is off
                                    if (zakat==0 && balances[player1]<cutOffValue) {
                                        return;
                                    }

                                    //The opponent must meet the cutoff value
                                    if (balances[opponent]<cutOffValue) {
                                        continue;
                                    }

                                    //Determine the bet using the current balances
                                    int betAmount=Math.min(balances[player1],balances[opponent])/5;

                                    //Flip the coin to determine the winner
                                    boolean coinFlip=rand.nextBoolean();

                                    if (coinFlip) {
                                        balances[player1]+=betAmount;
                                        balances[opponent]-=betAmount;
                                    } else {
                                        balances[player1]-=betAmount;
                                        balances[opponent]+=betAmount;
                                    }

                                    //Print the result of the game executed by this worker thread
                                    System.out.println("Bet result : player "+player1+" has "+balances[player1]+" vs "+opponent+" has "+balances[opponent]+"\n");

                                    //This task has completed its game
                                    return;
                                }
                            }
                        }
                    }
                });

                //Store the Future so the main thread can wait for this game to finish
                futures.add(future);

                //Add a small delay between task submissions
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    executor.shutdownNow();
                    return;
                }
            }

            //Wait for every submitted game in the current round to finish
            for (Future<?> future:futures) {
                try {
                    future.get();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    executor.shutdownNow();
                    return;
                } catch (ExecutionException e) {
                    System.out.println("Game task failed: "+e.getCause());
                }
            }
    
            // Apply Zakat if it is enabled
            if (zakat==1) {

                int rich,max,poor,calc_zakat;
                max=rich=poor=0;
                boolean found_poor=false;

                // Find the first player whose balance is at or below the cutoff
                for (int i=0; i<numPlayers; i++) {
                    if (balances[i]<=cutOffValue) {
                        found_poor=true;
                        poor=i;
                        break;
                    }
                }

                // If a poor player exists, find the richest player
                if (found_poor) {
                    max=0;

                    for (int i=0; i<numPlayers; i++) {
                        if (balances[i]>max) {
                            max=balances[i];
                            rich=i;
                        }
                    }

                    // Calculate 2.5% of the richest player's balance
                    calc_zakat=(int)(balances[rich]*0.025);

                    // Transfer Zakat from the richest player to the poor player
                    balances[poor]=balances[poor]+calc_zakat;
                    balances[rich]=balances[rich]-calc_zakat;

                    System.out.println("Zakat = "+calc_zakat+" was tranfered from Player "+rich+" to "+poor);
                }
            } else {

                int numRemainingPlayers=0;

                // Eliminate players below the cutoff value
                for (int i=0; i<numPlayers; i++) {
                    if (balances[i]>=cutOffValue) {
                        balances[numRemainingPlayers]=balances[i];
                        numRemainingPlayers++;
                    } else {
                        System.out.println("Player "+i+" eliminated with balance "+balances[i]);
                    }
                }

                // Update the number of players for the next round
                numPlayers=numRemainingPlayers;
            }
        }

        //Shut down the thread pool after all rounds are complete
        executor.shutdown();

        System.out.printf("\n\n----->Game finished after %d rounds<-----\n",rounds);
    }
}
