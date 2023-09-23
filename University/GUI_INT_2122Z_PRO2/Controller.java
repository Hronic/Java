import javax.swing.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.*;
import java.util.HashMap;

public class Controller
{
    public static void vOpenMainWindow(JFrame frmFrameToClose)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run() {
                new WindowsGame.MainWindow(frmFrameToClose);
            }
        });
    }
    public static void vOpenDifficultyWindow(JFrame frmFrameToClose)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run() {
                new WindowsGame.DifficultyWindow(frmFrameToClose);
            }
        });
    }
    public static void vOpenScoreWindow(JFrame frmFrameToClose)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run() {
                new WindowsGame.ScoreWindow(frmFrameToClose);
            }
        });
    }
    public static void vOpenGameWindow(int intDifficultyLevel,JFrame frmFrameToClose)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run() {
                new WindowsGame.GameMapWindow(intDifficultyLevel,frmFrameToClose);
            }
        });
    }
    public static void vOpenBuyUpgradeWindow(HashMap<String, Model.Country> dicCountries)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                new WindowsGame.BuyUpgradeWindow(dicCountries);
            }
        });
    }
    public static void vOpenScoreSaveWindow(JFrame frmFrameToClose,Model.DataForSummary objSummary,Timer t)
    {
        t.stop();                       //Stop the thread from main class
        System.out.println("Timer thread stopped.");
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run() {
                new WindowsGame.ScoreSaveWindow(objSummary,frmFrameToClose);
            }
        });
    }
    public static String strValidateAndGetPicPathSaveScore(Model.DataForSummary objSummary)
    {
        String strResult=null;
        //Check Win or Lose -> assign proper path to graphics
        if(objSummary.GetWinLoseStatus())
        {
            strResult="Win.jpg";
        } else {
            strResult="Lose.jpg";
        }
        //Validate if Graphics exist
        File tempFile = new File(strResult);
        if(!tempFile.exists())
        {
            strResult=null;
        }
        return strResult;
    }
    public static boolean blnHandleAddingNewScore(Model.DataForScoreSave objNewRecordData,JFrame frmSaveHighScore)
    {
        boolean blnResult=false;    //if record was successfully added -> return true
        String[][] arrFile;         //text file will be loaded into thus array
        try {
            File myObj = new File(objNewRecordData.strGetScoreFilePath());
            if (!myObj.exists())
            {
                System.out.println("- HighScore file does not exist.");
                SwingUtilities.invokeLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        new WindowsGame.SelectHighScoreFileWindow(myObj.getAbsolutePath(),frmSaveHighScore,objNewRecordData);
                    }
                });
            } else {                                                                    //Scoreboard does exist
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
            }
        } catch (IOException e) {
            System.out.println("- An error occurred.");
            e.printStackTrace();
        }
        return blnResult;
    }
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
    private static boolean IsItNumeric(String strIsItNumber)
    {
        //Validate if provided string is number -> string which consists of numbers only -> then it will be cast to Integer
        return strIsItNumber!= null && strIsItNumber.matches("[0-9]+");
    }
    public static boolean blnHandleScoreAddForGivenDirectory(Model.DataForScoreSave objNewRecordData,JFrame frmSaveHighScore)
    {
        String strDirectoryPath=strSelectDirectory(1);  //1 -> Directories only
        if(strDirectoryPath==null)
        {
            return false;
        }else{
            String strFilePath=strDirectoryPath + "\\ScoreBoard.txt";
            try {
                File myObj = new File(strFilePath);
                myObj.createNewFile();
                objNewRecordData.setScoreFilePath(strFilePath);     //update score path
                blnHandleAddingNewScore(objNewRecordData,frmSaveHighScore);
            } catch (IOException e) {
                System.out.println("- An error occurred.");
                e.printStackTrace();
            }
            return true;
        }
    }
    public static int blnHandleScoreAddForSelectedFile(Model.DataForScoreSave objNewRecordData,JFrame frmSaveHighScore)
    {
        String strDirectoryPath=strSelectDirectory(0);  //0 -> Files only
        int intResult=0;
        if(strDirectoryPath==null)                          //File was not selected
        {
            intResult=0;
        }else{
            if (strDirectoryPath.length() <= 3)              //Selected file path is smaller than 3 signs -> incorrect file
            {
                intResult=1;
            } else if(strDirectoryPath.length() > 3)        //path has more than 3 signs
            {
                String strExtension=strDirectoryPath.substring(strDirectoryPath.length() - 3);
                if(strExtension.equals("txt") || strExtension.equals("TXT")) //Correct file was selected
                {
                    objNewRecordData.setScoreFilePath(strDirectoryPath);
                    blnHandleAddingNewScore(objNewRecordData,frmSaveHighScore);
                    intResult=3;
                }else{                                                                      //incorrect extension
                    intResult=2;
                }
            }
        }
        return intResult;
    }
    private static String strSelectDirectory(int intFileType)
    {
        JFileChooser chooser;
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));             //Set default directory
        if(intFileType==0)                                                      //Select File
        {
            chooser.setDialogTitle("Please select file");
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);              //User see only folders
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text file", "txt");
            chooser.setFileFilter(filter);
        } else if(intFileType==1)                                               //Select Directory
        {
            chooser.setDialogTitle("Please select folder");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);         //User see only folders
        }
        if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION)
        {
            System.out.println("- Selected path: '" + chooser.getSelectedFile().toString() + "'.");
            return chooser.getSelectedFile().toString();
        }
        else
        {
            System.out.println("- No folder has been selected.");
            return null;
        }
    }
    public static String[] vHandleScorePresentation()
    {
        //Loop until scoreboard exists or user resigns
        String strScoreBoardFile="ScoreBoard.txt";
        File myObj = new File(strScoreBoardFile);

        while (!myObj.exists())
        {
            System.out.println("- When presenting ScoreBoard, file with ScoreBoard was not found -> looking for alternatives.");
            int intYesNo = JOptionPane.showConfirmDialog(null, "<html>File with ScoreBoard was not found in location: " + myObj.getAbsolutePath() +"<br>Would you like to select it and continue (Yes) or return to main menu (No)?", "ScoreBoard file is missing.", JOptionPane.YES_NO_OPTION);
            if (intYesNo == 0)          //user clicked "Yes"
            {
                strScoreBoardFile=strSelectDirectory(0);       //Select file
                myObj = new File(strScoreBoardFile);                    //Assign file from new path
            } else if(intYesNo == 1)    //user clicked "No" -> exit as null
            {
                return null;
            } else {                    //user clicked X -> exit as null
                return null;
            }
        }
        System.out.println("- When presenting ScoreBoard, file with ScoreBoard was found!");
        return arrGetStringArrayWithScores(strScoreBoardFile);
    }
    private static String[] arrGetStringArrayWithScores(String strPath)
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
    public static String strGetLevelDifficulty()
    {
        String strResult="";
        switch (Model.Global.intDifficulty)
        {
            case 1:
                strResult="Easy";
                break;
            case 2:
                strResult="Normal";
                break;
            case 3:
                strResult="Hard";
                break;
        }
        return strResult;
    }
    public static void vInitiateGlobalVariables(int intDifficultyLevel)
    {
        Model.Global.lngGameStart=System.nanoTime();        //Start countdown;
        Model.Global.lngElapsedTime=0;
        Model.Global.blnGameWon=false;
        Model.Global.blnGameLost=false;
        Model.Global.intInterval1=5;                               //Every X sec, people get infected
        Model.Global.intInterval2= Model.Global.intInterval1*2;     //Every X sec, people get healed/dead
        Model.Global.intSicknessPeriod=1;                           //infection lasts whole intInterval1 period
        Model.Global.intCurrentPeriod=0;
        Model.Global.intDifficulty=intDifficultyLevel;
        Model.Global.intPointsForUpgrades=0;
        Model.Global.dblPointRatioForAvoidInfec=0.5;
        Model.Global.dblPointRatioForHealing=1.1;
        Model.Global.intGlobalPopulation=0;
        Model.Global.intGlobalInfected=0;
        Model.Global.intGlobalDead=0;
        Model.Global.lngTotalInfected=0;
        Model.Global.lngTotalCured=0;
    }
    public static boolean blnValidateIfCountryGraphicsAvailable(String[] arrGraphicsPath)
    {
        boolean blnResult=true;
        for(int i=0;i<arrGraphicsPath.length;i++)   //loop through array with paths to graphics -> check availability of each file
        {
            File tempFile = new File(arrGraphicsPath[i]);
            if(!tempFile.exists())                          //if at least 1 graphic does not exist -> can't run the game
            {
                blnResult=false;
                break;                                      //can leave loop -> no need for further check
            }
        }
        return blnResult;
    }
    public static boolean blnValidateIfOtherGraphicsAvailable()
    {
        //
        return true;
    }
    public static void vGameBackgroundPerformance(JLabel[] arrLabels,HashMap<String, Model.Country> dicCountries)
    {

        boolean blnInterval1=false;
        boolean blnInterval2=false;

        //Actions performed every second:
        Model.Global.lngElapsedTime=(System.nanoTime()-Model.Global.lngGameStart)/1000000000;    //calculate how much time elapsed
        arrLabels[0].setText("Elasped time (sec): " + Model.Global.lngElapsedTime);              //Update label with time every second
        arrLabels[1].setText("Points to spend: " + Model.Global.intPointsForUpgrades);           //Update label with points  every second

        if(false)                  //(Model.Global.lngElapsedTime>10)
        {
            Model.Global.blnGameLost=true;
        }

        if(Model.Global.lngElapsedTime % Model.Global.intInterval1==0)
        {
            blnInterval1=true;
            Model.Global.intCurrentPeriod=Model.Global.intCurrentPeriod+1;      //increase period count
        }
        if(Model.Global.lngElapsedTime % Model.Global.intInterval2==0)
        {
            blnInterval2=true;
        }

        //Actions performed every specific time stamp -> taken from Global variable - intInterval1
        if(blnInterval1)
        {
            //Recalculate infection & points
            if(blnInterval2==false)
            {
                int intDeadCountries=0;
                int intSurvivedCountries=0;
                //Case only interval 1 -> Infections and points
                for (String strKey : dicCountries.keySet())
                {
                    dicCountries.get(strKey).vRecalculateInfection();
                    int intInfectedForCountry=dicCountries.get(strKey).getInfected();
                    //Check lose condition
                    boolean blnCountryStatus=dicCountries.get(strKey).getCountryStatus();
                    if(blnCountryStatus==false)
                    {
                        intDeadCountries=intDeadCountries+1;
                    }
                    //Check win conditions
                    if((blnCountryStatus==true) && (intInfectedForCountry==0))
                    {
                        intSurvivedCountries=intSurvivedCountries+1;
                    }
                }
                if(intDeadCountries==10)    //all 10 countries are dead -> game over
                {
                    Model.Global.blnGameLost=true;
                }
                if(intSurvivedCountries==10)    //all 10 countries are have no infected and are alive
                {
                    Model.Global.blnGameWon=true;
                }
                arrLabels[2].setText("Total citizens: " + Model.Global.intGlobalPopulation);
                arrLabels[3].setText("Total infected: " + Model.Global.intGlobalInfected);
                arrLabels[4].setText("Total dead: " + Model.Global.intGlobalDead);
            } else {
                //Case interval 1 and interval 2 -> Infections, heal, dead and points
                for (String strKey : dicCountries.keySet())
                {
                    dicCountries.get(strKey).vRecalculateInfection();
                    dicCountries.get(strKey).vRecalculateHealDead();
                }
            }
        }
    }
    public static void vLoadCountries(HashMap<String, Model.Country> dicCountries)
    {
        int intInfectedForTest=1000;
        //Create countries
        Model.Country cAmerkyka_N = new Model.Country("United States",37950000,intInfectedForTest);
        Model.Country cAmerkyka_S = new Model.Country("Mexico",37950000,intInfectedForTest);
        Model.Country cHiszpania = new Model.Country("Spain",37950000,intInfectedForTest);
        Model.Country cFrancja = new Model.Country("France",37950000,intInfectedForTest);
        Model.Country cNiemcy = new Model.Country("Germany",37950000,intInfectedForTest);
        Model.Country cPolska = new Model.Country("Poland",37950000,intInfectedForTest);
        Model.Country cRussia = new Model.Country("Russia",37950000,intInfectedForTest);
        Model.Country cChina = new Model.Country("China",37950000,1000);
        Model.Country cGreatBritain = new Model.Country("Great Britain",37950000,intInfectedForTest);
        Model.Country cAfrica = new Model.Country("Nigeria",37950000,intInfectedForTest);

        //put Countries into HashMap
        dicCountries.put("United States",cAmerkyka_N);
        dicCountries.put("Mexico",cAmerkyka_S);
        dicCountries.put("Spain",cHiszpania);
        dicCountries.put("France",cFrancja);
        dicCountries.put("Germany",cNiemcy);
        dicCountries.put("Poland",cPolska);
        dicCountries.put("Russia",cRussia);
        dicCountries.put("China",cChina);
        dicCountries.put("Great Britain",cGreatBritain);
        dicCountries.put("Nigeria",cAfrica);
    }
    public static JPanel jpBuyUpgradeSub1GridR2to6(int intRow,HashMap<String, Model.Country> dicCountries)
    {
        JPanel jpResult=new JPanel();
        jpResult.setBackground(new Color(207, 98, 255));
        jpResult.setLayout(new GridLayout(1,11));

        JLabel jlblHeader= new JLabel("");
        if(intRow==3){jlblHeader.setText("Population:");}
        if(intRow==4){jlblHeader.setText("Infected");}
        if(intRow==5){jlblHeader.setText("Cured");}
        if(intRow==6){jlblHeader.setText("Dead");}
        jpResult.add(jlblHeader);                           //Column 1 -> header

        String strInput="";
        for (String strKey: dicCountries.keySet())
        {
            switch(intRow)
            {
                case 2:         //Depending on row, different data
                    strInput=strKey;
                    break;
                case 3:
                    strInput=String.valueOf(dicCountries.get(strKey).getPopulation());
                    break;
                case 4:
                    strInput=String.valueOf(dicCountries.get(strKey).getInfected());
                    break;
                case 5:
                    strInput=String.valueOf(dicCountries.get(strKey).getCured());
                    break;
                case 6:
                    strInput=String.valueOf(dicCountries.get(strKey).getDead());
                    break;
            }

            JLabel jlblCountry=new JLabel(strInput);
            jpResult.add(jlblCountry);
        }
        return jpResult;
    }
    public static JPanel jpBuyUpgradeSub1GridR9to17(int intRow)
    {
        JPanel jpResult=new JPanel();
        jpResult.setBackground(new Color(207, 98, 255));
        jpResult.setLayout(new GridLayout(1,3));

        String strCol1="";
        String strCol2="";
        JLabel jlblCol1=new JLabel("");
        JLabel jlblCol2=new JLabel("");
        JButton btnAction=new JButton("Buy upgrade");

        switch(intRow)
        {
            case 9:         //Depending on row, different data
                strCol1=Model.DataUpgrades.strUpg1Name;
                strCol2=String.valueOf(Model.DataUpgrades.intUpg1Cost);
                break;
            case 10:
                strCol1=Model.DataUpgrades.strUpg2Name;
                strCol2=String.valueOf(Model.DataUpgrades.intUpg2Cost);
                break;
            case 11:
                strCol1=Model.DataUpgrades.strUpg3Name;
                strCol2=String.valueOf(Model.DataUpgrades.intUpg3Cost);
                break;
            case 12:
                strCol1=Model.DataUpgrades.strUpg4Name;
                strCol2=String.valueOf(Model.DataUpgrades.intUpg4Cost);
                break;
            case 13:
                strCol1=Model.DataUpgrades.strUpg5Name;
                strCol2=String.valueOf(Model.DataUpgrades.intUpg5Cost);
                break;
            case 14:
                strCol1=Model.DataUpgrades.strUpg6Name;
                strCol2=String.valueOf(Model.DataUpgrades.intUpg6Cost);
                break;
            case 15:
                strCol1=Model.DataUpgrades.strUpg7Name;
                strCol2=String.valueOf(Model.DataUpgrades.intUpg7Cost);
                break;
            case 16:
                strCol1=Model.DataUpgrades.strUpg8Name;
                strCol2=String.valueOf(Model.DataUpgrades.intUpg8Cost);
                break;
            case 17:
                strCol1=Model.DataUpgrades.strUpg9Name;
                strCol2=String.valueOf(Model.DataUpgrades.intUpg9Cost);
                break;
        }
        jlblCol1.setText(strCol1);
        jlblCol2.setText(strCol2);

        jpResult.add(jlblCol1);
        jpResult.add(jlblCol2);
        jpResult.add(btnAction);
        return jpResult;
    }
    public static JComboBox jpBuyUpgradeComboBox(HashMap<String, Model.Country> dicCountries)
    {

        String arrCountries[] = new String[dicCountries.size()];    //arrayof strings
        int intIterator=0;
        for(String strKey: dicCountries.keySet())
        {
            arrCountries[intIterator]=strKey;
            intIterator = intIterator+1;
        }
        JComboBox  jcbSelectCountry = new JComboBox(arrCountries);   //create combobox of countries
        return jcbSelectCountry;
    }
    public static boolean vBuyUpgradeButtonAction(int intUpgradeNumber,String strCountryName,HashMap<String, Model.Country> dicCountries)
    {
        //Check if user has enough points -> Check if he can buy upgrade -> Buy upgrade
        boolean blnResult=false;
        switch(intUpgradeNumber)
        {
            case 1:
                if(Model.Global.intPointsForUpgrades>Model.DataUpgrades.intUpg1Cost)        //Check if user has enough points
                {
                    blnResult=dicCountries.get(strCountryName).UpgradeHelmets();            //Check if user can upgrade, if so do so
                }
                break;
            case 2:
                if(Model.Global.intPointsForUpgrades>Model.DataUpgrades.intUpg2Cost)        //Check if user has enough points
                {
                    blnResult=dicCountries.get(strCountryName).UpgradeMasks();              //Check if user can upgrade, if so do so
                }
                break;
            case 3:
                if(Model.Global.intPointsForUpgrades>Model.DataUpgrades.intUpg3Cost)        //Check if user has enough points
                {
                    blnResult=dicCountries.get(strCountryName).UpgradeSocialDistance();     //Check if user can upgrade, if so do so
                }
                break;
            case 4:
                if(Model.Global.intPointsForUpgrades>Model.DataUpgrades.intUpg4Cost)        //Check if user has enough points
                {
                    blnResult=dicCountries.get(strCountryName).UpgradeVirusAwareness();     //Check if user can upgrade, if so do so
                }
                break;
            case 5:
                if(Model.Global.intPointsForUpgrades>Model.DataUpgrades.intUpg5Cost)        //Check if user has enough points
                {
                    blnResult=dicCountries.get(strCountryName).UpgradeVaccine1();           //Check if user can upgrade, if so do so
                }
                break;
            case 6:
                if(Model.Global.intPointsForUpgrades>Model.DataUpgrades.intUpg6Cost)        //Check if user has enough points
                {
                    blnResult=dicCountries.get(strCountryName).UpgradeVaccine2();           //Check if user can upgrade, if so do so
                }
                break;
            case 7:
                if(Model.Global.intPointsForUpgrades>Model.DataUpgrades.intUpg7Cost)        //Check if user has enough points
                {
                    blnResult=dicCountries.get(strCountryName).UpgradeMedicalHardware();    //Check if user can upgrade, if so do so
                }
                break;
            case 8:
                if(Model.Global.intPointsForUpgrades>Model.DataUpgrades.intUpg8Cost)        //Check if user has enough points
                {
                    blnResult=dicCountries.get(strCountryName).UpgradeDoctorKnowledge();    //Check if user can upgrade, if so do so
                }
                break;
            case 9:
                if(Model.Global.intPointsForUpgrades>Model.DataUpgrades.intUpg9Cost)        //Check if user has enough points
                {
                    blnResult=dicCountries.get(strCountryName).UpgradeQuickerReaction();    //Check if user can upgrade, if so do so
                }
                break;
        }
        System.out.println("- User wants to buy upgrade: " + intUpgradeNumber + " for country: " + strCountryName);
        return blnResult;
    }
}
