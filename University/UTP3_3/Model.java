package zad3;

import java.util.Random;
import java.util.concurrent.*;

class CustomFutureTask
{
    public Future<Integer> fTask;
    private String strNameOfTask;
    private int intNumber1;
    private int intNumber2;
    private int intReqProcessTime;                          //Time required to finish the task
    private TaskState enumTaskState;                        //task state
    public CustomFutureTask(String strName)                 //constructor
    {
        //Randomize calculation parameters
        Random random = new Random();
        this.intNumber1= random.nextInt(1000);
        this.intNumber2= random.nextInt(1000);
        this.intReqProcessTime= random.nextInt(30);
        this.strNameOfTask=strName;
        this.enumTaskState= TaskState.CREATED;
        this.fTask = new SumTwoNumbers().calculate(this,this.intNumber1,this.intNumber2,this.intReqProcessTime);
    }
    public enum TaskState                                   //State types
    {
        //typy stanów
        CREATED,
        RUNNING,
        ABORTED,
        READY;
    }
    public void vChangeToRunning()
    {
        this.enumTaskState=TaskState.RUNNING;
    }
    public void vChangeToAborted()
    {
        if(this.enumTaskState!=TaskState.READY)             //You can't abort ready task
        {
            this.enumTaskState=TaskState.ABORTED;
        }
    }
    public void vChangeToDone()
    {
        this.enumTaskState=TaskState.READY;
    }
    public TaskState getState()
    {
        //return current state of the task
        return enumTaskState;
    }
    public class SumTwoNumbers
    {
        private ExecutorService executor = Executors.newSingleThreadExecutor();
        public Future<Integer> calculate(CustomFutureTask fTask,Integer intNumber1,Integer intNumber2,Integer intReqProcessTime)
        {
            fTask.vChangeToRunning();       //change state
            return executor.submit(() -> {
                Thread.sleep(intReqProcessTime*1000);                 //wait intReqProcessTime seconds
                fTask.vChangeToDone();       //change state
                return intNumber1 + intNumber2;
            });
        }
    }
    public String toString()
    {
        //returns name of the task
        return this.strNameOfTask;
    }
}
