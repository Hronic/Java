//https://docs.oracle.com/javase/tutorial/uiswing/layout/visual.html

import javax.naming.ldap.Control;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class WindowsGame
{
    public static class MainWindow extends JFrame
    {
        public MainWindow()             //constructor
        {
            //Define buttons
            int intWidth = 205;
            int intHeight=30;
            int intFrameWidth=335;

            JFrame frmMainFrame = new JFrame("Main menu - menu główne");
            frmMainFrame.setLayout(null);
            frmMainFrame.setSize(intFrameWidth, intHeight*3 + 55);     //Set the size of frame
            frmMainFrame.setLocationRelativeTo(null);                        //set panel to center
            frmMainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     //Exit the application

            JPanel panel = new JPanel(null);
            panel.setBackground(new Color(207, 98, 255));
            panel.setBounds(0,0,intFrameWidth, intHeight*3 + 55);

            //New game button
            JButton btnStart = new JButton("New Game - nowa gra");              //add button with specific description
            btnStart.setBounds((intFrameWidth-intWidth)/2,5,intWidth,intHeight);
            btnStart.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Controller.vOpenDifficultyWindow(frmMainFrame);
                    System.out.println("- User wants to start new game.");
                }
            });

            //Score button
            JButton btnScore = new JButton("High Scores - tabela wyników");
            btnScore.setBounds((intFrameWidth-intWidth)/2,40,intWidth,intHeight);
            btnScore.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Controller.vOpenScoreWindow(frmMainFrame);
                    System.out.println("- User wants to view high score.");
                }
            });

            //Exit button
            JButton btnExit = new JButton("Exit – wyjście");
            btnExit.setBounds((intFrameWidth-intWidth)/2,75,intWidth,intHeight);
            btnExit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    //Make sure user wants to close the window
                    int intYesNo = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit the game?", "Please confirm game closure.", JOptionPane.YES_NO_OPTION);
                    if (intYesNo == 0)                  //Yes
                    {
                        System.out.println("- User decided to close the game.\n");
                        frmMainFrame.dispose(); //Close window
                    } else if(intYesNo == 1){           //No
                        System.out.println("- User decided to stay in the game.\n");
                    }
                }
            });

            //Add buttons
            panel.add(btnStart);
            panel.add(btnScore);
            panel.add(btnExit);

            //Create panel -> display the window
            frmMainFrame.add(panel);
            frmMainFrame.setVisible(true);                                   //Panel is visible
        }
        public MainWindow(JFrame frmFrame)
        {
            this();                         //call default constructor
            frmFrame.dispose();         //close given frame
        }
    }
    public static class ScoreWindow extends JFrame
    {
        public ScoreWindow()                            //constructor
        {
            //https://docs.oracle.com/javase/tutorial/uiswing/layout/border.html
            JFrame frmHighScore = new JFrame("High score - punktacja");     //Description on frame
            frmHighScore.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);         //Close when click "X"
            frmHighScore.setSize(350, 500);
            frmHighScore.setLocationRelativeTo(null);

            JPanel panel = new JPanel(new BorderLayout());

            JButton btnReturnMain= new JButton("Return to menu");
            btnReturnMain.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    SwingUtilities.invokeLater(new Runnable()
                    {
                        @Override
                        public void run() {
                            new MainWindow(frmHighScore);
                        }
                    });
                    System.out.println("- User wants to go back to main menu.");
                }
            });
            panel.add(btnReturnMain,BorderLayout.PAGE_START);                           //add button and position it on panel

            String[] arrTemp=Controller.vHandleScorePresentation();                     //array made of text file, which will be used for Jlist
            if(arrTemp==null)                                                           //test if array was loaded properly
            {
                JOptionPane.showMessageDialog(null, "<html>Application was unable to load ScoreBoard.<br>You will be moved back to main menu.", "Problem with ScoreBoard file.",JOptionPane.WARNING_MESSAGE);
                SwingUtilities.invokeLater(new Runnable()
                {
                    @Override
                    public void run() {
                        new MainWindow(frmHighScore);
                    }
                });
                System.out.println("- Something went wrong with reading of the ScoreBoard file, return to main menu.");
            } else {
                //Define JList and scroll bar for it
                JList jlistScoreArray = new JList(arrTemp);   //load data from text file into Jlist
                JScrollPane jScrollPaneList = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
                jScrollPaneList.setViewportView(jlistScoreArray);

                //jlistScoreArray.setLayoutOrientation(JList.VERTICAL);                     //in case we would like to have scroll only vertical -> delete then value from brackets in jScrollPaneList declaration
                panel.add(jScrollPaneList,BorderLayout.CENTER);                             //add Jlist and scrollbar to panel and position it in center

                //Set and display JFrame
                frmHighScore.add(panel);             //add panel to frame
                frmHighScore.setVisible(true);
            }
        }
        public ScoreWindow(JFrame frmFrame)            //constructor
        {
            this();
            frmFrame.dispose();
        }
    }
    public static class DifficultyWindow extends JFrame
    {
        public DifficultyWindow()
        {
            //Define frame

            JFrame frmDifficulty = new JFrame("Select difficulty - wybierz stopień trudności");     //descr. of frame
            frmDifficulty.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                                //behaviour on close
            frmDifficulty.setSize(400,160);
            frmDifficulty.setLocationRelativeTo(null);                                        //set frame to center

            //Prepare panel, where buttons will be stored
            JPanel panel = new JPanel();
            panel.setBackground(new Color(207, 98, 255));
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));    //Vertical layout in panel

            // Define buttons
            JButton btnEasyDiff = new JButton("Easy difficulty");
            btnEasyDiff.setMaximumSize(new Dimension(200, 30));
            btnEasyDiff.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnEasyDiff.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Controller.vOpenGameWindow(1,frmDifficulty);
                }
            });

            JButton btnNormDiff = new JButton("Normal difficulty");
            btnNormDiff.setMaximumSize(new Dimension(200, 30));
            btnNormDiff.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnNormDiff.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Controller.vOpenGameWindow(2,frmDifficulty);
                }
            });

            JButton btnHardDiff = new JButton("Hard difficulty");
            btnHardDiff.setMaximumSize(new Dimension(200, 30));
            btnHardDiff.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnHardDiff.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Controller.vOpenGameWindow(3,frmDifficulty);
                }
            });

            JButton btnReturn = new JButton("Return to main menu");
            btnReturn.setMaximumSize(new Dimension(200, 30));
            btnReturn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnReturn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    SwingUtilities.invokeLater(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            System.out.println("- User did not choose diffculty -> returned to main menu.");
                            new MainWindow(frmDifficulty);
                        }
                    });
                }
            });

            // Add buttons to the panel
            panel.add(btnEasyDiff);
            panel.add(btnNormDiff);
            panel.add(btnHardDiff);
            panel.add(btnReturn);

            //Add panel to frame, make visible
            frmDifficulty.add(panel);
            frmDifficulty.setVisible(true);
        }
        public DifficultyWindow(JFrame frmFrame)
        {
            this();
            frmFrame.dispose();
        }
    }
    public static class GameMapWindow extends JFrame
    {
        private Timer t;                            //define Timer
        public GameMapWindow(int intDifficultyLevel)
        {
            int intHeight=40;
            int intFrameWidth=1000;
            int intFrameHeight=650;

            //Frame
            JFrame frmGame = new JFrame("Game AntiPlague - Gra Anty Plaga!");       //descr. of frame
            frmGame.setLayout(null);
            frmGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );                    //behaviour on close -> ignore -> force choosing from buttons
            frmGame.setSize(intFrameWidth,intFrameHeight);
            frmGame.setLocationRelativeTo(null);                                        //set frame to center
            frmGame.setResizable(false);                                                //block resize option

            //Panel
            JPanel panel = new JPanel(null);
            panel.setBackground(new Color(51, 158, 255));
            panel.setBounds(0,0,intFrameWidth,intFrameHeight);

            //Check if graphics are available
            String[] arrGraphicsPath={"NorthAmerica.png","SouthAmerica.png","SpainPortugal.png","France.png","Germany.png","Poland.png","GreatBritain.png","Russia.png","China.png","Africa.png"};    //Define array with paths to graphics
            if(Controller.blnValidateIfCountryGraphicsAvailable(arrGraphicsPath)==false)            //Check if pictures with countries are available -> false error
            {
                JOptionPane.showMessageDialog(null, "<html>Some of the graphics are missing - they should be located in: " + System.getProperty("user.dir") + "<br>Please update the catalogue and run the game again.<br>Current game session will close.", "Missing graphics!",    JOptionPane.WARNING_MESSAGE);
                Controller.vOpenMainWindow(frmGame);
                System.out.println("- Some of the country graphics are missing, close the game, return to menu.");
            }

            //Return menu button
            int intButtWidth=140;
            JButton btnReturnMenu = new JButton("Return main menu");
            btnReturnMenu.setBounds(5,5,intButtWidth,intHeight);
            btnReturnMenu.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    if(JOptionPane.showConfirmDialog(null, "<html>Are you sure you want to quit the game (Yes) or return (No)?<br>Note that all progress will be lost.", "Do you really want to leave the game?.", JOptionPane.YES_NO_OPTION)==0)
                    {
                        Controller.vOpenMainWindow(frmGame);
                        System.out.println("- User wants to leave the game through return to menu button.");
                    }
                }
            });

            //Globals
            Controller.vInitiateGlobalVariables(intDifficultyLevel);
            //Load countries
            HashMap<String, Model.Country> dicCountries = new HashMap<String, Model.Country>();            //Dictionary, Key name of country, Value object of country
            Controller.vLoadCountries(dicCountries);

            //Button for country management
            JButton btnManageCountry = new JButton("Manage country");
            btnManageCountry.setBounds(intFrameWidth-intButtWidth-20,5,intButtWidth,intHeight);
            btnManageCountry.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Controller.vOpenBuyUpgradeWindow(dicCountries);
                    System.out.println("- User wants to manage countries.");
                }
            });

            //Position label for Difficulty, Timer and Point count
            JLabel jlblDifficulty=new JLabel("Game difficulty: " + Controller.strGetLevelDifficulty());
            jlblDifficulty.setBounds(btnReturnMenu.getX()+intButtWidth+5,btnReturnMenu.getY(),150,intHeight);
            int intLblWidth=300;
            JLabel jlblTime=new JLabel("Game time: ");
            jlblTime.setBounds(jlblDifficulty.getX()+intButtWidth+5,btnReturnMenu.getY(),intLblWidth,intHeight);
            JLabel jlblPoints=new JLabel("Current points: ");
            jlblPoints.setBounds(jlblTime.getX()+intLblWidth+5,jlblTime.getY(),intLblWidth,intHeight);

            JLabel jlblTotalCitizens=new JLabel("Total citizens: unknown.");
            jlblTotalCitizens.setBounds(intFrameWidth-intButtWidth-80,intFrameHeight-150,intLblWidth,intHeight);
            JLabel jlblTotalInfected=new JLabel("Total infected: unknown.");
            jlblTotalInfected.setBounds(jlblTotalCitizens.getX(),jlblTotalCitizens.getY()+20,intLblWidth,intHeight);
            JLabel jlblTotalDead = new JLabel("Total dead: unknown.");
            jlblTotalDead.setBounds(jlblTotalCitizens.getX(),jlblTotalInfected.getY()+20,intLblWidth,intHeight);

            //Consolidate all labels which should be modified during gameplay
            JLabel[] arrLabels = {jlblTime,jlblPoints,jlblTotalCitizens,jlblTotalInfected,jlblTotalDead};

            //Add countries to Panel
            try {
                BufferedImage imgCountryPicture = ImageIO.read(new File(arrGraphicsPath[0]));
                //Country 1
                JLabel picCountry1 = new JLabel(new ImageIcon(imgCountryPicture));
                int intPosX=0;
                int inPoxY=btnReturnMenu.getY()+btnReturnMenu.getHeight();
                picCountry1.setBounds(intPosX,inPoxY,350,350);
                panel.add(picCountry1);

                //Country 2
                imgCountryPicture = ImageIO.read(new File(arrGraphicsPath[1]));
                intPosX=picCountry1.getX()+60;
                inPoxY=picCountry1.getY()+picCountry1.getHeight()-130;
                JLabel picCountry2 = new JLabel(new ImageIcon(imgCountryPicture));
                picCountry2.setBounds(intPosX,inPoxY,350,350);
                panel.add(picCountry2);

                //Country 3
                imgCountryPicture = ImageIO.read(new File(arrGraphicsPath[2]));
                intPosX=picCountry1.getX()+ 250;
                inPoxY=200;
                JLabel picCountry3 = new JLabel(new ImageIcon(imgCountryPicture));
                picCountry3.setBounds(intPosX,inPoxY,350,350);
                panel.add(picCountry3);

                //Country 4
                imgCountryPicture = ImageIO.read(new File(arrGraphicsPath[3]));
                intPosX=picCountry3.getX()+ 45;
                inPoxY=135;
                JLabel picCountry4 = new JLabel(new ImageIcon(imgCountryPicture));
                picCountry4.setBounds(intPosX,inPoxY,350,350);
                panel.add(picCountry4);

                //Country 5
                imgCountryPicture = ImageIO.read(new File(arrGraphicsPath[4]));
                intPosX=picCountry4.getX()+ 67;
                inPoxY=135;
                JLabel picCountry5 = new JLabel(new ImageIcon(imgCountryPicture));
                picCountry5.setBounds(intPosX,inPoxY,350,350);
                panel.add(picCountry5);

                //Country 6
                imgCountryPicture = ImageIO.read(new File(arrGraphicsPath[5]));
                intPosX=picCountry5.getX()+ 63;
                inPoxY=125;
                JLabel picCountry6 = new JLabel(new ImageIcon(imgCountryPicture));
                picCountry6.setBounds(intPosX,inPoxY,350,350);
                panel.add(picCountry6);

                //Country 7
                imgCountryPicture = ImageIO.read(new File(arrGraphicsPath[6]));
                intPosX=picCountry4.getX()-30;
                inPoxY=30;
                JLabel picCountry7 = new JLabel(new ImageIcon(imgCountryPicture));
                picCountry7.setBounds(intPosX,inPoxY,350,350);
                panel.add(picCountry7);

                //Country 8
                imgCountryPicture = ImageIO.read(new File(arrGraphicsPath[7]));
                intPosX=picCountry6.getX()+185;
                inPoxY=70;
                JLabel picCountry8 = new JLabel(new ImageIcon(imgCountryPicture));
                picCountry8.setBounds(intPosX,inPoxY,350,350);
                panel.add(picCountry8);

                //Country 9
                imgCountryPicture = ImageIO.read(new File(arrGraphicsPath[8]));
                intPosX=picCountry6.getX()+155;
                inPoxY=175;
                JLabel picCountry9 = new JLabel(new ImageIcon(imgCountryPicture));
                picCountry9.setBounds(intPosX,inPoxY,350,350);
                panel.add(picCountry9);

                //Country 10
                imgCountryPicture = ImageIO.read(new File(arrGraphicsPath[9]));
                intPosX=picCountry3.getX()+200;
                inPoxY=350;
                JLabel picCountry10 = new JLabel(new ImageIcon(imgCountryPicture));
                picCountry10.setBounds(intPosX,inPoxY,200,200);
                panel.add(picCountry10);
            } catch (IOException ex) {
                System.out.println("- Error occured when loading pictures.");
            }

            //Set Timer to update every second, https://docs.oracle.com/javase/7/docs/api/javax/swing/Timer.html
            //Run in new thread
            int intInterval = 1000;                                     //milliseconds
            ActionListener alPerformBackgroundTasks = new ActionListener() {
                public void actionPerformed(ActionEvent evt)
                {
                    Controller.vGameBackgroundPerformance(arrLabels,dicCountries);        //Perform all the actions in the background of the game
                    //Lose condition met
                    if(Model.Global.blnGameLost==true)                                                                    //Check if condition for game end is fulfilled
                    {
                        Model.DataForSummary objSummary = new Model.DataForSummary(false,dicCountries);
                        Controller.vOpenScoreSaveWindow(frmGame,objSummary,t);                              //Open Score Window
                    }
                    //Victory condition met
                    if(Model.Global.blnGameWon==true)                                                                    //Check if condition for game end is fulfilled
                    {
                        Model.DataForSummary objSummary = new Model.DataForSummary(true,dicCountries);
                        Controller.vOpenScoreSaveWindow(frmGame,objSummary,t);                              //Open Score Window
                    }
                }
            };
            t = new Timer(intInterval, alPerformBackgroundTasks);   //set Timer with interval and task to be performed
            t.start();                                              //start the thread

            //Add shorcut to close Game when Ctrl+Shift+Q is pressed
            InputMap imActivate = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            imActivate.put(KeyStroke.getKeyStroke("ctrl shift pressed Q"), "CSQ");           //create mapping for Ctrl + Shift + Q (CSQ)
            panel.getActionMap().put("CSQ", new AbstractAction(){                                   //Action performed when CSQ is pressed
                public void actionPerformed(ActionEvent e) {
                    if(JOptionPane.showConfirmDialog(null, "<html>Are you sure you want to quit the game (Yes) or return (No)?<br>Note that all progress will be lost.", "Do you really want to leave the game?.", JOptionPane.YES_NO_OPTION)==0)
                    {
                        Controller.vOpenMainWindow(frmGame);
                        System.out.println("- User pressed: Ctrl + Shift + Q -> game will close.");
                    }
                }
            });

            //add objects to panel
            panel.add(btnReturnMenu);
            panel.add(btnManageCountry);
            panel.add(jlblDifficulty);
            panel.add(jlblTime);
            panel.add(jlblPoints);
            panel.add(jlblTotalCitizens);
            panel.add(jlblTotalInfected);
            panel.add(jlblTotalDead);

            //add panel to frame, display frame
            frmGame.add(panel);
            frmGame.setVisible(true);
        }
        public GameMapWindow(int intDifficultyLevel,JFrame frmFrame)
        {
            this(intDifficultyLevel);
            frmFrame.dispose();
        }
    }
    public static class BuyUpgradeWindow extends JFrame
    {
        public BuyUpgradeWindow(HashMap<String, Model.Country> dicCountries)
        {
            JFrame frmBuyUpgrades = new JFrame("Want to buy upgrades?!");     //descr. of frame
            frmBuyUpgrades.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);    //behaviour on close -> just close this frame
            frmBuyUpgrades.setSize(900,500);
            frmBuyUpgrades.setLocationRelativeTo(null);                        //set panel to center

            JPanel pMainPanel = new JPanel();
            pMainPanel.setBackground(new Color(207, 98, 255));
            pMainPanel.setLayout(new GridLayout(17,1));

            //Elements of Main Grid
            JLabel jlblHeader=new JLabel("Country summary after period: " + Model.Global.intCurrentPeriod);

            //Rows 2,3,4,5,6
            JPanel pSub1Grid2 = Controller.jpBuyUpgradeSub1GridR2to6(2,dicCountries);
            JPanel pSub1Grid3 = Controller.jpBuyUpgradeSub1GridR2to6(3,dicCountries);
            JPanel pSub1Grid4 = Controller.jpBuyUpgradeSub1GridR2to6(4,dicCountries);
            JPanel pSub1Grid5 = Controller.jpBuyUpgradeSub1GridR2to6(5,dicCountries);
            JPanel pSub1Grid6 = Controller.jpBuyUpgradeSub1GridR2to6(6,dicCountries);

            //Rows 7,8
            JComboBox jcbSelectCountry = Controller.jpBuyUpgradeComboBox(dicCountries);
            //JLabel jlblCountry = new JLabel("Country selection");
            JLabel jlblPointsToBuy = new JLabel("Points to spare on upgardes: " + Model.Global.intPointsForUpgrades);

            //Rows 9,10,11,12,13,14,15,16,17
            JPanel pSub1Grid9 = Controller.jpBuyUpgradeSub1GridR9to17(9);

            JPanel pSub1Grid10 = Controller.jpBuyUpgradeSub1GridR9to17(10);
            JPanel pSub1Grid11 = Controller.jpBuyUpgradeSub1GridR9to17(11);
            JPanel pSub1Grid12 = Controller.jpBuyUpgradeSub1GridR9to17(12);
            JPanel pSub1Grid13 = Controller.jpBuyUpgradeSub1GridR9to17(13);
            JPanel pSub1Grid14 = Controller.jpBuyUpgradeSub1GridR9to17(14);
            JPanel pSub1Grid15 = Controller.jpBuyUpgradeSub1GridR9to17(15);
            JPanel pSub1Grid16 = Controller.jpBuyUpgradeSub1GridR9to17(16);
            JPanel pSub1Grid17 = Controller.jpBuyUpgradeSub1GridR9to17(17);

            //Assign Action listener to JButtons
            JPanel[] arrPanelsWithButtons = {pSub1Grid9,pSub1Grid10,pSub1Grid11,pSub1Grid12,pSub1Grid13,pSub1Grid14,pSub1Grid15,pSub1Grid16,pSub1Grid17};   //Get array with panels

            for(int i=0;i<arrPanelsWithButtons.length;i++)                              //loop through all panels
            {
                Component[] arrPanelComp = arrPanelsWithButtons[i].getComponents();     //assign components from specific panel under array
                for (int j = 0;j<arrPanelComp.length;j++)                               //loop through those components
                {
                    if (arrPanelComp[j] instanceof JButton)                             //Check if component is JButton
                    {
                        int intUpgradeNumber = i+1;                                     //I am keeping indexing of upgrades starting 1 not 0
                        JButton tempJButton=(JButton) arrPanelComp[j];                  //if it is cast it under temp variable
                        tempJButton.addActionListener(new ActionListener()              //add action listener
                        {
                            @Override
                            public void actionPerformed(ActionEvent e)
                            {
                                boolean blnBought=Controller.vBuyUpgradeButtonAction(intUpgradeNumber,jcbSelectCountry.getSelectedItem().toString(),dicCountries);
                                if(blnBought)
                                {
                                    JOptionPane.showMessageDialog(null, "Upgrade was bought!.");
                                } else {
                                    JOptionPane.showMessageDialog(null, "Upgrade was not bought.");
                                }
                                System.out.println("- User wants to buy upgrade.");
                            }
                        });
                    }
                }
            }

            //Finalize Main Grid
            pMainPanel.add(jlblHeader);         //R1
            pMainPanel.add(pSub1Grid2);         //R2
            pMainPanel.add(pSub1Grid3);         //R3
            pMainPanel.add(pSub1Grid4);         //R4
            pMainPanel.add(pSub1Grid5);         //R5
            pMainPanel.add(pSub1Grid6);         //R6
            pMainPanel.add(jcbSelectCountry);   //R7
            pMainPanel.add(jlblPointsToBuy);    //R8
            pMainPanel.add(pSub1Grid9);         //R9
            pMainPanel.add(pSub1Grid10);        //R10
            pMainPanel.add(pSub1Grid11);        //R11
            pMainPanel.add(pSub1Grid12);        //R12
            pMainPanel.add(pSub1Grid13);        //R13
            pMainPanel.add(pSub1Grid14);        //R14
            pMainPanel.add(pSub1Grid15);        //R15
            pMainPanel.add(pSub1Grid16);        //R16
            pMainPanel.add(pSub1Grid17);        //R17

            //Display frame
            frmBuyUpgrades.add(pMainPanel);
            frmBuyUpgrades.setVisible(true);
        }
    }
    public static class ScoreSaveWindow extends JFrame
    {
        public ScoreSaveWindow(Model.DataForSummary objSummary)
        {
            //Prepare Frame
            JFrame frmSaveScore = new JFrame("Save your scrore!");     //descr. of frame
            frmSaveScore.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE );    //behaviour on close -> ignore -> force choosing from buttons
            frmSaveScore.setSize(700,500);
            frmSaveScore.setLocationRelativeTo(null);                        //set panel to center

            //Prepare Panels
            JPanel MainGridPanel = new JPanel();
            MainGridPanel.setBackground(new Color(207, 98, 255));
            MainGridPanel.setLayout(new GridLayout(2,1));

            JPanel Sub1GridCol2Panel = new JPanel();
            Sub1GridCol2Panel.setBackground(new Color(207, 98, 255));
            Sub1GridCol2Panel.setLayout(new GridLayout(1,2));

            JPanel Sub2GridRow4Panel = new JPanel();
            Sub2GridRow4Panel.setBackground(new Color(255, 151, 232));
            Sub2GridRow4Panel.setLayout(new GridLayout(4,1));

            JPanel Sub2GridRow6LPanel = new JPanel();
            Sub2GridRow6LPanel.setBackground(new Color(255, 151, 232));
            Sub2GridRow6LPanel.setLayout(new GridLayout(8,1));

            JPanel Sub3GridCol2Panel = new JPanel();
            Sub3GridCol2Panel.setBackground(new Color(255, 151, 232));
            Sub3GridCol2Panel.setLayout(new GridLayout(1,2));

            JPanel FlowPanelButtons = new JPanel();
            FlowPanelButtons.setBackground(new Color(255, 151, 232));
            FlowPanelButtons.setLayout(new FlowLayout());

            //Prepare Sub3GridPanel layout, row 1 of main grid
            JLabel lblScore = new JLabel("Your score is:");
            JLabel lblScoreAchieved = new JLabel();
            lblScoreAchieved.setText(Long.toString(objSummary.GetAcquiredPoints()));

            //Prepare and finalize Sub2GridPanel panel
            JLabel lblInput = new JLabel("<html>Please sign for your score! <br> Please use characters a-Z and numbers, no special signs. <br> Maximum number of characters is 15.");
            TextField txtInput = new TextField("");
            txtInput.addKeyListener(new java.awt.event.KeyAdapter()
            {
                public void keyTyped(java.awt.event.KeyEvent e)
                {
                    if (txtInput.getText().length() >= 15 )
                    {
                        JOptionPane.showMessageDialog(null,"<html>Too many characters! <br>Please limit your nickname to 15 signs.","Too many signs!",JOptionPane.ERROR_MESSAGE);
                        System.out.println("- User tried to insert nickname with more than 15 characters");
                        e.consume();        //block action -> block further key presses
                    }
                    int intASCII = (int)e.getKeyChar();
                    if(!(47<intASCII && intASCII<58) && !(64<intASCII && intASCII<91) && !(96<intASCII && intASCII<123))    //Validate ASCII codes of inserted signs
                    {
                        JOptionPane.showMessageDialog(null,"<html>Forbidden character was used! <br>Please use characters a-Z and numbers, no special signs.","Incorrect signs!",JOptionPane.ERROR_MESSAGE);
                        System.out.println("- User tried to user forbidden sign");
                        e.consume();        //block action -> block further key presses
                    }
                }
            });

            //Prepare Flow panel
            JButton btnSave = new JButton("Save score!");
            btnSave.setPreferredSize(new Dimension(150, 50));
            btnSave.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    boolean blnRecordAdded=false;
                    String strNickname = txtInput.getText();            //get text input by the user
                    String strHighScore=lblScoreAchieved.getText();     //get the score
                    if(strNickname.equals(""))
                    {
                        System.out.println("- User did not enter nickname");
                        JOptionPane.showMessageDialog(null, "<html>Nickname can't be empty!<br>Please insert something or return to menu.");
                    } else {
                        System.out.println("- User nickname is: " + strNickname);
                        Model.DataForScoreSave objNewRecordData = new Model.DataForScoreSave(strNickname,strHighScore);
                        blnRecordAdded=Controller.blnHandleAddingNewScore(objNewRecordData,frmSaveScore);
                    }
                    if(blnRecordAdded)              //if record was added, close SaveScore frame and open main menu
                    {
                        Controller.vOpenMainWindow(frmSaveScore);
                        System.out.println("- User saved score and was moved back to main screen.");
                    }
                }
            });
            JButton btnExit = new JButton("Exit without saving");
            btnExit.setPreferredSize(new Dimension(150, 50));
            btnExit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    SwingUtilities.invokeLater(new Runnable()
                    {
                        @Override
                        public void run() {
                            new MainWindow(frmSaveScore);
                        }
                    });
                    System.out.println("- User did not save score and was moved back to main screen.");
                }
            });
            FlowPanelButtons.add(btnSave);
            FlowPanelButtons.add(btnExit);

            //Finalize Sub3GridCol2Panel panel
            Sub3GridCol2Panel.add(lblScore);
            Sub3GridCol2Panel.add(lblScoreAchieved);

            //Finalize Sub2GridRow4Panel panel
            Sub2GridRow4Panel.add(Sub3GridCol2Panel);
            Sub2GridRow4Panel.add(lblInput);
            Sub2GridRow4Panel.add(txtInput);
            Sub2GridRow4Panel.add(FlowPanelButtons);

            //Finalize Sub2GridRow6LPanel panel
            JLabel jlblHeader = new JLabel("Data regarding population");
            JLabel jlblPopulation = new JLabel("Population alive: " + objSummary.getCitizensTotal());
            JLabel jlblTotalInfectedEnd = new JLabel("Infected (end): " + objSummary.getPeopleInfectedEnd());
            JLabel jlblTotalInfectedWhole = new JLabel("Infected (total): " + objSummary.getPeopleInfectedTotal());
            JLabel jlblTotalDead = new JLabel("Dead: " + objSummary.getPeopleDeadTotal());
            JLabel jlblTotalCured = new JLabel("Cured: " + objSummary.getPeopleCured());
            JLabel jlblGameDuration = new JLabel("Game time: " + objSummary.getGameDuration());
            JLabel jlblDifficulty = new JLabel("Difficulty: " + objSummary.getGameDifficulty());
            System.out.println(objSummary.getCitizensTotal());
            System.out.println(objSummary.getPeopleInfectedEnd());
            System.out.println(objSummary.getPeopleInfectedTotal());
            System.out.println(objSummary.getPeopleDeadTotal());

            Sub2GridRow6LPanel.add(jlblHeader);
            Sub2GridRow6LPanel.add(jlblPopulation);
            Sub2GridRow6LPanel.add(jlblTotalInfectedEnd);
            Sub2GridRow6LPanel.add(jlblTotalInfectedWhole);
            Sub2GridRow6LPanel.add(jlblTotalDead);
            Sub2GridRow6LPanel.add(jlblTotalCured);
            Sub2GridRow6LPanel.add(jlblGameDuration);
            Sub2GridRow6LPanel.add(jlblDifficulty);

            //Finalize Sub1GridCol2Panel panel
            Sub1GridCol2Panel.add(Sub2GridRow6LPanel);
            Sub1GridCol2Panel.add(Sub2GridRow4Panel);

            //Finalize MainGridPanel
            JLabel lblWinLosePic=new JLabel("Picture was not found.");
            try {
                String strWinLoseGraphic=Controller.strValidateAndGetPicPathSaveScore(objSummary);
                if(strWinLoseGraphic!=null)
                {
                    BufferedImage imgCountryPicture = ImageIO.read(new File(strWinLoseGraphic));
                    lblWinLosePic = new JLabel(new ImageIcon(imgCountryPicture));
                } else {
                    JOptionPane.showMessageDialog(null, "<html>Graphic for Win/Lose was not found.<br>Desired directory with graphics is: " + System.getProperty ("user.dir") +".", "Missing graphic!",    JOptionPane.WARNING_MESSAGE);
                    System.out.println("- Graphic for Win/Lose status was not found.");
                }
            } catch (IOException ex) {
                System.out.println("- Error occured when loading pictures.");
            }
            MainGridPanel.add(lblWinLosePic);       //insert Win/Lose picture
            MainGridPanel.add(Sub1GridCol2Panel);   //insert subgrid into main grid

            //Combine rest and display
            frmSaveScore.add(MainGridPanel);        //insert grid panel to frame
            frmSaveScore.setVisible(true);
        }
        public ScoreSaveWindow(Model.DataForSummary objSummary,JFrame frmFrame)
        {
            this(objSummary);
            frmFrame.dispose();
        }
    }
    public static class SelectHighScoreFileWindow extends JFrame
    {
        public SelectHighScoreFileWindow(String strScoreFile, JFrame frmSaveHighScore, Model.DataForScoreSave objNewRecordData)
        {
            //Prepare Frame
            JFrame frmSelectHighScore = new JFrame("What to do with missing score file?");    //descr. of frame
            frmSelectHighScore.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);              //I don't want to let user to close window -> force choice
            frmSelectHighScore.setSize(500,150);
            int intButtWidth=130;
            int intButtHeight=30;
            objNewRecordData.setScoreFilePath(strScoreFile);

            //Prepare Panels
            JPanel MainGridPanel = new JPanel();
            MainGridPanel.setBackground(new Color(207, 98, 255));
            MainGridPanel.setLayout(new GridLayout(2,1));

            JPanel FlowPanel = new JPanel();
            FlowPanel.setBackground(new Color(207, 98, 255));
            FlowPanel.setLayout(new FlowLayout());

            //Prepare Gridlayout, row 1
            JLabel lblInput = new JLabel("<html>File with HighScore was not found! <br>Expected location: " +objNewRecordData.strGetScoreFilePath() + "<br> Please: select the file, directory where file will be created or quit.");
            MainGridPanel.add(lblInput);
            //Prepare Flowlayout -> row 2
            JButton btnSelectFile = new JButton("Select file");
            btnSelectFile.setPreferredSize(new Dimension(intButtWidth, intButtHeight));
            btnSelectFile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    int intResponse = Controller.blnHandleScoreAddForSelectedFile(objNewRecordData,frmSelectHighScore);
                    switch(intResponse) {
                        case 0:
                            JOptionPane.showMessageDialog(null, "File was not selected. Please select the file or return to menu.");
                            System.out.println("- File with ScoreBoard was not updated -> no file was chosen.");
                            break;
                        case 1:
                            JOptionPane.showMessageDialog(null, "<html>Incorrect file was selected!<br> Please select file again or return to menu.");
                            System.out.println("- File with ScoreBoard was not updated -> incorrect file.");
                            break;
                        case 2:
                            JOptionPane.showMessageDialog(null, "<html>Incorrect file was selected (wrong extension)!<br> Please select file again or return to menu.");
                            System.out.println("- File with ScoreBoard was not updated -> incorrect file.");
                            break;
                        case 3:
                            frmSaveHighScore.dispose();
                            JOptionPane.showMessageDialog(null, "<html>Record was successfully added to ScoreBoard.<br> Location: " + objNewRecordData.strGetScoreFilePath());
                            Controller.vOpenMainWindow(frmSelectHighScore);
                            System.out.println("- File with ScoreBoard was updated! Record was saved!");
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "<html>There was unexpected error.<br>ScoreBoard was not updated.<br>Please try again.");
                            System.out.println("- Unexpected error, when selecting ScoreBoard file.");
                    }
                }
            });
            JButton btnSelectDirectory = new JButton("Select directory");
            btnSelectDirectory.setPreferredSize(new Dimension(intButtWidth, intButtHeight));
            btnSelectDirectory.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    if(Controller.blnHandleScoreAddForGivenDirectory(objNewRecordData,frmSelectHighScore))
                    {
                        frmSaveHighScore.dispose();
                        JOptionPane.showMessageDialog(null, "<html>Record was successfuly added to ScoreBoard.<br> Location: " + objNewRecordData.strGetScoreFilePath());
                        Controller.vOpenMainWindow(frmSelectHighScore);
                        System.out.println("- File with highscore was created, record was saved!");
                    }else{
                        JOptionPane.showMessageDialog(null, "For some reason, file with scoreboard was not created, try again.");
                        System.out.println("- File with highscore was not created!");
                    }
                }
            });

            JButton btnReturnMenu = new JButton("Return menu");
            btnReturnMenu.setPreferredSize(new Dimension(intButtWidth, intButtHeight));
            btnReturnMenu.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    frmSaveHighScore.dispose();                                     //user decided to go back to menu -> close "save score window"
                    Controller.vOpenMainWindow(frmSelectHighScore);
                }
            });

            FlowPanel.add(btnSelectFile);
            FlowPanel.add(btnSelectDirectory);
            FlowPanel.add(btnReturnMenu);

            //Combine all and display
            MainGridPanel.add(FlowPanel);
            frmSelectHighScore.add(MainGridPanel);           //insert grid panel to frame
            frmSelectHighScore.setVisible(true);
        }
        public SelectHighScoreFileWindow(JFrame frmFrame, String strScoreFile,JFrame frmSaveHighScore,Model.DataForScoreSave objNewRecordData)
        {
            this(strScoreFile,frmSaveHighScore,objNewRecordData);
            frmFrame.dispose();
        }
    }
}
