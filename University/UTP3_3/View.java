package zad3;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class View
{
    public static class TaskListWindow extends JFrame
    {
        JButton  btnAddTask = new JButton ("Dodaj zadanie");
        JButton  btnTaskState = new JButton ("Stan zadania");
        JButton  btnTaskCancel = new JButton ("Anuluj zadanie");
        JButton  btnTaskResult = new JButton ("Rezultat zadabnia");

        int intTaskCount=0;
        TaskListWindow()         //constructor
        {
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            //Layout
            JPanel panel =new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            //Data
            DefaultListModel listModel = new DefaultListModel();

            JList jTaskList= new JList(listModel);
            JScrollPane scrollPane = new JScrollPane();                             //define scrollbar for JList
            scrollPane.setViewportView(jTaskList);
            jTaskList.setLayoutOrientation(JList.VERTICAL);                         //set Vertical scrollbar
            jTaskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);        //can select only one at the time

            //Action per click -> add task
            btnAddTask.addActionListener(e ->
            {
                CustomFutureTask test1 = new CustomFutureTask("Zadanie " + intTaskCount);
                listModel.addElement(test1);
                intTaskCount=intTaskCount+1;

            });
            //Task state
            btnTaskState.addActionListener(e ->
            {
                System.out.println("Task state");
                int index = jTaskList.getSelectedIndex();
                if(index==-1)
                {
                    msgGeneralMessage frame = new msgGeneralMessage("Nie zaznaczyles zadnego zadania.\nZaznacz zadanie i kliknij ponownie operacje");
                    frame.setVisible(true);
                } else {
                    futureMessageState frame = new futureMessageState((CustomFutureTask)listModel.get(index));
                }
            });
            btnTaskCancel.addActionListener(e ->
            {
                System.out.println("Task cancel");
                int index = jTaskList.getSelectedIndex();
                if(index==-1)
                {
                    msgGeneralMessage frame = new msgGeneralMessage("Nie zaznaczyles zadnego zadania.\nZaznacz zadanie i kliknij ponownie operacje");
                    frame.setVisible(true);
                } else {
                    futureMessageCancel frame = new futureMessageCancel((CustomFutureTask)listModel.get(index));
                }
            });
            btnTaskResult.addActionListener(e ->
            {
                System.out.println("Task result");
                int index = jTaskList.getSelectedIndex();
                if(index==-1)
                {
                    msgGeneralMessage frame = new msgGeneralMessage("Nie zaznaczyles zadnego zadania.\nZaznacz zadanie i kliknij ponownie operacje");
                    frame.setVisible(true);
                } else {
                    futureMessageResult frame = new futureMessageResult((CustomFutureTask)listModel.get(index));
                }
            });

            //Add buttons
            panel.add(btnAddTask);
            panel.add(btnTaskState);
            panel.add(btnTaskCancel);
            panel.add(btnTaskResult);
            panel.add(scrollPane);       //Add Jlist in JScrollPane
            this.add(panel);
            this.setSize(300,400);
            this.show();
        }
    }
    public static class msgGeneralMessage extends JFrame
    {
        JFrame f;
        msgGeneralMessage(String strMessage)
        {
            f=new JFrame();
            JOptionPane.showMessageDialog(f,strMessage);
        }
        public void main(String strMessage)
        {
            new msgGeneralMessage(strMessage);
        }
    }
    public static class futureMessageState extends JFrame
    {
        JFrame f;
        futureMessageState(CustomFutureTask ft)
        {
            f=new JFrame();
            JOptionPane.showMessageDialog(f,"Stan zaznaczonego zadania: " + ft.getState());
        }
    }
    public static class futureMessageCancel extends JFrame
    {
        JFrame f;
        futureMessageCancel(CustomFutureTask ft)
        {
            f=new JFrame();
            if(ft.getState()== CustomFutureTask.TaskState.READY)
            {
                JOptionPane.showMessageDialog(f,"Zadanie zostało już wykonane i nie może zostać anulowane!");
            } else {
                JOptionPane.showMessageDialog(f,"Zadanie zostało anulowane!");
                ft.fTask.cancel(true);
                ft.vChangeToAborted();
            }
        }
    }
    public static class futureMessageResult extends JFrame
    {
        JFrame f;
        futureMessageResult(CustomFutureTask ft)
        {
            f=new JFrame();
            if(ft.fTask.isCancelled())
            {
                JOptionPane.showMessageDialog(f,"Zadanie zostało anulowane!");
            }else  if(ft.fTask.isDone())
            {
                try {
                    JOptionPane.showMessageDialog(f,"Rezultat zadania: " + ft.fTask.get());
                } catch(ExecutionException a)
                {}catch(InterruptedException a)
                {}
            } else{
                JOptionPane.showMessageDialog(f,"Zadanie nie zostalo jeszcze wykonane, sprobuj pozniej.");
            }
        }
    }
}
