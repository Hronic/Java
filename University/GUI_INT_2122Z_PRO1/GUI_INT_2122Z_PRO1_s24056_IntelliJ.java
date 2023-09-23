import java.util.Scanner;   //import the Scanner class
import java.util.HashMap;   //import the HashMap class
import java.util.Random;    //import random class

public class GUI_INT_2122Z_PRO1_s24056_IntelliJ
{
    public static void main(String[] args)
    {
        //Variables to store global/program data
        boolean blnEnd=false;
        String strUserPesel="";                                                                //Variable will store Pesel of person on which admin has logged
        HashMap<Integer, ClassObject.Estate> EstateData = new HashMap<Integer, ClassObject.Estate>();                    //Key: Estate Id; Value: Estate Object
        HashMap<Integer, ClassObject.Block> BlockData = new HashMap<Integer, ClassObject.Block>();                      //Key: Block Id; Value: Block Object
        HashMap<Integer, ClassObject.Mieszkanie> MieszkanieData = new HashMap<Integer, ClassObject.Mieszkanie>();            //Key: Mieszkanie Id; Value: Mieszkanie Object
        HashMap<Integer, ClassObject.MiejsceParkingowe> ParkingData = new HashMap<Integer, ClassObject.MiejsceParkingowe>();        //Key: Parking Id; Value: MiejsceParkingowe Object
        HashMap<String, ClassObject.Person> PersonsData = new HashMap<String, ClassObject.Person>();                    //Key: Person Pesel; Value: Person Object
        HashMap<Integer, Object> ItemsData = new HashMap<Integer, Object>();                                 //Key: Person Id; Value: Item or Vehicle

        System.out.println("Welcome to the estate management application.");
        vLoadTestSubject(EstateData,BlockData,MieszkanieData,ParkingData,PersonsData,ItemsData);                                         //Create sample data
        while (blnEnd==false)
        {
            vPresentAvailableOperations();
            Scanner objInputScanner = new Scanner(System.in);       //create a scanner object
            String strSelectedOper  = objInputScanner.nextLine();   //read choosen by user command
            if (blnDoesOperationExists(strSelectedOper))            //validate if operation exists
            {
                switch(strSelectedOper)
                {
                    case "1":
                        strUserPesel=UsersOperations.strSelectUser_1(PersonsData);
                        break;
                    case "2":
                        UsersOperations.vPrintUsersData_2(strUserPesel,PersonsData);
                        break;
                    case "3":
                        UsersOperations.vPrintAvailableLocals_3(MieszkanieData,ParkingData);
                        break;
                    case "4":
                        UsersOperations.vRentNewMieszkanie_4(strUserPesel,MieszkanieData,ParkingData,PersonsData);
                        break;
                    case "5":
                        UsersOperations.vPrintItems_5(strUserPesel,PersonsData,MieszkanieData,ParkingData,ItemsData);
                        break;
                    case "6":
                        UsersOperations.vAllocateItemVehicles_6(strUserPesel,PersonsData,MieszkanieData,ParkingData,ItemsData);
                        break;
                    case "7":
                        UsersOperations.vPutOutItem_7(strUserPesel,PersonsData,MieszkanieData,ParkingData,ItemsData);
                        break;
                    case "8":
                        UsersOperations.vSaveEstateToFile_8(EstateData,BlockData,MieszkanieData,ParkingData,PersonsData,ItemsData);
                        break;
                    case "9":
                        blnEnd=UsersOperations.blnEndProgram_9(strSelectedOper);   //User chose to close application
                        break;
                }
            } else {
                System.out.println("Operation: " + strSelectedOper + " does not exist, please choose another one.");
                System.out.println();
            }
        }
    }
    public static void vLoadTestSubject(HashMap<Integer, ClassObject.Estate> EstateData,HashMap<Integer, ClassObject.Block> BlockData, HashMap<Integer, ClassObject.Mieszkanie> MieszkanieData, HashMap<Integer, ClassObject.MiejsceParkingowe> ParkingData,HashMap<String, ClassObject.Person> PersonsData,HashMap<Integer, Object> ItemsData)
    {
        //Method to load sample values

        //Get counts of Estate, Blocks, Mieszkanie, Persons, Items
        int intMaxRandRange=100;        //Max range of randomization
        int intMaxSize=50;             //Max size of Mieszkanie/Miejsce parkingowe

        int intNumberOfEstates = 1;     //In description of the project, in main method, exactly 1 estate should be created
        Random rand = new Random();     //Set randomization
        int intRndBlocks = rand.nextInt(intMaxRandRange+intNumberOfEstates)+intNumberOfEstates;  //Number of Blocks, min intNumberOfEstates, max intNumberOfEstates+intMaxRandRange
        int intRndMieszkanie=0;       //number of Mieszkanie
        if(intRndBlocks>=10)
        {
            intRndMieszkanie = rand.nextInt(intMaxRandRange+intRndBlocks)+intRndBlocks; //Number of Mieszkanie, min intRndBlocks, max intRndBlocks+intMaxRandRange
        } else {
            intRndMieszkanie = rand.nextInt(intMaxRandRange+10)+10; //Number of Mieszkanie, min 10, max 10+intMaxRandRange
        }
        int intRndMiejscPark = rand.nextInt(intMaxRandRange+intRndMieszkanie)+intRndMieszkanie; //Number of MiejscaParkingowe, min intRndMieszkanie, max intRndMieszkanie+intMaxRandRange
        int intNumOfPersons=rand.nextInt(intMaxRandRange)+5;                                          //Number of persons, min 5 max intMaxRandRange
        int intNumOfItems=rand.nextInt(intMaxRandRange+intNumOfPersons)+intNumOfPersons;        //Number of items, min 5 max intMaxRandRange+intNumOfPersons, not specified in desc.

        //Create objects
        ClassObject clsMyObjects = new ClassObject();                           //Set class object so i can import from other classes
        ClassObject.Estate objEstate = new ClassObject.Estate(EstateData);      //Create 1 Easte
        for(int i=0;i<intRndBlocks;i++)                                         //Create Blocks
        {
            ClassObject.Block objBlok = new ClassObject.Block(BlockData);
        }
        for(int i=0;i<intRndMieszkanie;i++)                                     //Create Mieszkanie
        {
            int intPlaceSize=rand.nextInt(intMaxSize)+30;                       //Randomize size of Mieszkanie from 30 to intMaxSize
            ClassObject.Mieszkanie objBlok = new ClassObject.Mieszkanie(intPlaceSize,MieszkanieData);
        }
        for(int i=0;i<intRndMiejscPark;i++)                                     //Create MiejsceParkingowe
        {
            int intPlaceSize=rand.nextInt(intMaxSize)+30;                       //Randomize size of Mieszkanie from 30 to intMaxSize
            ClassObject.MiejsceParkingowe objMiejsceParkingowe = new ClassObject.MiejsceParkingowe(intPlaceSize,ParkingData);
        }
        for(int i=0;i<intNumOfPersons;i++)                                      //Create Persons
        {
            String strName="Jan" + i;
            String strSurname="Kowalski" + i;
            String strPesel=String.valueOf(i);
            String strAddress="Koszykowa " + i + ", 02-008 Warszawa";
            String strDateOfBirth="01-01-1990";
            ClassObject.Person objPerson = new ClassObject.Person(strName,strSurname,strPesel,strAddress,strDateOfBirth,PersonsData);
        }
        for(int i=0;i<intNumOfItems;i++)                                        //Create items
        {
            int intRandItem = rand.nextInt(6);            //There are 6 items types (1+5 Vehicles) -> randomize creation
            int intRandSpace = rand.nextInt(10)+1;        //Randomize space of item from 1 to 10
            switch(intRandItem)
            {
                case 0:             //Item
                    ClassObject.Item objItem = new ClassObject.Item("Przedmiot " + i,intRandSpace,ItemsData);
                    break;
                case 1:             //SamochodTerenowy
                    ClassObject.SamochodTerenowy objST = new ClassObject.SamochodTerenowy("Samochod Terenowy " + i,intRandSpace,ItemsData);
                    break;
                case 2:             //SamochódMiejski
                    ClassObject.SamochódMiejski objSM = new ClassObject.SamochódMiejski("Samochód Miejski " + i,intRandSpace,ItemsData);
                    break;
                case 3:             //Lodz
                    ClassObject.Lodz objLodz = new ClassObject.Lodz("Lodz " + i,intRandSpace,ItemsData);
                    break;
                case 4:             //Motocykl
                    ClassObject.Motocykl objMotocykl = new ClassObject.Motocykl("Motocykl " + i,intRandSpace,ItemsData);
                    break;
                case 5:             //Amfibia
                    ClassObject.Amfibia obAmfibia = new ClassObject.Amfibia("Amfibia " + i,intRandSpace,ItemsData);
                    break;
            }
        }

        //Assign objects
        for (int i: BlockData.keySet())             //Assign all Blocks to Estate (id of estate is 0 -> only 1 estate)
        {
            BlockData.get(i).SetEstateId(0);
            EstateData.get(0).AddBlock(i);
        }
        int intInsertId=0;
        for (int i: MieszkanieData.keySet())        //Assign Mieszkanie to Blocks
        {
            if (intInsertId>=BlockData.size())
            {
                intInsertId=0;
            }
            MieszkanieData.get(i).SetBlockId(intInsertId);  //To Mieszkanie of Id i assign Block with Id intInsertId
            BlockData.get(intInsertId).AddMieszkanie(i);    //Put Mieszkanie of Id i to Block's HashSet of Mieszkanie (Block's Id intInsertId)
            intInsertId=intInsertId+1;                      //Increase Block Index
        }
        intInsertId=0;
        for (String i: PersonsData.keySet())                            //Assign Persons to Mieszkanie
        {                                                               //loop through persons data
            if (intInsertId>=MieszkanieData.size())                     //if count of people > count of Mieszkanie -> start from 0
            {
                intInsertId=0;
            }
            int intOwnerType=0;
            if(MieszkanieData.get(intInsertId).GetNumberOfTenants()==0) //if there are no tenants in the Mieszkanie -> new one is the "owner"
            {
                intOwnerType=1;
                MieszkanieData.get(intInsertId).SetStartDate("01-02-2000 00:00:00");        //"dd-MM-yyyy hh:mm:ss"
                MieszkanieData.get(intInsertId).SetEndDate("01-02-2020 23:59:59");          //"dd-MM-yyyy hh:mm:ss"
                MieszkanieData.get(intInsertId).SetRenter(i);                                           //set renter
            }
            PersonsData.get(i).AddRent(intInsertId,intOwnerType,0);       //Assign to Person, Mieszkanie of id intInsertId, type 0 because Mieszkanie
            MieszkanieData.get(intInsertId).AddTenant(i);                           //assign Person to Mieszkanie's HashSet
            intInsertId=intInsertId+1;
        }
        //Assign ParkingSpots to owners
        intInsertId=0;
        for (String i: PersonsData.keySet())                                       //To every person assign Parking
        {
            PersonsData.get(i).AddRent(intInsertId,1,1);        //Assign to Person, Parking of id intInsertId, always owner so 1, type 1 because Parking
            ParkingData.get(intInsertId).SetRenter(i);                             //set renter

            intInsertId=intInsertId+1;
            if(intInsertId>=ParkingData.size())                                    //Parking spots ended, leave the loop
            {
                break;
            }
        }

        //Assign Items to places
        for (int i: ItemsData.keySet())                                           //Loop through items
        {
            boolean blnAssigned=false;
            Object objItem = ItemsData.get(i);                                  //Get the item from the DataSet
            if (objItem instanceof ClassObject.SamochodTerenowy)
            {
                ClassObject.SamochodTerenowy objRecognizedItem = (ClassObject.SamochodTerenowy) objItem;
                for(int j: ParkingData.keySet())                                    //Loop through parking spots
                {
                    ParkingData.get(j).PutItem(i,objRecognizedItem.ReadSpace());    //Put id of the item into object
                    objRecognizedItem.SetItemAllocateStatus(true);          //change status of item
                    break;                                                          //leave loop car was allocated
                }
                blnAssigned=true;
            }
            if (objItem instanceof ClassObject.SamochódMiejski)
            {
                ClassObject.SamochódMiejski objRecognizedItem = (ClassObject.SamochódMiejski) objItem;
                for(int j: ParkingData.keySet())                                    //Loop through parking spots
                {
                    ParkingData.get(j).PutItem(i,objRecognizedItem.ReadSpace());    //Put id of the item into object
                    objRecognizedItem.SetItemAllocateStatus(true);          //change status of item
                    break;                                                          //leave loop car was allocated
                }
                blnAssigned=true;
            }
            if (objItem instanceof ClassObject.Lodz)
            {
                ClassObject.Lodz objRecognizedItem = (ClassObject.Lodz) objItem;
                for(int j: ParkingData.keySet())                                    //Loop through parking spots
                {
                    ParkingData.get(j).PutItem(i,objRecognizedItem.ReadSpace());    //Put id of the item into object
                    objRecognizedItem.SetItemAllocateStatus(true);          //change status of item
                    break;                                                          //leave loop car was allocated
                }
                blnAssigned=true;
            }
            if (objItem instanceof ClassObject.Motocykl)
            {
                ClassObject.Motocykl objRecognizedItem = (ClassObject.Motocykl) objItem;
                for(int j: ParkingData.keySet())                                    //Loop through parking spots
                {
                    ParkingData.get(j).PutItem(i,objRecognizedItem.ReadSpace());    //Put id of the item into object
                    objRecognizedItem.SetItemAllocateStatus(true);          //change status of item
                    break;                                                          //leave loop car was allocated
                }
                blnAssigned=true;
            }
            if (objItem instanceof ClassObject.Amfibia)
            {
                ClassObject.Amfibia objRecognizedItem = (ClassObject.Amfibia) objItem;
                for(int j: ParkingData.keySet())                                    //Loop through parking spots
                {
                    ParkingData.get(j).PutItem(i,objRecognizedItem.ReadSpace());    //Put id of the item into object
                    objRecognizedItem.SetItemAllocateStatus(true);          //change status of item
                    break;                                                          //leave loop car was allocated
                }
                blnAssigned=true;
            }
            if ((objItem instanceof ClassObject.Item) && (blnAssigned==false))
            {
                ClassObject.Item objRecognizedItem = (ClassObject.Item) objItem;
                for(int j: MieszkanieData.keySet())
                {
                    MieszkanieData.get(j).PutItem(i,objRecognizedItem.ReadSpace());                                                     //Put id of the item into object
                    objRecognizedItem.SetItemAllocateStatus(true);
                    break;
                }
            }
        }

        //Information for the user
        System.out.println("Sample data have been loaded:");
        System.out.println("- Estate count: " + EstateData.size());
        System.out.println("- Block count: " + BlockData.size());
        System.out.println("- Mieszkanie count: " + MieszkanieData.size());
        System.out.println("- MiejsceParkingowe count: " + ParkingData.size());
        System.out.println("- Persons count: " + PersonsData.size());
        System.out.println("- Items count: " + ItemsData.size());
        System.out.println();
    }
    public static void vPresentAvailableOperations()
    {
        //List user all available operations he can perform
        System.out.println("Which operation would you like to perform, please insert appropriate number:");
        System.out.println("- 1, choose person you are.");
        System.out.println("- 2, print your data in the console, together with rented apartments.");
        System.out.println("- 3, print available apartments and parking spots.");
        System.out.println("- 4, choose and rent Mieszkanie or Miejsce parkingowe.");
        System.out.println("- 5, choose apartment rented by specific person and print its content.");
        System.out.println("- 6, put new vehicles/items into the location.");
        System.out.println("- 7, take out items or vehicles.");
        System.out.println("- 8, save current state of the estate into file.");
        System.out.println("- 9, end program.");
        System.out.println();
      }
    public static boolean blnDoesOperationExists(String strSelectedOper)
    {
        //Method to validate if operation selected by the user exists -> true, otherwise false
        switch(strSelectedOper)
        {
            case "1","2","3","4","5","6","7","8","9":
                return true;
            default:
                return false;
        }
    }
}
