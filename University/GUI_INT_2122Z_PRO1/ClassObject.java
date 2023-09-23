import java.util.HashSet;
import java.util.HashMap;                   //import the HashMap class
import java.util.Date;                      //import date formats
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.io.File;

public class ClassObject
{
    public static class Estate
    {
        private int intId;                                          //unique Id of this estate (each estate in the whole program has unique Id)
        private HashSet<Integer> Blocks = new HashSet<Integer>();           //Hashset which stores Id of Blocks in this Estate
        Estate(HashMap<Integer, ClassObject.Estate> EstateMap)      //Constructor, provide HashMap of Estates
        {
            this.intId=ProvideFreeEstateId(EstateMap);              //Find and assign new Id
            EstateMap.put(this.intId,this);                         //Put into HashMap under new Id, this Estate
        }
        public void AddBlock(int intBlockId)                        //Add Block to estate, public methods must be called by creating objects
        {
            //Method to validate if Block was already added to Estate, if not add it
            if(!this.Blocks.contains(intBlockId))
            {
                this.Blocks.add(intBlockId);
            } else {
                System.out.println("Block of Id: " + intBlockId + " is already assigned to Estate.");
            }
        }
        public void RemoveBlock(int intBlockId)                     //Remove Block from estate
        {
            //Check if block of specific Id is already in the estate, if so delete
            if(this.Blocks.contains(intBlockId))
            {
                this.Blocks.remove(intBlockId);
            } else {
                System.out.println("Block of Id: " + intBlockId + " does not exist in this Estate.");
            }
        }
        public void ReadBlock(HashSet<Integer> inputBlocks)
        {
            //Print values of the Block in the console
            for(int i:Blocks)
            {
                if(!inputBlocks.contains(i))
                {
                    inputBlocks.add(i);
                }
            }
        }
        public void SetId(int intId)
        {
            //Assign Id of the estate
            this.intId=intId;
        }
        public void ReadId()
        {
            //Read Id of the estate
            System.out.println(intId);
        }
        public String toString()
        {
            String strReturn="";
            strReturn= "Estate of id: " + this.intId + "\nContains blocks of the following Ids: ";
            for(int i:Blocks)
            {
                strReturn=strReturn + i + ", ";
            }
            strReturn=strReturn+"\n";
            return strReturn;
        }
    }
    public static class Block
    {
        private int intId;                                              //unique Id of this Block (each block in the whole program has unique Id), count starts with 0
        private int intEstateId;                                        //Stores Id of estate to which block belongs
        private HashSet<Integer> Mieszkania = new HashSet<Integer>();   //Hashset which stores Id of Mieszkanie in this Block
        Block(HashMap<Integer, ClassObject.Block> BlockMap)             //Constructor
        {
            this.intId=ProvideFreeBlockId(BlockMap);                    //Find and assign new Id
            BlockMap.put(this.intId,this);                              //Put into HashMap under new Id, this Estate
        }
        public void SetId(int intId)
        {
            //Set Id of the Block
            this.intId=intId;
        }
        public void ReadId()
        {
            //Read Id of the Block
            System.out.println(intId);
        }
        public void SetEstateId(int intId)
        {
            //Set Id of the Estate
            this.intEstateId=intId;
        }
        public void ReadEstateId()
        {
            //Read Id of the Estate
            System.out.println(intEstateId);
        }
        public void AddMieszkanie(int intMieszkanieId)
        {
            //Method to validate if Mieszkanie was already added to Block, if not add it
            if(!this.Mieszkania.contains(intMieszkanieId))
            {
                this.Mieszkania.add(intMieszkanieId);
            } else {
                System.out.println("Mieszkanie of Id: " + intMieszkanieId + " is already assigned to Block.");
            }
        }
        public void RemoveMieszkanie(int intMieszkanieId)
        {
            //Check if block of specific Id is already in the estate, if so delete
            if(this.Mieszkania.contains(intMieszkanieId))
            {
                this.Mieszkania.remove(intMieszkanieId);
            } else {
                System.out.println("Block of Id: " + intMieszkanieId + " does not exist in this Estate.");
            }
        }
        public void ReadMieszkanie(HashSet<Integer> InputMieszkania)
        {
            for(int i:this.Mieszkania)
            {
                if(!InputMieszkania.contains(i))
                {
                    InputMieszkania.add(i);
                }
            }
        }
        public String toString()
        {
            return "Block of Id: " + this.intId + ", which is locatd in Estate of id: " + this.intEstateId;
        }
    }
    public static class Pomieszczenie
    {
        private double dblSpace;                        //Space of Pomieszczenie or Parking Spot
        private double dblUsedSpace=0;                                          //how much space was used so far by items
        private HashSet<Integer> StoredItems = new HashSet<Integer>();          //Store id of stored item
        private int intId;                                                      //Id of Mieszkanie or Parking Spot
        private String strRented="";                                            //Stores Pesel of person who is renting place -> if "" then no one is renting
        Pomieszczenie(double dblVolume, int intId)
        {
            //Constructor
            this.dblSpace=dblVolume;
            this.intId=intId;
        }
        Pomieszczenie(double dblWidth, double dblLength, double dblHeight, int intId)
        {
            //Constructor
            this.dblSpace=dblWidth*dblLength*dblHeight;
            this.intId=intId;
        }
        public void SetId(int intBlockId)
        {
            //Set Id of Mieszkanie
            this.intId=intBlockId;
        }
        public int ReadId()
        {
            //return id of the pomieszczenie
            return this.intId;
        }
        public void SetSpace(int intVolume)
        {
            //Set space, provided Volume
            this.dblSpace=intVolume;
        }
        public void SetSpace(int intLength,int intWidth, int intHeight)
        {
            //Set space, provided Width,Length,Height
            this.dblSpace=intLength*intWidth*intHeight;
        }
        public double ReadSpace()
        {
            //return space value
            return this.dblSpace;
        }
        public void SetUsedSpace(double dblVolume)
        {
            //Set used space, provided Volume
            this.dblUsedSpace=dblVolume;
        }
        public double ReadUsedSpace()
        {
            //return used space value
            return this.dblUsedSpace;
        }
        public void SetRenter(String strPesel)
        {
            //set renter of local
            this.strRented=strPesel;
        }
        public String ReadRenter()
        {
            //Returns Pesel of person who is renting the Local
            return this.strRented;
        }
        public boolean PutItem(int intItemId, double dblReqSpace)
        {
            //method puts item into Pomieszczenie: true - added, false not added
            boolean blnReturn=false;
            if(!StoredItems.contains(intItemId))                        //Check if item is in the location already
            {
                if(this.dblSpace-this.dblUsedSpace-dblReqSpace>0)       //enough required space
                {
                    this.StoredItems.add(intItemId);                        //add item to map
                    this.dblUsedSpace=this.dblUsedSpace+dblReqSpace;        //increase used space
                    blnReturn=true;
                    System.out.println("Item has been stored");
                } else {
                    System.out.println("There is not enough space in Pomieszczenie.");
                }
            } else {
                System.out.println("Item is already in the location.\n");
            }
            return blnReturn;
        }
        public void RemoveItem(int intItemId, double dblItemSpace)
        {
            if(this.StoredItems.contains(intItemId))
            {
                this.dblUsedSpace=dblUsedSpace-dblItemSpace;
                this.StoredItems.remove(intItemId);
            }
        }
        public void ReadItems(HashSet<Integer> InputHashSet)
        {
            for(int i:this.StoredItems)
            {
                InputHashSet.add(i);
            }
        }
        public boolean ContainsItem(int intItemId)
        {
            boolean blnResult=false;
            if(this.StoredItems.contains(intItemId))
            {
                blnResult=true;
            }
            return blnResult;
        }
    }
    public static class Mieszkanie extends Pomieszczenie
    {
        private int BlockId;                                            //Block in which Mieszkanie is located, count starts with 0
        private HashSet<String> Tenants = new HashSet<String>();             //Hashset which stores Pesel of persons living in Mieszkanie, Pesel is unique for each person=
        private Date dtmStartDate;                                      //Date when rent starts
        private Date dtmEndDate;                                        //Date when rent ends

        SimpleDateFormat DateFormat = new SimpleDateFormat ("dd-MM-yyyy hh:mm:ss");

        Mieszkanie(double dblVolume,HashMap<Integer, ClassObject.Mieszkanie> MieszkanieData) //Constructor
        {
            super(dblVolume,ProvideFreeMieszkanieId(MieszkanieData));
            MieszkanieData.put(super.intId,this);                         //Put into HashMap under new Id, this Estate
        }
        Mieszkanie(double dblWidth, double dblLength, double dblHeight,HashMap<Integer, ClassObject.Mieszkanie> MieszkanieData) //Constructor
        {
            super(dblWidth,dblLength,dblHeight,ProvideFreeMieszkanieId(MieszkanieData));
            MieszkanieData.put(super.intId,this);                         //Put into HashMap under new Id, this Estate
        }
        public void SetBlockId(int intBlockId)
        {
            //Assigns Id of block in which Mieszkanie is located
            BlockId=intBlockId;
        }
        public void ReadBlockId()
        {
            //Prints BlockId
            System.out.println(BlockId);
        }
        public void AddTenant(String strPesel)
        {
            //Method to check if Person was already added to Mieszkanie, if not add it
            if(!this.Tenants.contains(strPesel))
            {
                this.Tenants.add(strPesel);
                if(Tenants.size()==0)           //Check if this is first renter -> if so he pays the bills
                {
                    super.strRented=strPesel;
                }
            } else {
                System.out.println("Tenant of Id: " + strPesel + " is already assigned to Mieszkanie.");
            }
        }
        public void RemoveTenant(String strPesel)
        {
            //Check if block of specific Id is already in the estate, if so delete
            if(this.Tenants.contains(strPesel))
            {
                this.Tenants.remove(strPesel);
            } else {
                System.out.println("Person of pesel: " + strPesel + " does not exist in this Mieszkanie.");
            }
        }
        public void ReadTenant(HashSet<String> InputTenants)
        {
            for(String strKey:Tenants)
            {
                if(!InputTenants.contains(strKey))
                {
                    InputTenants.add(strKey);
                }
            }
        }
        public int GetNumberOfTenants()
        {
            //Get number of tenants
            return Tenants.size();
        }
        public void SetStartDate(String strStartDate)
        {
            try
            {
                this.dtmStartDate = DateFormat.parse(strStartDate);                //parse text to a date variable
            } catch (ParseException e) {
                System.out.println("Unparseable using " + DateFormat);
            }
        }
        public void ReadStartDate()
        {
            //Print Start date in console
            System.out.println("Current Date: " + DateFormat.format(dtmStartDate));
        }
        public void RemoveStartDate()
        {
            //Clear variable with Start Date
            dtmStartDate=null;
        }
        public void SetEndDate(String strEndDate)
        {
            try
            {
                this.dtmStartDate = DateFormat.parse(strEndDate);                //parse text to a date variable
            } catch (ParseException e) {
                System.out.println("Unparseable using " + DateFormat);
            }
        }
        public void ReadEndDate()
        {
            //Print End date in console
            System.out.println("Current Date: " + DateFormat.format(dtmEndDate));
        }
        public void RemoveEndDate()
        {
            //Clear variable with Start Date
            dtmEndDate=null;
        }
        public void Eviction()
        {
            //Process of Eviction of all persons in Mieszkanie
            for (String Key : Tenants)
            {
                //
                System.out.println(Key);
            }
        }
        public String toString()
        {
            return "Mieszkanie of Id: " + super.intId + " Belongs to block: " + this.BlockId;
        }
    }
    public static class MiejsceParkingowe extends Pomieszczenie
    {
        MiejsceParkingowe(double dblVolume,HashMap<Integer, ClassObject.MiejsceParkingowe> MiejsceParkingoweMap)         //Constructor
        {
            super(dblVolume,ProvideFreeParkingId(MiejsceParkingoweMap));
            MiejsceParkingoweMap.put(super.intId,this);
        }
        MiejsceParkingowe(double dblWidth, double dblLength, double dblHeight,HashMap<Integer, ClassObject.MiejsceParkingowe> MiejsceParkingoweMap)         //Constructor
        {
            super(dblWidth, dblLength, dblHeight,ProvideFreeParkingId(MiejsceParkingoweMap));
            MiejsceParkingoweMap.put(super.intId,this);
        }
    }
    public static class Person
    {
        private String strName;
        private String strSurname;
        private String strPesel;
        private String strAddress;
        private Date dtmDateOfBirth;
        //Column 0 - Id of Rented Local, Column 1 - If owner of it or not (0 no, 1 yes, 2 'empty'), Column 2 type of local (0-Mieszkanie, 1-Parking, 2-'empty')
        private int[][] arrRentStatus = new int[5][3];
        private HashMap<String,File> ReceivedFiles = new HashMap<String,File>();    //HashSet of files, which Tenant received

        SimpleDateFormat DateFormat = new SimpleDateFormat ("dd-MM-yyyy");
        Person(String strName, String strSurname,String strPesel,String strAddress,String strDateOfBirth,HashMap<String, ClassObject.Person> PersonsData)    //Constructor
        {
            for(int i=0;i<arrRentStatus.length;i++)                                     //set "empty" array
            {
                arrRentStatus[i][1]=2;
                arrRentStatus[i][2]=2;
            }
            this.strName=strName;
            this.strSurname=strSurname;
            this.strPesel=strPesel;
            this.strAddress=strAddress;
            try
            {
                this.dtmDateOfBirth = DateFormat.parse(strDateOfBirth);                //parse text to a date variable
            } catch (ParseException e) {
                System.out.println("Unparseable using " + DateFormat);
            }
            PersonsData.put(strPesel,this);
        }
        public void SetName(String strName)
        {
            //Set person's name
            this.strName=strName;
        }
        public String ReadName()
        {
            //return name of person
            return this.strName;
        }
        public void SetSurname(String strSurname)
        {
            //Set person's surname
            this.strSurname=strSurname;
        }
        public String ReadSurname()
        {
            //return surname of person
            return this.strSurname;
        }
        public void SetPesel(String strPesel)
        {
            //Set person's Pesel
            this.strPesel=strPesel;
        }
        public String ReadPesel()
        {
            //return Pesel of person
            return this.strPesel;
        }
        public void SetAddress(String strAddress)
        {
            //Set person's Address
            this.strAddress=strAddress;
        }
        public String ReadAddress()
        {
            //return Address of person
            return this.strAddress;
        }
        public void SetDateOfBirth(String strDateOfBirth)
        {
            //Set person's Address
            try
            {
                this.dtmDateOfBirth = DateFormat.parse(strDateOfBirth);                //parse text to a date variable
            } catch (ParseException e) {
                System.out.println("Unparseable using " + DateFormat);
            }
        }
        public String ReadDateOfBirth()
        {
            //return Birth Date of person
            return this.dtmDateOfBirth.toString();
        }
        public void AddFile(String strFileName, File objFile)
        {
            //Add file to person's folder
            if(this.ReceivedFiles.containsKey(strFileName))
            {
                System.out.println("File under name: " + strFileName + " already exists, please provide different name.");
            } else {
                this.ReceivedFiles.put(strFileName,objFile);
                System.out.println("File: " + strFileName + " has been added to person's folder.");
            }
        }
        public void RemoveFile(String strFileName)
        {
            //Remove file of person's folder
            if(this.ReceivedFiles.containsKey(strFileName))
            {
                this.ReceivedFiles.remove(strFileName);
                System.out.println("File: " + strFileName + " has been deleted from person's folder.");
            } else {
                System.out.println("File: " + strFileName + " does not exists in person's folder.");
            }
        }
        public void ReadFiles()
        {
            //list files of person's folder

        }
        public void AddRent(int intId,int intOwener,int intRentType)  //intRentType -> 0-Mieszkanie, 1-ParkingSpot
        {
            //method to assign id of mieszkanie (true) or ParkingSpot (false) into first free spot of array
            int i;

            //Check if user can rent another local, if so rent
            for(i=0;i<arrRentStatus.length;i++)
            {
                if(arrRentStatus[i][2]==2)
                {
                    arrRentStatus[i][0]=intId;              //Rented local
                    arrRentStatus[i][1]=intOwener;          //Is he owner
                    arrRentStatus[i][2]=intRentType;        //Local type
                    if(intRentType==0)
                    {
                        System.out.println("User with pesel: " + this.strPesel + ", will rent Mieszkanie of Id: " + intId + ".\n");
                    }else{
                        System.out.println("User with pesel: " + this.strPesel + ", will rent Miejsce Parkingowe of Id: " + intId + ".\n");
                    }
                    break;
                }
            }
            if(i==arrRentStatus.length)
            {
                System.out.println("Selected person is already renting 5 locals - he/she can't rent more.");
            }
        }
        public void RemoveRent()
        {
            //Stop renting specific local by the user
        }
        public void ReadRent(int[][] arrCurrentRent)
        {
            //Present all locals rented by the user
            for(int i=0;i<this.arrRentStatus.length;i++)
            {
                arrCurrentRent[i][0]=this.arrRentStatus[i][0];
                arrCurrentRent[i][1]=this.arrRentStatus[i][1];
                arrCurrentRent[i][2]=this.arrRentStatus[i][2];
            }
        }
        public boolean IsPersonRenting(int intLocalId, int intLocalType)        //Local type -> 0-Mieszkanie, 1-Parking
        {
            boolean blnReturn=false;
            for(int i=0;i<arrRentStatus.length;i++) //loop through status array
            {
                if((arrRentStatus[i][0]==intLocalId) && (arrRentStatus[i][2]==intLocalType))    //Check if id of local and type of local are matching
                {
                    blnReturn=true;
                    break;
                }
            }
            return blnReturn;           //return true if local was found
        }
        public String ReadPersonsData()
        {
            String strReturn = "Data of the user are as follows: \n";
            strReturn=strReturn + "Name & surname: " + this.strName + " " + this.strSurname + ".\n";
            strReturn=strReturn + "Pesel: " + " " + this.strPesel + ".\n";
            strReturn=strReturn + "Address: " + this.strAddress + ".\n";
            strReturn=strReturn + "Date of birth: " + this.dtmDateOfBirth + ".\n";
            //Add mieszkanie to the listing
            String strMieszkanie="";
            for(int i=0;i<arrRentStatus.length;i++)
            {
                if(arrRentStatus[i][2]==0)               //check if Mieszkanie
                {
                    strMieszkanie=strMieszkanie + "- Mieszkanie of Id: " + arrRentStatus[i][0] + ".\n";
                }
            }
            if(strMieszkanie!="")
            {
                strMieszkanie= "List of rented locals: " + "\n" + strMieszkanie;
                strReturn = strReturn+strMieszkanie;
            } else {
                strMieszkanie= "User does not rent any locals.\n";
                strReturn = strReturn+strMieszkanie;
            }
            //Add Parking spots to listing
            String strParking="";
            for(int i=0;i<arrRentStatus.length;i++)
            {
                if(arrRentStatus[i][2]==1)               //check if parking
                {
                    strParking=strParking + "- Parking of Id: " + arrRentStatus[i][0] + ".\n";
                }
            }
            if(strParking!="")
            {
                strParking= "\"List of rented parking spots: " + ".\n" + strParking;
                strReturn = strReturn+strParking;
            } else {
                strParking= "User does not rent any parking spots.\n";
                strReturn = strReturn+strParking;
            }
            //Add received files to the listing
            if(ReceivedFiles.size()!=0)
            {
                strReturn=strReturn+"List of files: \n";
                for (String strKey : ReceivedFiles.keySet()) {
                    strReturn=strReturn + strKey + ".\n";
                }
            } else {
                strReturn=strReturn+"User does not have any files in the folder.\n";
            }
            return strReturn;
        }
        public boolean IsAllowedToRentParking()
        {
            int intMieszkOwnCount=0;
            int intParkOwnCount=0;
            boolean blnResult=false;
            for(int i=0;i<arrRentStatus.length;i++)
            {
                if(arrRentStatus[i][2]==0)                          //check if mieszkanie
                {
                    if(arrRentStatus[i][1]==1)                      //check if owner
                    {
                        intMieszkOwnCount=intMieszkOwnCount+1;      //count for how many Mieszkanie he is owner
                    }
                }
                if(arrRentStatus[i][2]==1)                          //check if parking
                {
                    intParkOwnCount=intParkOwnCount+1;              //count how many parkings he has
                }
            }
            if(intMieszkOwnCount>intParkOwnCount)                   //1 parking spot per Mieszkanie
            {
                blnResult=true;
            }
            return blnResult;
        }
        public String toString()
        {
           return "Pesel: " + this.strPesel + ", Name: " + this.strName + ", Surname: " + this.strSurname + ", Address: " + this.strAddress + ", Date of birth: " + this.dtmDateOfBirth;
        }
    }
    public static class Item
    {
        private int intId;
        private String strName;
        private double dblSpace;
        private boolean blnAllocated=false;         //from default it is not allocated
        Item(String strName, double dblVolume,HashMap<Integer, Object> ItemsData)                                       //Constructor
        {
            this.intId=ReturnFreeIdItem(ItemsData);
            this.strName=strName;
            this.dblSpace=dblVolume;
            ItemsData.put(this.intId,this);
        }
        Item(String strName, double dblLength,double dblWidth,double dblHeight,HashMap<Integer, Object> ItemsData)      //Constructor
        {
            this.intId=ReturnFreeIdItem(ItemsData);
            this.strName=strName;
            this.dblSpace=dblLength*dblWidth*dblHeight;
            ItemsData.put(this.intId,this);
        }
        private int ReturnFreeIdItem(HashMap<Integer, Object> ItemsData)
        {
            //Method returns free id from HashMap Items
            int intFreeId=0;
            for(int i=0;i < ItemsData.size()+1;i++)     //loop from 0 to size of HashMap +1 and find available Id
            {
                if(!ItemsData.containsKey(i))           //If id is not contained in HashMap -> assign it and return in method
                {
                    intFreeId=i;                            //id found, leave loop
                    break;
                }
            }
            return intFreeId;
        }
        public void SetName(String strName)
        {
            //Set name of the item
            this.strName=strName;
        }
        public String ReadName()
        {
            //Return name of the item
            return this.strName;
        }
        public void SetSpace(double dblVolume)
        {
            //Set Space of the item
            this.dblSpace=dblVolume;
        }
        public void SetSpace(double dblLength,double dblWidth,double dblHeight)
        {
            //Set Space of the item
            this.dblSpace=dblLength*dblWidth*dblHeight;
        }
        public double ReadSpace()
        {
            //Return Space of the item
            return this.dblSpace;
        }
        public void SetId(int intId)
        {
            //assign id
            this.intId=intId;
        }
        public int ReadId()
        {
            //return id of item
            return this.intId;
        }
        public void SetItemAllocateStatus(boolean blnStatus)
        {
            //set status
            this.blnAllocated=blnStatus;
        }
        public boolean IsItemAllocated()
        {
            //returns status of the item
            return this.blnAllocated;
        }
        public String toString()
        {
            String strReturn="Id: " + this.intId + ", Name: " + this.strName + ", Is allocated: " + this.blnAllocated;
            return strReturn;
        }
    }
    public static class SamochodTerenowy extends Item
    {
        //Dane techniczne Jeep Wrangler III Terenowy
        private int intDoorCount=2;
        private int intSeatCount=4;
        private int intPower=199;           //Moc silnika
        private int intTorque=315;          //Moment obrotowy
        private int intEngDispl=3778;       //Pojemność skokowa
        private int intCylinderCount=6;
        private int intProdYear=1997;

        SamochodTerenowy(String strName, double dblVolume,HashMap<Integer, Object> ItemsData)                                      //Constructor
        {
            super(strName, dblVolume,ItemsData);
        }
        SamochodTerenowy(String strName, double dblLength,double dblWidth,double dblHeight,HashMap<Integer, Object> ItemsData)      //Constructor
        {
            super(strName,dblLength,dblWidth,dblHeight,ItemsData);
        }
        private void SetDoorsCount(int intDoorCount)
        {
            //Set door count
            this.intDoorCount=intDoorCount;
        }
        private int ReadDoorsCount()
        {
            //Read door count
            return this.intDoorCount;
        }
        private void SetSeatCount(int intSeatCount)
        {
            //Set seat count
            this.intSeatCount=intSeatCount;
        }
        private int ReadSeatCount()
        {
            //Read seat count
            return this.intSeatCount;
        }
        private void SetPower(int intPower)
        {
            //Set Power
            this.intPower=intPower;
        }
        private int ReadPower()
        {
            //Read seat count
            return this.intPower;
        }
        private void SetTorque(int intTorque)
        {
            //Set Torque
            this.intTorque=intTorque;
        }
        private int ReadTorque()
        {
            //Read Torque
            return this.intTorque;
        }
        private void SetEngDispl(int intEngDispl)
        {
            //Set EngDispl
            this.intEngDispl=intEngDispl;
        }
        private int ReadEngDispl()
        {
            //Read EngDispl
            return this.intEngDispl;
        }
        private void SetCylinderCount(int intCylinderCount)
        {
            //Set CylinderCount
            this.intCylinderCount=intCylinderCount;
        }
        private int ReadCylinderCount()
        {
            //Read CylinderCount
            return this.intCylinderCount;
        }
        private void SetProdYear(int intProdYear)
        {
            //Set ProdYear
            this.intProdYear=intProdYear;
        }
        private int ReadProdYear()
        {
            //Read ProdYear
            return this.intProdYear;
        }
        public String toString()
        {
            return "Door count: " + this.intDoorCount + ", Seat count: " + this.intSeatCount + ", Power: " + this.intPower + ", Torque: " + this.intTorque + ", Engine displacement: " + this.intEngDispl + ", Cylinder count: " + this.intCylinderCount + " , Year of production: " + intProdYear;
        }
    }
    public static class SamochódMiejski extends Item
    {
        //Dane techniczne Lexus UX Crossover
        private int intDoorCount=5;
        private int intSeatCount=5;
        private int intPower=184;           //Moc silnika
        private int intTorque=202;             //Moment obrotowy
        private int intEngDispl=1987;       //Pojemność skokowa
        private int intCylinderCount=4;
        private double dblKmTraveled=310;

        SamochódMiejski(String strName, double dblVolume,HashMap<Integer, Object> ItemsData)                                      //Constructor
        {
            super(strName, dblVolume,ItemsData);
        }
        SamochódMiejski(String strName, double dblLength,double dblWidth,double dblHeight,HashMap<Integer, Object> ItemsData)      //Constructor
        {
            super(strName,dblLength,dblWidth,dblHeight,ItemsData);
        }
        private void SetDoorsCount(int intDoorCount)
        {
            //Set door count
            this.intDoorCount=intDoorCount;
        }
        private int ReadDoorsCount()
        {
            //Read door count
            return this.intDoorCount;
        }
        private void SetSeatCount(int intSeatCount)
        {
            //Set seat count
            this.intSeatCount=intSeatCount;
        }
        private int ReadSeatCount()
        {
            //Read seat count
            return this.intSeatCount;
        }
        private void SetPower(int intPower)
        {
            //Set Power
            this.intPower=intPower;
        }
        private int ReadPower()
        {
            //Read seat count
            return this.intPower;
        }
        private void SetTorque(int intTorque)
        {
            //Set Torque
            this.intTorque=intTorque;
        }
        private int ReadTorque()
        {
            //Read Torque
            return this.intTorque;
        }
        private void SetEngDispl(int intEngDispl)
        {
            //Set EngDispl
            this.intEngDispl=intEngDispl;
        }
        private int ReadEngDispl()
        {
            //Read EngDispl
            return this.intEngDispl;
        }
        private void SetCylinderCount(int intCylinderCount)
        {
            //Set CylinderCount
            this.intCylinderCount=intCylinderCount;
        }
        private int ReadCylinderCount()
        {
            //Read CylinderCount
            return this.intCylinderCount;
        }
        private void SetProdYear(double dblKmTraveled)
        {
            //Set ProdYear
            this.dblKmTraveled=dblKmTraveled;
        }
        private double ReadProdYear()
        {
            //Read KmTraveled
            return this.dblKmTraveled;
        }
        public String toString()
        {
            return "Door count: " + this.intDoorCount + ", Seat count: " + this.intSeatCount + ", Power: " + this.intPower + ", Torque: " + this.intTorque + ", Engine displacement: " + this.intEngDispl + ", Cylinder count: " + this.intCylinderCount + " , Km traveled: " + dblKmTraveled;
        }
    }
    public static class Lodz extends Item
    {
        //Hanse 348
        private double dblDipping=1.95;
        private double dblSailsArea=58.5;
        private int intPower=21;
        private int intNumOfCabs=3;
        private int intNumOfBerths=8;

        Lodz(String strName, double dblVolume,HashMap<Integer, Object> ItemsData)                                      //Constructor
        {
            super(strName, dblVolume,ItemsData);
        }
        Lodz(String strName, double dblLength,double dblWidth,double dblHeight,HashMap<Integer, Object> ItemsData)      //Constructor
        {
            super(strName,dblLength,dblWidth,dblHeight,ItemsData);
        }
        public void SetDipping(double dblDipping)
        {
            //Set dipping of boat
            this.dblDipping=dblDipping;
        }
        public double ReadDipping()
        {
            //Read dipping of boat
            return this.dblDipping;
        }
        public void SetSailsArea(double dblSailsArea)
        {
            //Set Sails area of boat
            this.dblSailsArea=dblSailsArea;
        }
        public double ReadSailsArea()
        {
            //Read sails area of boat
            return this.dblSailsArea;
        }
        public void SetPower(int intPower)
        {
            //Set Power of boat
            this.intPower=intPower;
        }
        public double ReadPower()
        {
            //Read sails area of boat
            return this.intPower;
        }
        public void SetNumOfCabs(int intNumOfCabs)
        {
            //Set Number of Cabs of boat
            this.intNumOfCabs=intNumOfCabs;
        }
        public int ReadNumOfCabs()
        {
            //Read Number of cabs of boat
            return this.intNumOfCabs;
        }
        public void SetNumOfBerths(int intNumOfBerths)
        {
            //Set Number of Berths of boat
            this.intNumOfBerths=intNumOfBerths;
        }
        public int ReadNumOfBerths()
        {
            //Read Number of Berths of boat
            return this.intNumOfBerths;
        }
        public String toString()
        {
            return "Dipping: " + this.dblDipping + ", Sails area: " + this.dblSailsArea + ", Power: " + this.intPower + ", Number of Cabs: " + this.intNumOfCabs + ", Number of Berths: " + this.intNumOfBerths;
        }
    }
    public static class Motocykl extends Item
    {
        //Kawasaki Z H2
        private int intSeatCount=2;
        private int intPower=200;           //Moc silnika
        private int intTorque=133;          //Moment obrotowy
        private int intEngDispl=998;        //Pojemność skokowa
        private int intTankCapacity=17;     //Pojemność zbiornika
        Motocykl(String strName, double dblVolume,HashMap<Integer, Object> ItemsData)                                      //Constructor
        {
            super(strName, dblVolume,ItemsData);
        }
        Motocykl(String strName, double dblLength,double dblWidth,double dblHeight,HashMap<Integer, Object> ItemsData)      //Constructor
        {
            super(strName,dblLength,dblWidth,dblHeight,ItemsData);
        }
        public void SetSeatCount(int intSeatCount)
        {
            //Set seat count
            this.intSeatCount=intSeatCount;
        }
        public int ReadSeatCount()
        {
            //read seat count
            return this.intSeatCount;
        }
        public void SetPower(int intPower)
        {
            //Set power
            this.intPower=intPower;
        }
        public int ReadPower()
        {
            //read power
            return this.intPower;
        }
        public void SetTorque(int intTorque)
        {
            //Set Torque
            this.intTorque=intTorque;
        }
        public int ReadTorque()
        {
            //read Torque
            return this.intTorque;
        }
        public void SetEngDispl(int intEngDispl)
        {
            //Set EngDispl
            this.intEngDispl=intEngDispl;
        }
        public int ReadEngDispl()
        {
            //read EngDispl
            return this.intEngDispl;
        }
        public void SetTankCapacity(int intTankCapacity)
        {
            //Set TankCapacity
            this.intTankCapacity=intTankCapacity;
        }
        public int ReadTankCapacity()
        {
            //read TankCapacity
            return this.intTankCapacity;
        }
        public String toString()
        {
            return "Seast count: " + this.intSeatCount + ", Power: " + this.intPower + ", intTorque: " + intTorque + ", Engine Displacement: " + intEngDispl + ", Tank capacity: " + this.intTankCapacity;
        }
    }
    public static class Amfibia extends Item
    {
        //Amphicar Modell 770
        private int intDoorCount=2;
        private int intSeatCount=4;
        private int intPower=38;            //Moc silnika
        private int intTorque=70;           //Moment obrotowy
        private int intEngDispl=1400;       //Pojemność skokowa
        private int intCylinderCount=6;
        private boolean blnWaterProof=true;

        Amfibia(String strName, double dblVolume,HashMap<Integer, Object> ItemsData)                                      //Constructor
        {
            super(strName, dblVolume,ItemsData);
        }
        Amfibia(String strName, double dblLength,double dblWidth,double dblHeight,HashMap<Integer, Object> ItemsData)      //Constructor
        {
            super(strName,dblLength,dblWidth,dblHeight,ItemsData);
        }
        private void SetDoorsCount(int intDoorCount)
        {
            //Set door count
            this.intDoorCount=intDoorCount;
        }
        private int ReadDoorsCount()
        {
            //Read door count
            return this.intDoorCount;
        }
        private void SetSeatCount(int intSeatCount)
        {
            //Set seat count
            this.intSeatCount=intSeatCount;
        }
        private int ReadSeatCount()
        {
            //Read seat count
            return this.intSeatCount;
        }
        private void SetPower(int intPower)
        {
            //Set Power
            this.intPower=intPower;
        }
        private int ReadPower()
        {
            //Read seat count
            return this.intPower;
        }
        private void SetTorque(int intTorque)
        {
            //Set Torque
            this.intTorque=intTorque;
        }
        private int ReadTorque()
        {
            //Read Torque
            return this.intTorque;
        }
        private void SetEngDispl(int intEngDispl)
        {
            //Set EngDispl
            this.intEngDispl=intEngDispl;
        }
        private int ReadEngDispl()
        {
            //Read EngDispl
            return this.intEngDispl;
        }
        private void SetCylinderCount(int intCylinderCount)
        {
            //Set CylinderCount
            this.intCylinderCount=intCylinderCount;
        }
        private int ReadCylinderCount()
        {
            //Read CylinderCount
            return this.intCylinderCount;
        }
        private void SetProdYear(boolean blnWaterProof)
        {
            //Set WaterProof
            this.blnWaterProof=blnWaterProof;
        }
        private boolean ReadProdYear()
        {
            //Read WaterProof
            return this.blnWaterProof;
        }
        public String toString()
        {
            return "Door count: " + this.intDoorCount + ", Seast count: " + this.intSeatCount + ", Power: " + this.intPower + ", intTorque: " + intTorque + ", Engine Displacement: " + intEngDispl + ", Cylinder countL " + this.intCylinderCount + ", Waterproff: " + this.blnWaterProof;
        }
    }
    private static int ProvideFreeEstateId(HashMap<Integer, ClassObject.Estate> EstateMap)
    {
        //Method to find first available Id in given HashMap for Estates
        int intFreeId=0;
        for(int i=0;i < EstateMap.size()+1;i++)     //loop from 0 to size of HashMap +1 and find available Id
        {
            if(!EstateMap.containsKey(i))           //If id is not contained in HashMap -> assign it and return in method
            {
                intFreeId=i;                        //id found, leave loop
                break;
            }
        }
        return intFreeId;
    }
    private static int ProvideFreeBlockId(HashMap<Integer, ClassObject.Block> BlockMap)
    {
        //Method to find first available Id in given HashMap for Estates
        int intFreeId=0;
        for(int i=0;i < BlockMap.size()+1;i++)     //loop from 0 to size of HashMap +1 and find available Id
        {
            if(!BlockMap.containsKey(i))           //If id is not contained in HashMap -> assign it and return in method
            {
                intFreeId=i;                        //id found, leave loop
                break;
            }
        }
        return intFreeId;
    }
    private static int ProvideFreeMieszkanieId(HashMap<Integer, ClassObject.Mieszkanie> MieszkaniaMap)
    {
        //Method to find first available Id in given HashMap for Estates
        int intFreeId=0;
        for(int i=0;i < MieszkaniaMap.size()+1;i++)     //loop from 0 to size of HashMap +1 and find available Id
        {
            if(!MieszkaniaMap.containsKey(i))           //If id is not contained in HashMap -> assign it and return in method
            {
                intFreeId=i;                            //id found, leave loop
                break;
            }
        }
        return intFreeId;
    }
    private static int ProvideFreeParkingId(HashMap<Integer, ClassObject.MiejsceParkingowe> MiejsceParkingoweMap)
    {
        //Method to find first available Id in given HashMap for Estates
        int intFreeId=0;
        for(int i=0;i < MiejsceParkingoweMap.size()+1;i++)     //loop from 0 to size of HashMap +1 and find available Id
        {
            if(!MiejsceParkingoweMap.containsKey(i))           //If id is not contained in HashMap -> assign it and return in method
            {
                intFreeId=i;                            //id found, leave loop
                break;
            }
        }
        return intFreeId;
    }
}