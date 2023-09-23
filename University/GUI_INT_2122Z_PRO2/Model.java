import java.util.HashMap;

public class Model
{
    public static class Global
    {
        public static long lngGameStart;
        public static long lngElapsedTime;
        public static boolean blnGameWon;
        public static boolean blnGameLost;
        public static int intInterval1;                         //
        public static int intInterval2;
        public static int intSicknessPeriod;                  //How long person is sick and then heals or dies
        public static int intCurrentPeriod;
        public static int intDifficulty;
        public static int intPointsForUpgrades;
        public static double dblPointRatioForAvoidInfec;
        public static double dblPointRatioForHealing;
        public static int intGlobalPopulation;
        public static int intGlobalInfected;                    //Current
        public static int intGlobalDead;
        public static long lngTotalInfected;                    //Cummulative infected count
        public static long lngTotalCured;
    }
    public static class PlagueVirus
    {
        private double dblInfectionEfficiency;                                  //Infection in %
        private double dblHealChance;                                           //Heal in %
        private int intVirusSpread;                                             //How many persons it may affect
        PlagueVirus()                                                           //Constructor
        {
            this.dblInfectionEfficiency=(45+ Global.intDifficulty*5)/100.0;       //higher difficulty increases infection chance
            this.dblHealChance=0.6-(Global.intDifficulty*0.1);                  //higher difficulty decreases heal chance
            this.intVirusSpread=3*Global.intDifficulty;                        //higher difficulty increases infection spread
        }
        public int getVirusSpread()
        {
            return this.intVirusSpread;
        }
        public double getDblInfectionEfficiency()
        {
            return dblInfectionEfficiency;
        }
        public double getDblHealChance()
        {
            return dblHealChance;
        }
        public void setDecreaseVirusInfectionEfficiency(double dblDecrease)
        {
            this.dblInfectionEfficiency=this.dblInfectionEfficiency-dblDecrease;
        }
        public void setIncreaseVirusHealChance(double dblIncrease)
        {
            this.dblHealChance=this.dblHealChance+dblIncrease;
        }
        public void setDecreaseVirusSpread(int intDecrease)
        {
            this.intVirusSpread=this.intVirusSpread-intDecrease;
        }
    }
    public static class Country extends PlagueVirus                 //every country has Virus, but its efficiency differ because of upgrades.
    {
        //General parameters
        private String strCountryName;
        private int intPopulation;
        private int intInfected;                                    //Total infected
        private int intDead=0;                                      //default no dead
        private int intCured=0;                                     //default no cured
        private HashMap<Integer, Integer> dicInfectedPerPeriod;     //Key -> specific period, Value -> How many persons got infected (new)
        private boolean blnCountryAlive=true;                       //If all infected/dead -> country cease to exists

        //Transportation form -> from default all transporation forms are available
        private boolean blnPlanes=true;
        private boolean blnShips=true;
        private boolean blnCar=true;

        //Upgrades -> from default nothing is researched
        private boolean blnHelmets=false;                       //Affect dblInfectionEfficiency
        private boolean blnMasks=false;                         //Affect dblInfectionEfficiency
        private boolean blnSocialDistance=false;                //Affect intVirusSpread
        private boolean blnVirusAwareness=false;                //Affect intVirusSpread
        private boolean blnVaccine1=false;                      //Affect dblInfectionEfficiency & dblHealChance
        private boolean blnVaccine2=false;                      //Affect dblInfectionEfficiency & dblHealChance
        private boolean blnMedicalHardwareUpgrade=false;        //Affect dblHealChance
        private boolean blnDoctorKnowledge=false;               //Affect dblHealChance
        private boolean blnQuickerReaction=false;               //Affect dblHealChance

        Country(String strCountryName, int intPopulation, int intInfected)              //Constructor
        {
            super();                                                        //run constructor from parent class
            this.strCountryName=strCountryName;
            this.intPopulation=intPopulation;
            this.intInfected=intInfected;
            this.dicInfectedPerPeriod = new HashMap<Integer, Integer>();
            dicInfectedPerPeriod.put(1,intInfected);
            Global.intGlobalPopulation=Global.intGlobalPopulation+intPopulation;
            Global.intGlobalInfected=Global.intGlobalInfected+intInfected;
        }
        public void vRecalculateInfection()
        {
            if(this.blnCountryAlive)
            {
                //If there are more infected than citizens -> all citizens are infected -> country is dead
                if(this.intInfected>=this.intPopulation)    //everyone is infected -> country lost
                {
                    setCountryDead();
                }
                if(blnCountryAlive)
                {
                    int intNewPotentialInfected=this.intInfected*getVirusSpread();                  //how many persons had contact with the virus
                    //Everyone might be infected
                    if(intNewPotentialInfected>intPopulation-intDead)
                    {
                        intNewPotentialInfected=intPopulation-intDead;
                    }
                    int intNewInfected=(int)(intNewPotentialInfected*getDblInfectionEfficiency());  //how many persons got infected -> cast to int
                    int intAvoidedInfection=intNewPotentialInfected-intNewInfected;                 //had cont with vir wasn't infected -> "os. uratowana przed zarażeniem"
                    dicInfectedPerPeriod.put(Global.intCurrentPeriod,intNewInfected);               //Add new infected for specific period
                    this.intInfected= this.intInfected+intNewInfected;                              //Change total infected for country
                    Global.intGlobalInfected=Global.intGlobalInfected+intNewInfected;               //Change global infected count
                    Global.lngTotalInfected= Global.lngTotalInfected+intNewInfected;                //Infected cummulative
                    Global.intPointsForUpgrades=Global.intPointsForUpgrades+(int)(intAvoidedInfection*Global.dblPointRatioForAvoidInfec);   //Calculate points for upgrades
                }
            }
        }
        public void vRecalculateHealDead()
        {
            if (this.blnCountryAlive)
            {
                int intJudgementPeriod = Global.intCurrentPeriod - Global.intSicknessPeriod;    //Get period for which program judges cured or dead
                int intInfectedForPeriod = this.dicInfectedPerPeriod.get(intJudgementPeriod);   //Get infected from specific period
                int intCured = (int) (intInfectedForPeriod * getDblHealChance());               //Number of people who were cured from specific period
                int intDead = intInfectedForPeriod - intCured;                                  //Number of ppeople who died from specific period
                this.intCured=this.intCured+intCured;
                Global.lngTotalCured=Global.lngTotalCured+intCured;
                this.intInfected = this.intInfected - intInfectedForPeriod;                     //Infected were resolved -> Dead/Cured
                Global.intGlobalInfected=Global.intGlobalInfected-intInfectedForPeriod;         //Change global infected count
                this.intDead = this.intDead + intDead;                                          //increase country count of dead
                Global.intGlobalDead=Global.intGlobalDead+intDead;                              //Change global dead count
                this.intPopulation=this.intPopulation-intDead;
                Global.intGlobalPopulation=Global.intGlobalPopulation-intDead;                                                  //Change global population count
                Global.intPointsForUpgrades = Global.intPointsForUpgrades + (int) (intCured * Global.dblPointRatioForHealing);  //update points for curing citizens
            }
        }
        public boolean getCountryStatus()
        {
            //
            return blnCountryAlive;
        }
        public int getPopulation()
        {
            //
            return this.intPopulation;
        }
        public int getInfected()
        {
            //
            return this.intInfected;
        }
        public int getDead()
        {
            //
            return this.intDead;
        }
        public int getCured()
        {
            //
            return this.intCured;
        }
        public void setCountryDead()
        {
            //
            this.blnCountryAlive=false;
        }

        //Manage upgrades
        public boolean UpgradeHelmets()
        {
            boolean blnResult=false;
            if(blnHelmets==false)
            {
                blnHelmets=true;                                                                    //change status
                Global.intPointsForUpgrades= Global.intPointsForUpgrades-DataUpgrades.intUpg1Cost;  //decrease number of points
                setDecreaseVirusInfectionEfficiency(0.1);
                blnResult=true;
            }
            return blnResult;
        }
        public boolean UpgradeMasks()
        {
            boolean blnResult=false;
            if(blnMasks==false)
            {
                blnMasks=true;                                                                    //change status
                Global.intPointsForUpgrades= Global.intPointsForUpgrades-DataUpgrades.intUpg2Cost;  //decrease number of points
                setDecreaseVirusInfectionEfficiency(0.2);
                blnResult=true;
            }
            return blnResult;
        }
        public boolean UpgradeSocialDistance()
        {
            boolean blnResult=false;
            if(blnSocialDistance==false)
            {
                blnSocialDistance=true;                                                                    //change status
                Global.intPointsForUpgrades= Global.intPointsForUpgrades-DataUpgrades.intUpg3Cost;  //decrease number of points
                setDecreaseVirusSpread(10);
                blnResult=true;
            }
            return blnResult;
        }
        public boolean UpgradeVirusAwareness()
        {
            boolean blnResult=false;
            if(blnVirusAwareness==false)
            {
                blnVirusAwareness=true;                                                                    //change status
                Global.intPointsForUpgrades= Global.intPointsForUpgrades-DataUpgrades.intUpg4Cost;  //decrease number of points
                setDecreaseVirusSpread(15);
                blnResult=true;
            }
            return blnResult;
        }
        public boolean UpgradeVaccine1()
        {
            boolean blnResult=false;
            if(blnVaccine1==false)
            {
                blnVaccine1=true;                                                                    //change status
                Global.intPointsForUpgrades= Global.intPointsForUpgrades-DataUpgrades.intUpg5Cost;  //decrease number of points
                setDecreaseVirusInfectionEfficiency(0.1);
                setIncreaseVirusHealChance(0.1);
                blnResult=true;
            }
            return blnResult;
        }
        public boolean UpgradeVaccine2()
        {
            boolean blnResult=false;
            if(blnVaccine2==false)
            {
                blnVaccine2=true;                                                                    //change status
                Global.intPointsForUpgrades= Global.intPointsForUpgrades-DataUpgrades.intUpg6Cost;  //decrease number of points
                setDecreaseVirusInfectionEfficiency(0.1);
                setIncreaseVirusHealChance(0.1);
                blnResult=true;
            }
            return blnResult;
        }
        public boolean UpgradeMedicalHardware()
        {
            boolean blnResult=false;
            if(blnMedicalHardwareUpgrade==false)
            {
                blnMedicalHardwareUpgrade=true;                                                                    //change status
                Global.intPointsForUpgrades= Global.intPointsForUpgrades-DataUpgrades.intUpg7Cost;  //decrease number of points
                setIncreaseVirusHealChance(0.1);
                blnResult=true;
            }
            return blnResult;
        }
        public boolean UpgradeDoctorKnowledge()
        {
            boolean blnResult=false;
            if(blnDoctorKnowledge==false)
            {
                blnDoctorKnowledge=true;                                                                    //change status
                Global.intPointsForUpgrades= Global.intPointsForUpgrades-DataUpgrades.intUpg8Cost;  //decrease number of points
                setIncreaseVirusHealChance(0.1);
                blnResult=true;
            }
            return blnResult;
        }
        public boolean UpgradeQuickerReaction()
        {
            boolean blnResult=false;
            if(blnQuickerReaction==false)
            {
                blnQuickerReaction=true;                                                                    //change status
                Global.intPointsForUpgrades=Global.intPointsForUpgrades-DataUpgrades.intUpg9Cost;  //decrease number of points
                setIncreaseVirusHealChance(0.1);
                blnResult=true;
            }
            return blnResult;
        }
        //Block specific transportation method
        public void setBlockPlane()
        {
            blnPlanes=false;
        }
        public void setBlockShip()
        {
            blnShips=false;
        }
        public void setBlockCar()
        {
            blnCar=false;
        }
    }
    public static class DataUpgrades
    {
        //Alternatywnie stworzyć własny obiekt 2 elementowy w tablicy 9 wymiarowej
        public static String strUpg1Name="Helmets";
        public static String strUpg2Name="Masks";
        public static String strUpg3Name="Social distancing";
        public static String strUpg4Name="Virus awareness";
        public static String strUpg5Name="Vaccine 1";
        public static String strUpg6Name="Vaccine 2";
        public static String strUpg7Name="Medical hardware upg.";
        public static String strUpg8Name="Dr knowledge";
        public static String strUpg9Name="Quciker reaction";
        public static int intUpg1Cost=1000;
        public static int intUpg2Cost=2000;
        public static int intUpg3Cost=3000;
        public static int intUpg4Cost=4000;
        public static int intUpg5Cost=5000;
        public static int intUpg6Cost=6000;
        public static int intUpg7Cost=7000;
        public static int intUpg8Cost=8000;
        public static int intUpg9Cost=9000;
    }
    public static class DataForSummary
    {
        private boolean blnWin;
        private int intPeopleInTheWorld;
        private long lngPeopleInfectedEnd;
        private long lngTotalInfectedTotal;
        private int intPeopleDead;
        private long lngPeopleHealed;
        private long lngPointsAcquired;
        private long lngGameTime;
        private int intDifficulty;

        DataForSummary(boolean blnWin, HashMap<String, Model.Country> dicCountries)      //Constructor
        {
            this.blnWin=blnWin;
            this.intPeopleInTheWorld=Global.intGlobalPopulation;
            this.lngPeopleInfectedEnd=Global.intGlobalInfected;
            this.lngTotalInfectedTotal=Global.lngTotalInfected;
            this.intPeopleDead=Global.intGlobalDead;
            this.lngPeopleHealed=Global.lngTotalCured;
            this.lngGameTime=Global.lngElapsedTime;
            this.intDifficulty=Global.intDifficulty;
            this.lngPointsAcquired=lngCalculatePoints();
        }
        private long lngCalculatePoints()
        {
            //Ranking liczony jest na podstawie czasu, uzyskanych efektów i stopnia trudności (dowolny sposób implementacji).
            long lngResult=this.intPeopleInTheWorld-this.intPeopleDead-this.lngGameTime+(int)(this.intDifficulty*0.5*this.lngGameTime);
            return lngResult;
        }
        public boolean GetWinLoseStatus()   //How did the game end?
        {
            return blnWin;
        }
        public int getCitizensTotal()
        {
            //
            return this.intPeopleInTheWorld;
        }
        public long getPeopleInfectedEnd()
        {
            //
            return this.lngPeopleInfectedEnd;
        }
        public long getPeopleInfectedTotal()
        {
            //
            return lngTotalInfectedTotal;
        }
        public long getPeopleDeadTotal()
        {
            //
            return this.intPeopleDead;
        }
        public long getPeopleCured()
        {
            //
            return this.lngPeopleHealed;
        }
        public long getGameDuration()
        {
            //
            return this.lngGameTime;
        }
        public String getGameDifficulty()
        {
            String strResult=Controller.strGetLevelDifficulty();
            return strResult;
        }
        public long GetAcquiredPoints()
        {
            //
            return lngPointsAcquired;
        }
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
            this.strScoreDataFilePath="ScoreBoard.txt";     //default path
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
            this.strScoreDataFilePath=strFilePath;
        }
        public String strGetScoreFilePath()
        {
            return strScoreDataFilePath;
        }
    }
}
