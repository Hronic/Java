import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.*;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;



//Methods
public class Controller
{
    public static class RunGameApplicationn extends Application             //Run the appllication
    {
        @Override
        public void start(Stage primaryStage) throws Exception
        {
            new View.MainMenuWindow();
        }
        public static void main()
        {
            launch();
        }
    }
    public static void vClearGlobalVariables()
    {
        //Method to clear all global variables
        Model.Global.lngGameStart=System.nanoTime();
        Model.Global.lngCurrentTime=0;
        Model.Global.lngPlayedTime=0;
        Model.Global.blnGameStatus=false;
        Model.Global.intLifes=10;       //10 lifes from default
        Model.Global.lngScore=0;
        Model.Global.intKilledWhiteDucks=0;
        Model.Global.intKilledRedDucks=0;
        Model.Global.intKilledBlackDucks=0;
        Model.Global.intDucksSpawned=0;
        Model.Global.blnEndGame=false;
    }
    public static void vOpenCloseWindow(Stage stageToClose, String strWindowToOpen)
    {
        //Method which closes current stage and opens new one
        stageToClose.close();
        switch (strWindowToOpen)
        {
            case "MainMenuWindow":
                new View.MainMenuWindow();
                break;
            case "DifficultyLevelWindow":
                new View.DifficultyLevelWindow();
                break;
            case "HighScoreWindow":
                String strPath=strGetPathOfTheHighScoreFile(); //Get path to the highscore file
                new View.HighScoreWindow(strPath);
                break;
            case "GameMapWindow":
                Controller.vClearGlobalVariables();     //Once the game starts, clear all global variables
                new View.GameMapWindow();
                break;
            case "SaveYourScoreWindow":
                new View.SaveYourScoreWindow();
                break;
            case "GameSummaryWindow":
                new View.GameSummaryWindow(Model.Global.blnGameStatus);
                break;
        }
    }
    public static boolean blnFileExists(String strFilePath)
    {
        //Checks if file under specified path exists
        File f = new File(strFilePath);
        if(f.exists() && !f.isDirectory())
        {
            return true;
        } else {
            return false;
        }
    }
    public static void vAddIconForButton(String strPicturePath, Button btnModify)
    {
        //Method checks if picture under given path exists, if so add it to button
        if(blnFileExists(strPicturePath))               //if picture exists add it to the button
        {
            Image imgTemp;
            ImageView imgViewReturn;
            imgTemp = new Image(strPicturePath);        //must be full path to the picture
            imgViewReturn = new ImageView(imgTemp);
            imgViewReturn.setFitHeight(30);
            imgViewReturn.setPreserveRatio(true);
            btnModify.setGraphic(imgViewReturn);
        }
    }
    public static void vSetSizeOfButton(Button btnAdjustedButton, int intWidth, int intHeight)
    {
        //Set size and heigh of given button
        btnAdjustedButton.setMinWidth(intWidth);
        btnAdjustedButton.setMaxWidth(intWidth);
        btnAdjustedButton.setPrefWidth(intWidth);

        btnAdjustedButton.setMinHeight(intHeight);
        btnAdjustedButton.setMaxHeight(intHeight);
        btnAdjustedButton.setPrefHeight(intHeight);
        btnAdjustedButton.setAlignment(Pos.BASELINE_CENTER);
    }
    public static void vSetImage(String strPickPath,ImageView imageView)
    {
        //Verify if picture under given path exists, if so create image and add it to provided ImageView
        try
        {
            //Check if image is in directory
            if(blnFileExists(strPickPath))
            {
                InputStream stream = new FileInputStream(strPickPath);
                Image image = new Image(stream);
                imageView.setImage(image);
                imageView.setPreserveRatio(true);
            }
        } catch (IOException ex) {
            System.out.println("- Error occured when loading pictures.");
        }
    }
    private static boolean IsItNumeric(String strIsItNumber)
    {
        //Validate if provided string is number -> string which consists of numbers only -> then it will be cast to Integer
        return strIsItNumber!= null && strIsItNumber.matches("[0-9]+");
    }
    private static String strSelectDirectory(int intFileType)
    {
        String strResult="";
            Stage stage = new Stage();
            File fileDefaultDir = new File(System.getProperty("user.dir")); //Default directory of selection
            if(intFileType==0)                                              //Select File
            {
                FileChooser fileChooser = new FileChooser();                //define file chooser
                fileChooser.setInitialDirectory(fileDefaultDir);            //define default/starting directory
                File selectedFile = fileChooser.showOpenDialog(stage);      //open filedialog and select file
                strResult=selectedFile.getPath();
            } else if(intFileType==1)                                               //Select Directory
            {
                DirectoryChooser directoryChooser = new DirectoryChooser();         //define directory chooser
                directoryChooser.setInitialDirectory(fileDefaultDir);               //define default/starting directory
                File selectedDirectory = directoryChooser.showDialog(stage);        //open filedialog and select directory
                strResult=selectedDirectory.getAbsolutePath();
            }
            stage.close();
        return strResult;
    }
    //Related to presenting of HighScore
    public static String strGetPathOfTheHighScoreFile()
    {
        String strPathResult="ScoreBoard.txt";      //from default, file is in project location
        File myObj = new File(strPathResult);
        while (!myObj.exists())
        {
            System.out.println("- When presenting ScoreBoard, file with ScoreBoard was not found -> looking for alternatives.");
            View.YesNoMessageWindow YesNoAnswer = new View.YesNoMessageWindow("File with ScoreBoard was not found in location:\n" + myObj.getAbsolutePath() + "\n\nWould you like to select it and continue (Yes)\n or return to main menu (No)?");
            System.out.println(YesNoAnswer.intGetAnswer());
            int intYesNo=YesNoAnswer.intGetAnswer();
            YesNoAnswer=null;           //delete object
            System.out.println("Odpowiedź: " + intYesNo);

            if (intYesNo == 0)          //user clicked "Yes"
            {
                strPathResult=strSelectDirectory(0);       //Select file
                myObj = new File(strPathResult);                    //Assign file from new path
            } else if(intYesNo == 1)    //user clicked "No" -> exit as null
            {
                return null;
            } else {                    //user clicked X/Cancel -> exit as null
                return null;
            }
        }
        System.out.println("- When presenting ScoreBoard, file with ScoreBoard was found!");
        return strPathResult;
    }
    public static String[] arrExtractStringArrayWithScoresFromFile(String strPath)
    {
        int intLineCount=intNumberOfLines(strPath);       //get number of lines in the highscore file
        String[] arrArrayOfScore=new String[intLineCount];        //prepare array to store text file -> +1 because i'll add new score
        try {
            BufferedReader br = new BufferedReader(new FileReader(strPath));
            String strLine="";
            intLineCount=0;
            while ((strLine = br.readLine()) != null)       //Load every line
            {
                String[] arrTemp=arrLineIntoArray(strLine);
                String strLineForDisplay= "Rank: " + strGetRank(arrTemp[0]) + " Player: " + strGetPlayerName(arrTemp[1]) + " Score: " + arrTemp[2] +".";
                arrArrayOfScore[intLineCount]=strLineForDisplay;    //insert lane into array
                intLineCount=intLineCount+1;                //increase count
            }
            br.close();                                     //close the file
        } catch (IOException e) {
            System.out.println("- An error occurred.");
            e.printStackTrace();
        }
        return arrArrayOfScore;
    }
    private static String strGetRank(String strRank)
    {
        int intSpaceForRank=6;              //Set alignement up to intSpaceForRank digits of results
        String strResult=strRank;
        if(strResult.length()<intSpaceForRank)
        {
            int intNumOfReqSpaces = intSpaceForRank-strResult.length();
            for(int i=0;i<intNumOfReqSpaces;i++)
            {
                strResult=strResult+" ";
            }
        }
        return strResult;
    }
    private static String strGetPlayerName(String strNickname)
    {
        int intSpaceForName=15;
        String strResult=strNickname;
        if(strResult.length()<intSpaceForName)
        {
            int intNumOfReqSpaces = intSpaceForName-strResult.length();
            for(int i=0;i<intNumOfReqSpaces;i++)
            {
                strResult=strResult+" ";
            }
        }
        return strResult;
    }
    //Saving the score to the file
    public static void vSaveTheScore(TextField txtInsertNickname, String strHighScore, Stage stgStageToClose)
    {
        //Combines all operations related to adding new record to the highscore
        boolean blnRecordAdded=false;
        String strNickname=txtInsertNickname.getText();
        if(strNickname.equals(""))
        {
            System.out.println("- User did not enter nickname");
            View.InformationOkWindow InformationForUser = new View.InformationOkWindow("Nickname can't be empty! \nPlease insert something or return to menu.");
            InformationForUser=null;        //delete object
        } else {
            boolean blnAllowInsert=true;
            for(int i=0;i<strNickname.length();i++)
            {
                char chrSign = strNickname.charAt(i);   //get sign from text
                int intASCII = (int)chrSign;            //convert sign to int
                if(!(47<intASCII && intASCII<58) && !(64<intASCII && intASCII<91) && !(96<intASCII && intASCII<123))    //validate condition
                {
                    View.InformationOkWindow InformationForUser = new View.InformationOkWindow("Forbidden sign was used!");
                    blnAllowInsert=false;
                    break;                      //exit loop
                }
            }
            if(blnAllowInsert)          //record can be addeed to HighScore file
            {
                System.out.println("- User nickname is: " + strNickname);
                Model.DataForScoreSave objNewRecordData = new Model.DataForScoreSave(strNickname,strHighScore);
                blnRecordAdded=Controller.blnHandleAddingNewScore(objNewRecordData);                            //add score to the file
            }
        }
        if(blnRecordAdded)              //if record was added, close SaveScore frame and open main menu
        {
            Controller.vOpenCloseWindow(stgStageToClose,"MainMenuWindow");                  //close stage, open new one
            System.out.println("- User saved score and was moved back to main screen.");
        }
    }
    public static boolean blnHandleAddingNewScore(Model.DataForScoreSave objNewRecordData)
    {
        //
        boolean blnResult=false;    //if record was successfully added -> return true
        String[][] arrFile;         //text file will be loaded into this array
        try {
            File myObj = new File(objNewRecordData.strGetScoreFilePath());
            if (!myObj.exists())
            {
                System.out.println("- HighScore file does not exist.");
                System.out.println(objNewRecordData.strGetScoreFilePath());
                String strPath=System.getProperty("user.dir") + "\\ScoreBoard.txt";
                System.out.println(strPath);
                File file = new File(strPath);
                if (file.createNewFile()) {
                    System.out.println("File has been created.");
                } else {
                    System.out.println("File already exists.");
                }
            }
            System.out.println("- Save record to Scoreboard in location: " + myObj.getAbsolutePath());
            System.out.println("- HighScore file does exist.");
            int intLineCount=intNumberOfLines(objNewRecordData.strGetScoreFilePath());                         //get number of lines in the highscore file
            arrFile=new String[intLineCount+1][3];                                  //prepare array to store text file -> +1 because i'll add new score
            intLineCount=0;                                                         //zero out variable -> will be use as count of current line
            BufferedReader br = new BufferedReader(new FileReader(objNewRecordData.strGetScoreFilePath()));
            String strLine="";
            while ((strLine = br.readLine()) != null)       //Load every line
            {
                String[] arrTemp=arrLineIntoArray(strLine);
                arrFile[intLineCount][0]=arrTemp[0];
                arrFile[intLineCount][1]=arrTemp[1];
                arrFile[intLineCount][2]=arrTemp[2];
                intLineCount=intLineCount+1;
            }
            br.close();         //close the file
            vAddNewHighScore(arrFile,objNewRecordData);
            blnResult=true;
        } catch (IOException e) {
            System.out.println("- An error occurred.");
            e.printStackTrace();
        }
        return blnResult;
    }
    private static void vAddNewHighScore(String[][] arrFile,Model.DataForScoreSave objNewRecordData)
    {
        int intNewScore=0;
        boolean blnNewHighest=true;             //from default we have the highest score to insert
        if(IsItNumeric(objNewRecordData.strGetScore()))         //Get points to save as integer
        {
            intNewScore =Integer.valueOf(objNewRecordData.strGetScore());   //convert to integer
        }
        //Loop through file (loaded to array), find and insert new score -> -1 becase one empty row was added at the end for new score
        int i=0;
        for(i=arrFile.length-1;i>=0;i--)
        {
            if(IsItNumeric(arrFile[i][2]))
            {
                int intCurrScoreCompare = Integer.valueOf(arrFile[i][2]);
                arrFile[i+1][0]=String.valueOf(i+2);            //we move it one down on the board
                if(intNewScore>intCurrScoreCompare)             //new value is bigger than one of stored scores -> move it down
                {
                    arrFile[i+1][1]=arrFile[i][1];
                    arrFile[i+1][2]=arrFile[i][2];
                } else {                                        //encountered bigger or equal value -> insert score in previous row/under
                    arrFile[i+1][1]=objNewRecordData.strGetNick();
                    arrFile[i+1][2]=String.valueOf(intNewScore);
                    blnNewHighest=false;                            //if score was inserted it can't be highest
                    break;                                          //element was inserted -> end the loop
                }
            }
        }
        //Case loop went through whole file and did not insert new score -> new score is the highest one
        if(blnNewHighest==true)
        {
            arrFile[0][0]=String.valueOf(1);
            arrFile[0][1]=objNewRecordData.strGetNick();
            arrFile[0][2]=String.valueOf(intNewScore);
        }
        //prepare string with ranking and insert array into text file (overwrite)
        try
        {
            String strInsertData ="";
            for(i=0;i< arrFile.length;i++)
            {
                strInsertData=strInsertData+arrFile[i][0] + ';'+arrFile[i][1]+';'+arrFile[i][2] +"\n";
            }
            FileWriter myWriter = new FileWriter(objNewRecordData.strGetScoreFilePath(),false);      //Open file for write and clear it (false)
            myWriter.write(strInsertData);                                                                  //Insert string into file
            myWriter.flush();
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    ////
    private static int intNumberOfLines(String strFileName)             //Get number of lines in the text file
    {
        int intLineCount=0;
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(strFileName));
            while (br.readLine() != null)       //Load every line
            {
                intLineCount=intLineCount+1;
            }
            br.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return intLineCount;
    }
    private static String[] arrLineIntoArray(String strLine)            //Converts line from the text file into array of Strings
    {
        String[] arrLineByColumn = new String[3];       //Ranking, Nickname, Score
        int intColFound=0;
        String strColumn="";
        for(int i=0;i<strLine.length();i++)             //Loop through string
        {
            char chrSign=strLine.charAt(i);
            if(Character.compare(chrSign,';')==0)       //signs matching -> ";" found
            {
                arrLineByColumn[intColFound]=strColumn; //insert word into column
                strColumn="";                           //zero out column
                intColFound=intColFound+1;              //increase column count
            } else {
                strColumn=strColumn + chrSign;
            }
            arrLineByColumn[intColFound]=strColumn;
        }
        return arrLineByColumn;
    }
    //Game
    public static void vPrepareTheStage(Group CloudGroup, Pane GameMap)
    {
        //Make the clouds
        String strPathToGraphic = System.getProperty("user.dir") + "\\graphics\\Cloud.png";
        Random rand = new Random();                                 //instance of random class
        int intWidthMax=(int)GameMap.widthProperty().get()-100;          //max X property
        int intHeightMax=(int)GameMap.heightProperty().get()-100;         //max Y property

        for(int i=0;i<=(Model.Global.intDifficulty+2)*2;i++)
        {
            int intRdmXLocation=rand.nextInt(70+intWidthMax);       //Randomize X position
            int intRdmYLocation=rand.nextInt(70+intHeightMax);      //Randomize Y position
            ImageView imageView=new ImageView();
            Controller.vSetImage(strPathToGraphic,imageView);
            imageView.setPreserveRatio(true);
            imageView.relocate(intRdmXLocation,intRdmYLocation+40); //+40 to not cover button/labels etc
            imageView.setFitHeight(70);
            imageView.toFront();
            CloudGroup.getChildren().add(imageView);                             //Add clouds to pane
        }
    }
    public static void vGoToGameSummary(Timeline timeline, Timer BackgroundTimer, Stage stage)
    {
        timeline.stop();                                                                //Stop Timeline
        BackgroundTimer.cancel();                                                       //Stop thread
            Controller.vOpenCloseWindow(stage,"GameSummaryWindow");      //Go to game summary window
    }
    public static void vBackgroundperations(Group group, HashMap<Integer, Model.Duck> dicDucks,Label lblTimer, Label lblLifes, Label lblPoints,double dblXBoundOfMap,double dblYBoundOfMap)
    {
        if(Model.Global.intLifes<=0)
        {
            System.out.println("- Lose condition achieved");
            Model.Global.blnEndGame=true;       //Game should end
            Model.Global.blnGameStatus=false;   //Game is lost
        }
        Model.Global.lngCurrentTime=System.nanoTime();
        Model.Global.lngPlayedTime=(Model.Global.lngCurrentTime-Model.Global.lngGameStart)/1000000000;
        lblTimer.setText("Elapsed time (seconds): " + Model.Global.lngPlayedTime);
        lblLifes.setText("Lifes: " + Model.Global.intLifes);
        lblPoints.setText("Points: " + Model.Global.lngScore);
        Model.Global.lngScore = Model.Global.lngPlayedTime*(Model.Global.intDifficulty+1);   //points are derivative of time and difficulty
        if(Model.Global.lngPlayedTime % Model.Global.intSpawnInterval==0)                    //New interval started
        {
            Random rand = new Random();                                 //instance of random class
            int intDuckSide=rand.nextInt(2);       //randomize duck type
            int intDuckType=rand.nextInt(3);      //randomize starting side of duck
            int intStartX;
            if(intDuckSide==0)
            {
                intStartX=5;
            } else {
                intStartX=(int)dblXBoundOfMap-5;
            }
            int intStartY=rand.nextInt(300)+20;
            Model.Duck objDuck = new Model.Duck(intDuckType,intDuckSide,group);
            objDuck.relocate(intStartX,intStartY);
            objDuck.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    objDuck.GotHit();
                    if(!objDuck.StillAlive())                                   //if duck is not alive -> delete it from the map
                    {
                        group.getChildren().remove(objDuck);                //if no hp, delete from window
                    }
                }
            });
            Model.Global.intDucksSpawned=Model.Global.intDucksSpawned+1;
            dicDucks.put(Model.Global.intDucksSpawned,objDuck);
        }
    }
}
