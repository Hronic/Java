package zad1;

public class Letters extends Thread
{
    private Thread[] threads = null;
    private String[] arrThreadNames;
    private int intNumOfThreads;
    private boolean blnExit = false;

    public Letters(String strLetters)        //Constructor
    {
        this.intNumOfThreads = strLetters.length();             //get number of required threads
        this.arrThreadNames = new String[this.intNumOfThreads]; //array of thread names
        for(int i=0;i<this.intNumOfThreads;i++)                 //split letters into array
        {
            arrThreadNames[i]=String.valueOf(strLetters.charAt(i));
        }
        threads = new Thread[intNumOfThreads];                  //set array for threads
        for (int i = 0; i < this.intNumOfThreads; i++)  //https://www.geeksforgeeks.org/killing-threads-in-java/
        {
            String strThreadName = "Thread " + arrThreadNames[i];
            threads[i] = new Thread(
                    () -> {
                        vThreadRun(strThreadName.substring(strThreadName.length() - 1));
                        }, strThreadName);
        }
    }
    public void vStartThreads()
    {
        //change the flag, start all threads
        this.blnExit = false;
        for (int i = 0; i < this.intNumOfThreads; i++)
        {
            threads[i].start();
        }
    }
    public void vStopThreads()
    {
        //change the flag to exit
        this.blnExit = true;
    }
    public Thread[] getThreads()
    {
        //return threads
        return threads;
    }
    private void vThreadRun(String strLetter)
    {
        //All what is run in signle the thread
        while(!blnExit)
        {
            System.out.print(strLetter);  //print letter of thread
            try {
                //wait second
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}