/**
 *
 *  @author Bajorek Mikołaj S24056
 *
 */

package zad2;

public class Main
{
  public static void main(String[] args) throws InterruptedException
  {
    StringTask task = new StringTask("A", 70000);      //default
    System.out.println("Task " + task.getState());          //default
    task.start();                                           //default
    if (args.length > 0 && args[0].equals("abort"))
    {
        new Thread(() -> {
            try{
                Thread.sleep(1000);      //wait a second
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            task.abort();                       //abort current thread
          }).start();                           //start new thread
    }
    while (!task.isDone())
    {
      Thread.sleep(500);
      switch(task.getState())
      {
          case RUNNING: System.out.print("R."); break;
          case ABORTED: System.out.println(" ... aborted."); break;
          case READY: System.out.println(" ... ready."); break;
          default: System.out.println("unknown state");
      }
    }
    System.out.println("Task " + task.getState());
    System.out.println(task.getResult().length());
  }
}

