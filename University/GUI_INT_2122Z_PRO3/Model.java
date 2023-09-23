import com.sun.prism.paint.ImagePattern;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

//Objects
public class Model
{

    public static class Global
    {
        //Globals to be modified at the beginning of the game
        public static long lngGameStart;
        public static long lngCurrentTime;
        public static long lngPlayedTime;
        public static boolean blnGameStatus=false;                  //Win = True, Loose = False
        public static int intLifes=10;
        public static  long lngScore=0;
        public static  int intKilledWhiteDucks=0;
        public static  int intKilledRedDucks=0;
        public static  int intKilledBlackDucks=0;
        public static int intDucksSpawned=0;
        public static boolean blnEndGame=false;

        //Other globals
        public static int intDifficulty=0;      //0 - easy, 1 - normal, 2 - hard
        public static int intSpawnInterval=3;   //number of seconds between spawn of new duck
    }
    public static class DataForScoreSave
    {
        //Object stores data needed for new score record -> Nickname and Score
        private String strNickName;
        private String strScoreAchieved;
        private String strScoreDataFilePath;
        DataForScoreSave(String strNick, String strScore)   //Constructor
        {
            this.strNickName=strNick;
            this.strScoreAchieved=strScore;
            this.strScoreDataFilePath="ScoreBoard.txt";     //default path -> location of the project
        }
        public String strGetNick()
        {
            return this.strNickName;
        }
        public String strGetScore()
        {
            return this.strScoreAchieved;
        }
        public void setScoreFilePath(String strFilePath)
        {
            //Return path to the ScoreBoard file
            this.strScoreDataFilePath=strFilePath;
        }
        public String strGetScoreFilePath()
        {
            return strScoreDataFilePath;
        }
    }
    public static class Duck extends ImageView
    {
        //Paths to graphics
        private String strPathWhiteLeft=System.getProperty("user.dir") + "\\graphics\\DuckLeftWhite.png";
        private String strPathWhiteRight=System.getProperty("user.dir") + "\\graphics\\DuckRightWhite.png";
        private String strPathRedLeft=System.getProperty("user.dir") + "\\graphics\\DuckLeftRed.png";
        private String strPathRedRight=System.getProperty("user.dir") + "\\graphics\\DuckRightRed.png";
        private String strPathBlackLeft=System.getProperty("user.dir") + "\\graphics\\DuckLeftBlack.png";
        private String strPathBlackRight=System.getProperty("user.dir") + "\\graphics\\DuckRightBlack.png";

        //Default number of HPs for each type of duck
        private int intWhiteDuckDefaultHP=1;
        private int intRedDuckDefaultHP=3;
        private int intBlackDuckDefaultHP=5;

        //Duck attributes
        private int intDuckLifes;
        private boolean blnAlive=true;  //from default duck is alive
        private int intSide;
        private int intType;

        //intType: 0-White Duck, 1-Red Duck, 2-Black Duck
        //intSide, start: 0-Left Duck, 1-Right
        Duck(int intType, int intSide, Group group)
        {
            this.intType=intType;
            this.intSide=intSide;
            switch(intType)                 //determine proper graphic
            {
                case 0: //White duck
                    this.intDuckLifes=this.intWhiteDuckDefaultHP;
                    switch(intSide)
                    {
                        case 0:     //Left
                            Controller.vSetImage(strPathWhiteLeft,this);
                            break;
                        case 1:
                            Controller.vSetImage(strPathWhiteRight,this);
                            break;
                    }
                    break;
                case 1: //Red duck
                    this.intDuckLifes=this.intRedDuckDefaultHP;
                    switch(intSide)
                    {
                        case 0:
                            Controller.vSetImage(strPathRedLeft,this);
                            break;
                        case 1:
                            Controller.vSetImage(strPathRedRight,this);
                            break;
                    }
                    break;
                case 2: //Black duck
                    this.intDuckLifes=this.intBlackDuckDefaultHP;
                    switch(intSide)
                    {
                        case 0:
                            Controller.vSetImage(strPathBlackLeft,this);
                            break;
                        case 1:
                            Controller.vSetImage(strPathBlackRight,this);
                            break;
                    }
                    break;
            }
            this.setPreserveRatio(true);    //preserve ratio of the image
            this.setFitHeight(50);          //set size
            group.getChildren().add(this);  //add to stage
        }
        public void GotHit()
        {
            //Reduce number of HP
            this.intDuckLifes=this.intDuckLifes-1;
            System.out.println("- Duck got hit, good work!");
            if(this.intDuckLifes==0)                //change status to dead if HP drops to 0
            {
                switch(intType)                     //count duck kills
                {
                    case 0:
                        Global.intKilledWhiteDucks=Global.intKilledWhiteDucks+1;
                        break;
                    case 1:
                        Global.intKilledRedDucks=Global.intKilledRedDucks+1;
                        break;
                    case 2:
                        Global.intKilledBlackDucks=Global.intKilledBlackDucks+1;
                        break;
                }
                this.blnAlive=false;
            }
        }
        public boolean StillAlive()
        {
            //Checks if duck is alive -> HP > 0
            return blnAlive;
        }
        public void Escaped()
        {
            //If duck escaped, reduce number ofHP
            Model.Global.intLifes=Model.Global.intLifes-1;
        }
        public boolean Right()
        {
            //returns false if starts Left, true if starts right
            boolean blnResult=false;
            if(this.intSide==1)
            {
                blnResult=true;
            }
            return blnResult;
        }
    }
}
