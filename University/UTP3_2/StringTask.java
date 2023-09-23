package zad2;

public class StringTask implements Runnable //wymagany interfejs
{
    private TaskState enumTaskState;        //task state
    private String strLetter;
    private int intRepeatCount;
    private String strConcatResult;
    private boolean blnDone;
    private Thread newThread;

    public StringTask(String strLetter, int intRepeatCount)              //Constructor
    {
        this.intRepeatCount = intRepeatCount;
        this.strLetter = strLetter;
        this.strConcatResult="";
        this.blnDone = false;                   //default value -> not done
        this.enumTaskState = TaskState.CREATED; //default -> created
        this.newThread = new Thread(() -> {
            run();
        });
    }
    public enum TaskState                           //State types
    {
        //typy stanów
        CREATED,
        RUNNING,
        ABORTED,
        READY;
    }
    public String getResult()
    {
        //zwracającą wynik konkatenacji
        return this.strConcatResult;
    }
    public TaskState getState()
    {
        //zwracającą stan zadania
        return this.enumTaskState;
    }
    public void start()
    {
        //uruchamiającą zadanie w odrębnym watku
        this.newThread.start();
        this.enumTaskState = TaskState.RUNNING; //change state
    }
    public void abort()
    {
        //przerywającą wykonanie kodzu zadania i działanie watku
        this.blnDone = true;
        this.enumTaskState = TaskState.ABORTED;
    }
    public boolean isDone()
    {
        //return of status
        return this.blnDone;
    }
    @Override
    public void run()
    {
        int i = 0;
        while ((i < this.intRepeatCount) & (!isDone()))
        {
            i=i+1;                                  //increase iterator
            this.strConcatResult =this.strConcatResult + strLetter;   //concat
        }
        if (i==this.intRepeatCount)                 //everything was concated
        {
            this.blnDone = true;
            this.enumTaskState = TaskState.READY;
        }
    }
}