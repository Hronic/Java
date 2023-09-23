import javafx.application.Application;
//JavaIo
import java.io.FileInputStream;

//JavaFX
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.collections.ObservableList;
//Nodes
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.ImageView;

//Layouts
import javafx.collections.FXCollections;

//TEST
import javafx.scene.shape.Circle;
import javafx.geometry.Bounds;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

//Forms and windows
public class View
{
    public static class MainMenuWindow extends Stage
    {
        //Create buttons
        Button btnPlay = new Button("New Game - nowa gra");             //Creating the play button
        Button btnScore = new Button("High Scores - tabela wyników ");  //Creating the stop button
        Button btnExit = new Button("Exit – wyjście");                  //Creating the stop button

        //Path to picutres for buttons -> if available
        String strNewGamePicPath=System.getProperty("user.dir") + "\\graphics\\NewGame.jpg";
        String strHighScorePicPath=System.getProperty("user.dir") + "\\graphics\\HighScore.png";
        String strExitPicPath=System.getProperty("user.dir") + "\\graphics\\Exit.png";

        //Manage layout of the buttons
        Font font = Font.font("Times New Roman", FontWeight.BOLD, 12);  //set font in the button -> Type,weight,height
        VBox vBox = new VBox();                                              //Instantiating the VBox class

        MainMenuWindow()            //Constructor
        {
            Stage ThisStage = this;

            //Add icons to buttons
            Controller.vAddIconForButton(strNewGamePicPath,btnPlay);       //Add icon from the path to the button
            Controller.vAddIconForButton(strHighScorePicPath,btnScore);    //Add icon from the path to the button
            Controller.vAddIconForButton(strExitPicPath,btnExit);          //Add icon from the path to the button

            //Set font of buttons
            btnPlay.setFont(font);
            btnScore.setFont(font);
            btnExit.setFont(font);

            //Actions on buttons
            //btnPlay.setOnAction(t -> new DifficultyLevelWindow());   //Alternative solution
            btnPlay.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent event)
                {
                    Controller.vOpenCloseWindow(ThisStage,"DifficultyLevelWindow");
                }
            });
            btnScore.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent event)
                {
                    Controller.vOpenCloseWindow(ThisStage,"HighScoreWindow");
                }
            });
            btnExit.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent event)
                {
                    ThisStage.close();
                }
            });

            //Layout
            vBox.setAlignment(Pos.BASELINE_CENTER);  //allign to center all buttons
            //vBox.setSpacing(0);                    //Setting the space between the nodes of a VBox pane -> no nee

            //Setting the margin to the nodes, https://docs.oracle.com/javase/8/javafx/api/javafx/geometry/Insets.html
            vBox.setMargin(btnPlay, new Insets(1, 1, 1, 1));        //offset top, right, bottom, left
            vBox.setMargin(btnScore, new Insets(1, 1, 1, 1));
            vBox.setMargin(btnExit, new Insets(1, 1, 1, 1));

            ObservableList list = vBox.getChildren();           //retrieving the observable list of the VBox
            list.addAll(btnPlay, btnScore, btnExit);            //adding all the nodes/buttons to the observable list
            Scene scene = new Scene(vBox,320, 140);      //creating a scene object with specific size: width, height

            this.setTitle("Main menu - menu główne");   //setting title to the Stage
            this.setScene(scene);
            this.show();
        }
    }
    public static class DifficultyLevelWindow extends Stage
    {
        //define buttons
        Button btnPlayEasy = new Button("Easy - łatwy");             //Creating the play button
        Button btnPlayMedium = new Button("Medium - średni");  //Creating the stop button
        Button btnPlayHard = new Button("Hard - trudny");  //Creating the stop button
        Button btnReturnMainMenu = new Button("Return to main menu");                  //Creating the stop button

        GridPane gridPane = new GridPane();     //define a Grid Pane,

        DifficultyLevelWindow()
        {
            Stage ThisStage = this;

            Label lblLabel = new Label("Please select difficulty or return to main menu");

            //Set size of buttons
            int intWidth=170;
            Controller.vSetSizeOfButton(btnPlayEasy,intWidth,50);
            Controller.vSetSizeOfButton(btnPlayMedium,intWidth,50);
            Controller.vSetSizeOfButton(btnPlayHard,intWidth,50);
            Controller.vSetSizeOfButton(btnReturnMainMenu,intWidth,50);
            btnReturnMainMenu.setWrapText(true);

            //Actions on buttons
            btnPlayEasy.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent event)
                {
                    Model.Global.intDifficulty=0;
                    Controller.vOpenCloseWindow(ThisStage,"GameMapWindow");
                }
            });
            btnPlayMedium.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent event)
                {
                    Model.Global.intDifficulty=1;
                    Controller.vOpenCloseWindow(ThisStage,"GameMapWindow");
                }
            });
            btnPlayHard.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent event)
                {
                    Model.Global.intDifficulty=2;
                    Controller.vOpenCloseWindow(ThisStage,"GameMapWindow");
                }
            });
            btnReturnMainMenu.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent event)
                {
                    Controller.vOpenCloseWindow(ThisStage,"MainMenuWindow");
                }
            });

            //Setting size, setting the padding
            gridPane.setMinSize(400, 180);
            gridPane.setPadding(new Insets(10, 10, 10, 10));

            //Setting the vertical and horizontal gaps between the columns
            gridPane.setVgap(5);
            gridPane.setHgap(5);
            gridPane.setAlignment(Pos.CENTER);

            //Arranging all the nodes in the grid (column, row)-> https://docs.oracle.com/javase/8/javafx/api/javafx/scene/layout/GridPane.html
            gridPane.add(lblLabel, 0, 0);            //set
            gridPane.add(btnPlayEasy, 0, 1);            //set
            gridPane.add(btnPlayMedium, 0, 2);
            gridPane.add(btnPlayHard, 0, 3);
            gridPane.add(btnReturnMainMenu, 0, 4);

            //Combine & set & show
            Scene scene = new Scene(gridPane,290,260);  //creating a scene object with specific size: width, height
            this.setTitle("Choose difficulty level");         //Setting title to the Stage
            this.setScene(scene);                             //Adding scene to the stage
            this.show();                                      //Displaying the contents of the stage
        }
    }
    public static class HighScoreWindow extends Stage
    {
        //doc for listview: https://docs.oracle.com/javafx/2/ui_controls/list-view.htm
        Button btnReturnMainMenu = new Button("Return to main menu – powrót do menu głównego");                  //Creating the stop button\
        Label lblInfo = new Label("Return to main or view the HighScore");
        VBox vBox = new VBox();
        HighScoreWindow(String strHighScorePath)
        {
            Stage ThisStage = this;
            btnReturnMainMenu.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent event)
                {
                    Controller.vOpenCloseWindow(ThisStage,"MainMenuWindow");
                }
            });

            //prepare list with results -> load from file, insert into array, arrange array into ListView
            ListView<String> list = new ListView<String>();
            String[] arrHighScore=Controller.arrExtractStringArrayWithScoresFromFile(strHighScorePath); //extract data from the file
            ObservableList<String> data = FXCollections.observableArrayList();
            for(int i=0;i<arrHighScore.length;i++)
            {
                data.add(arrHighScore[i]);
            }

            //Assign the list
            vBox.getChildren().addAll(lblInfo, btnReturnMainMenu,list);
            VBox.setVgrow(list, Priority.ALWAYS);
            list.setItems(data);

            //Set the scene and stage
            Scene scene = new Scene(vBox, 300, 250);
            ThisStage.setTitle("HighScore!");
            ThisStage.setScene(scene);
            ThisStage.show();
        }
    }
    public static class GameSummaryWindow extends Stage
    {
        //Buttons for the window
        Button btnSaveScore = new Button("Save score - zapisz wynik");                    //Creating the play button
        Button btnExitToMainMenu = new Button("Exit - wróć do menu");     //Creating the stop button

        //Path to pictures
        String strWinPicPath = System.getProperty("user.dir") + "\\graphics\\Win.jpg";
        String strLosePicPath = System.getProperty("user.dir") + "\\graphics\\Lose.jpg";
        String strSavePicPath=System.getProperty("user.dir") + "\\graphics\\Save.png";
        String strExitPicPath=System.getProperty("user.dir") + "\\graphics\\Exit.png";

        //Labels with for the window
        Label lblScore = new Label("Your score:");
        Label lblTimePlayed = new Label("Time in game:");
        Label lblKilledWhite = new Label("Killed White ducks:");
        Label lblKilledRed = new Label("Killed red ducks:");
        Label lblKilledBlack = new Label("Killed black ducks:");

        //Labels with values for the window
        Label lblScoreValue = new Label(String.valueOf(Model.Global.lngScore));
        Label lblTimePlayedValue = new Label(String.valueOf(Model.Global.lngPlayedTime));
        Label lblKilledWhiteValue = new Label(String.valueOf(Model.Global.intKilledWhiteDucks));
        Label lblKilledRedValue = new Label(String.valueOf(Model.Global.intKilledRedDucks));
        Label lblKilledBlackValue = new Label(String.valueOf(Model.Global.intKilledBlackDucks));

        //Window presented after game finished -> win or lose
        GameSummaryWindow(boolean blnWin)
        {
            Stage ThisStage = this;

            //Set actions for buttons
            btnSaveScore.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent event)
                {
                    Controller.vOpenCloseWindow(ThisStage,"SaveYourScoreWindow");
                }
            });
            btnExitToMainMenu.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent event)
                {
                    Controller.vOpenCloseWindow(ThisStage,"MainMenuWindow");
                }
            });

            //Used layouts
            VBox MainVBoxLayout = new VBox();
            HBox SubHBoxLvl1Layout = new HBox();
            GridPane SubGridPaneLvl2Layout = new GridPane();
            VBox SubVBoxLvl2Layout = new VBox();

            //Set SubGridPaneLvl2Layout -> Left (Column, Row)
            SubGridPaneLvl2Layout.setAlignment(Pos.CENTER_LEFT);
            SubGridPaneLvl2Layout.setVgap(5);
            SubGridPaneLvl2Layout.setHgap(10);
            SubGridPaneLvl2Layout.add(lblScore, 0, 0);
            SubGridPaneLvl2Layout.add(lblTimePlayed, 0, 1);
            SubGridPaneLvl2Layout.add(lblKilledWhite, 0, 3);
            SubGridPaneLvl2Layout.add(lblKilledRed, 0, 4);
            SubGridPaneLvl2Layout.add(lblKilledBlack, 0, 7);
            SubGridPaneLvl2Layout.add(lblScoreValue, 1, 0);
            SubGridPaneLvl2Layout.add(lblTimePlayedValue, 1, 1);
            SubGridPaneLvl2Layout.add(lblKilledWhiteValue, 1, 3);
            SubGridPaneLvl2Layout.add(lblKilledRedValue, 1, 4);
            SubGridPaneLvl2Layout.add(lblKilledBlackValue, 1, 7);

            //Set SubVBoxLvl2Layout -> Right
            SubVBoxLvl2Layout.setAlignment(Pos.CENTER_RIGHT);
            SubVBoxLvl2Layout.setSpacing(10);
            Controller.vAddIconForButton(strSavePicPath,btnSaveScore);          //Add icon from the path to the button
            Controller.vAddIconForButton(strExitPicPath,btnExitToMainMenu);       //Add icon from the path to the button
            SubVBoxLvl2Layout.getChildren().add(btnSaveScore);
            SubVBoxLvl2Layout.getChildren().add(btnExitToMainMenu);

            //Set SubHBoxLvl1Layout
            SubHBoxLvl1Layout.setAlignment(Pos.CENTER);
            SubHBoxLvl1Layout.getChildren().add(SubGridPaneLvl2Layout);
            SubHBoxLvl1Layout.getChildren().add(SubVBoxLvl2Layout);

            //Set MainVBoxLayout
            ImageView imageView = new ImageView();
            if(blnWin==true)
            {
                Controller.vSetImage(strWinPicPath,imageView);
            }else{
                Controller.vSetImage(strLosePicPath,imageView);
            }
            MainVBoxLayout.setAlignment(Pos.CENTER);
            MainVBoxLayout.getChildren().add(imageView); //Add image to main layout
            MainVBoxLayout.getChildren().add(SubHBoxLvl1Layout);        //assign graphic for layout 2

            //Set final stage
            Scene scene = new Scene(MainVBoxLayout, 624, 550);
            ThisStage.setScene(scene);
            ThisStage.setTitle("Game summary");   //setting title to the Stage
            ThisStage.show();
        }
    }
    public static class SaveYourScoreWindow extends Stage
    {
        //Buttons for the window
        Button btnSaveScore = new Button("Save score - zapisz wynik");                    //Creating the play button
        Button btnExitToMainMenu = new Button("Exit - wróć do menu");     //Creating the stop button

        //Labels
        Label lblHello = new Label("Insert your nickname and save the score!");
        Label lblScoreInfo = new Label("Your score is:");
        Label lblScoreValue = new Label(String.valueOf(Model.Global.lngScore));
        Label lblProvideNickname= new Label("Please sign for your score! \nPlease use characters a-Z and numbers, no special signs. \nMaximum number of characters is 15.");
        Label lblUserNickname = new Label("Enter your nickname:");

        //Path to graphics
        String strSavePicPath=System.getProperty("user.dir") + "\\graphics\\Save.png";
        String strExitPicPath=System.getProperty("user.dir") + "\\graphics\\Exit.png";

        //Textfields
        TextField txtInsertNickname = new TextField();

        //Manage layout of the buttons
        Font font = Font.font("Times New Roman", FontWeight.BOLD, 12);  //set font in the button -> Type,weight,height

        //Used layouts
        VBox MainVBoxLayout = new VBox();
        FlowPane FlowPaneLayoutR2 = new FlowPane();
        FlowPane FlowPaneLayoutR4 = new FlowPane();
        FlowPane FlowPaneLayoutR5 = new FlowPane();

        SaveYourScoreWindow()       //Constructor of Stage
        {
            Stage ThisStage = this;
            lblProvideNickname.setTextAlignment(TextAlignment.CENTER);            //Allign text of the
            txtInsertNickname.setPromptText("Enter nickname.");

            //Define actions for buttons
            btnSaveScore.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent event)
                {
                    System.out.println("- User clicked button to save the score.");
                    Controller.vSaveTheScore(txtInsertNickname,lblScoreValue.getText(),ThisStage); //Save the score operation
                }
            });
            btnExitToMainMenu.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent event)
                {
                    Controller.vOpenCloseWindow(ThisStage,"MainMenuWindow");
                }
            });

            //Sublayout, row 2
            FlowPaneLayoutR2.setAlignment(Pos.CENTER);                      //Alignment of nodes to center
            FlowPaneLayoutR2.setHgap(25);                                   //Horizontal gap between nodes
            FlowPaneLayoutR2.getChildren().add(lblScoreInfo);
            FlowPaneLayoutR2.getChildren().add(lblScoreValue);

            //Sublayout, row 4
            FlowPaneLayoutR4.setAlignment(Pos.CENTER);
            FlowPaneLayoutR2.setHgap(15);
            FlowPaneLayoutR4.getChildren().add(lblUserNickname);
            FlowPaneLayoutR4.getChildren().add(txtInsertNickname);

            //Sublayout, row 5
            FlowPaneLayoutR5.setAlignment(Pos.CENTER);
            Controller.vAddIconForButton(strSavePicPath,btnSaveScore);          //Add icon from the path to the button
            Controller.vAddIconForButton(strExitPicPath,btnExitToMainMenu);       //Add icon from the path to the button
            FlowPaneLayoutR5.getChildren().add(btnSaveScore);
            FlowPaneLayoutR5.getChildren().add(btnExitToMainMenu);

            //Complete main layout
            MainVBoxLayout.setSpacing(20);
            MainVBoxLayout.setAlignment(Pos.CENTER);
            MainVBoxLayout.getChildren().add(lblHello);
            MainVBoxLayout.getChildren().add(FlowPaneLayoutR2);
            MainVBoxLayout.getChildren().add(lblProvideNickname);
            MainVBoxLayout.getChildren().add(FlowPaneLayoutR4);
            MainVBoxLayout.getChildren().add(FlowPaneLayoutR5);

            Scene scene = new Scene(MainVBoxLayout, 350, 250);
            this.setTitle("Save your score - zapisz wynik");   //setting title to the Stage
            this.setScene(scene);
            this.show();
        }
    }
    public static class YesNoMessageWindow
    {
        private int intAnswer;                            //answer of the user
        Label lblQuestion;                                  //label with question
        Button btnYes = new Button("Yes");              //0
        Button btnNo = new Button("No");                //1
        Button btnCancel = new Button("Cancel");        //2

        String strYesPicPath= System.getProperty("user.dir") + "\\graphics\\Yes.jpg";
        String strNoPicPath= System.getProperty("user.dir") + "\\graphics\\No.png";
        String strCancelPicPath= System.getProperty("user.dir") + "\\graphics\\Cancel.png";

        int intButtonWidth=90;
        int intButtonHeight=40;

        public YesNoMessageWindow(String strQuestion) 					//constructor
        {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);	//Stage modality determines if the window representing the Stage will block other windows opened by the same JavaFX application.
            lblQuestion = new Label(strQuestion);                   //define text of label
            lblQuestion.setTextAlignment(TextAlignment.CENTER);     //allign text to middle

            //Adjust buttons size, add icons
            Controller.vSetSizeOfButton(btnYes,intButtonWidth,intButtonHeight);
            Controller.vSetSizeOfButton(btnNo,intButtonWidth,intButtonHeight);
            Controller.vSetSizeOfButton(btnCancel,intButtonWidth,intButtonHeight);
            Controller.vAddIconForButton(strYesPicPath,btnYes);
            Controller.vAddIconForButton(strNoPicPath,btnNo);
            Controller.vAddIconForButton(strCancelPicPath,btnCancel);

            //Set sublayout
            HBox SubLayout2R = new HBox();
            SubLayout2R.setAlignment(Pos.CENTER);
            SubLayout2R.getChildren().add(btnYes);
            SubLayout2R.getChildren().add(btnNo);
            SubLayout2R.getChildren().add(btnCancel);
            SubLayout2R.setSpacing(10);

            //Set main layout
            VBox MainVboxLayout = new VBox();
            MainVboxLayout.setAlignment(Pos.CENTER);
            MainVboxLayout.getChildren().add(lblQuestion);
            MainVboxLayout.getChildren().add(SubLayout2R);

            //Define actions for buttons
            btnYes.setOnAction(e -> {
                this.intAnswer=0;
                stage.close();
            });
            btnNo.setOnAction(e -> {
                this.intAnswer=1;
                stage.close();
            });
            btnCancel.setOnAction(e -> {
                this.intAnswer=2;
                stage.close();
            });

            Scene scene = new Scene(MainVboxLayout, 340, 125);
            stage.setTitle("Yes/No question.");   //setting title to the Stage
            stage.setScene(scene);
            stage.showAndWait();
        }
        public int intGetAnswer()
        {
            //Returns answer of the user
            return this.intAnswer;
        }
    }
    public static class InformationOkWindow
    {
        Label lblQuestion;                                  //label with question
        Button btnYes = new Button("Ok");
        String strYesPicPath= System.getProperty("user.dir") + "\\graphics\\OkInfo.jpg";

        int intButtonWidth=80;
        int intButtonHeight=40;

        public InformationOkWindow(String strQuestion) 					//constructor
        {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);	//Stage modality determines if the window representing the Stage will block other windows opened by the same JavaFX application.
            lblQuestion = new Label(strQuestion);                   //define text of label
            lblQuestion.setTextAlignment(TextAlignment.CENTER);     //allign text to middle

            //Adjust buttons size, add icons
            Controller.vSetSizeOfButton(btnYes,intButtonWidth,intButtonHeight);
            Controller.vAddIconForButton(strYesPicPath,btnYes);

            //Set sublayout
            HBox SubLayout2R = new HBox();
            SubLayout2R.setAlignment(Pos.CENTER);
            SubLayout2R.getChildren().add(btnYes);
            SubLayout2R.setSpacing(10);

            //Set main layout
            VBox MainVboxLayout = new VBox();
            MainVboxLayout.getChildren().add(lblQuestion);
            MainVboxLayout.getChildren().add(SubLayout2R);

            //Define actions for buttons
            btnYes.setOnAction(e -> {
                stage.close();
            });

            //Finalize
            Scene scene = new Scene(MainVboxLayout, 240, 85);
            stage.setTitle("Information.");   //setting title to the Stage
            stage.setScene(scene);
            stage.showAndWait();
        }
    }
    public static class GameMapWindow extends Stage
    {
        //Buttons for the window
        Button btnExitToMainMenu = new Button("End game - tabela wyników");     //Creating the stop button
        //Labels
        Label lblTimer=new Label("");
        Label lblLifes=new Label("");
        Label lblPoints=new Label("");

        //Sizes
        private int intWidth=165;
        private int intHeight=30;

        //Timers
        Timer BackgroundTimer;
        Timeline timeline;

        GameMapWindow()                                 //constructor
        {
            //Stage + pane
            Stage ThisStage = this;
            Pane GameMap = new Pane();
            GameMap.setBackground(new Background(new BackgroundFill(Color.rgb(135,206,235), CornerRadii.EMPTY, Insets.EMPTY)));  //set blue background
            Scene scene = new Scene(GameMap, 1200, 700); //Width & height

            //Bounds of the map
            double dblXBoundOfMap=GameMap.getBoundsInLocal().getMaxX();
            double dblYBoundOfMap=GameMap.getBoundsInLocal().getMaxY();

            //Define buttons and labels
            btnExitToMainMenu.relocate(5,5);
            Controller.vSetSizeOfButton(btnExitToMainMenu,intWidth,intHeight);
            lblTimer.relocate(intWidth+10,5);
            lblTimer.setPrefSize(intWidth,intHeight);
            lblLifes.relocate(intWidth+850,5);
            lblLifes.setPrefSize(intWidth,intHeight);
            lblPoints.relocate(intWidth+900,5);
            lblPoints.setPrefSize(intWidth,intHeight);
            //Add buttons to stage
            GameMap.getChildren().add(lblTimer);
            GameMap.getChildren().add(btnExitToMainMenu);
            GameMap.getChildren().add(lblLifes);
            GameMap.getChildren().add(lblPoints);

            Group group = new Group();
            Group CloudGroup = new Group();
            Controller.vPrepareTheStage(CloudGroup,GameMap);
            GameMap.getChildren().add(group);
            GameMap.getChildren().add(CloudGroup);
            CloudGroup.toFront();

            //Dictionary with ducks
            HashMap<Integer, Model.Duck> dicDucks = new HashMap<Integer, Model.Duck>();
            timeline = new Timeline(new KeyFrame(Duration.millis(20),new EventHandler<ActionEvent>() {
            double dx = 7+(Model.Global.intDifficulty)*2; //Step on x or velocity
            double dy = 0; //Step on y
            @Override
            public void handle(ActionEvent t) {
                if(dicDucks.size()>0)
                {
                    HashMap<Integer, Model.Duck> dicDeleteDuck = new HashMap<Integer, Model.Duck>();
                    for (int intKey : dicDucks.keySet())
                    {
                        Model.Duck objDuck = dicDucks.get(intKey);
                        objDuck.setLayoutX(objDuck.getLayoutX() + (objDuck.Right() ? -dx : +dx));
                        objDuck.setLayoutY(objDuck.getLayoutY() + dy);

                        if((objDuck.getLayoutX() <= 0) || (objDuck.getLayoutX() >= dblXBoundOfMap))
                        {
                            dicDeleteDuck.put(intKey,dicDucks.get(intKey));
                            objDuck.Escaped();                          //set status on escaped
                            group.getChildren().remove(objDuck);        //throw away from the group
                        }
                        if((objDuck.getLayoutY() <= 0) || (objDuck.getLayoutY() >= dblYBoundOfMap))
                        {
                            dicDeleteDuck.put(intKey,dicDucks.get(intKey));
                            objDuck.Escaped();
                            group.getChildren().remove(objDuck);
                        }
                    }
                    if(dicDeleteDuck.size()>0)
                    {
                        for(int intKey : dicDeleteDuck.keySet())
                        {
                            dicDucks.remove(intKey);                    //delete from dic
                        }
                    }
                }
                if(Model.Global.blnEndGame==true)
                {
                    timeline.stop();
                }
                CloudGroup.toFront();
            }
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();

            //Additional thread to run operations in background
            BackgroundTimer = new Timer();
            BackgroundTimer.scheduleAtFixedRate(new TimerTask()
            {
                @Override
                public void run() {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Controller.vBackgroundperations(group,dicDucks,lblTimer,lblLifes,lblPoints,dblXBoundOfMap,dblYBoundOfMap);  //Operations which are run in background
                            if(Model.Global.blnEndGame==true)
                            {
                                BackgroundTimer.cancel();
                                Controller.vGoToGameSummary(timeline,BackgroundTimer,ThisStage);
                            }
                        }
                    });
                }
            }, 0, 1000);    //run every second after 0 delay from the start

            //Define exit button
            btnExitToMainMenu.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent event)
                {
                    Controller.vGoToGameSummary(timeline,BackgroundTimer,ThisStage);
                }
            });


            //Shortcut definition
            KeyCombination kcQuitGame = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN,KeyCombination.SHIFT_DOWN);   //define shortcut Q + CTRL + SHIFT
            scene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {                                             //Define event after shortcut press
                @Override
                public void handle(KeyEvent event) {
                    if (kcQuitGame.match(event))
                    {
                        timeline.stop();                                                                //Stop Timeline
                        BackgroundTimer.cancel();                                                       //Stop thread
                        System.out.println("- User pressed CTRL+SHIFT+Q -> Game will close.");
                        Controller.vOpenCloseWindow(ThisStage,"MainMenuWindow");        //Go back to main menu after shortcut pressed
                    }
                }
            });
            ThisStage.setScene(scene);
            ThisStage.setTitle("Duck shooter!");   //setting title to the Stage
            ThisStage.show();
        }
    }
}