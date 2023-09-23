package zad3;

import javafx.application.Application;
import javafx.stage.Stage;

public class Controller
{
    public static class RunTaskListApplication extends Application             //Run the appllication
    {
        @Override
        public void start(Stage primaryStage) throws Exception
        {
            new View.TaskListWindow();
        }
        public static void main()
        {
            launch();
        }
    }
}
