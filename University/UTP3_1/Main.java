package zad1;

public class Main
{
    public static void main(String[] args) throws InterruptedException
    {
        Letters letters = new Letters("ABCD");
        for (Thread t : letters.getThreads()) System.out.println(t.getName());

        letters.vStartThreads();
        Thread.sleep(5000);         //odczekaj 5 sekund
        letters.vStopThreads();        //fragment, który kończy działanie kodów, wypisujących litery

        System.out.println("\nProgram skończył działanie");
    }
}