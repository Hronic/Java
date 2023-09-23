import javax.swing.JOptionPane;
import java.io.File;            // Import the File class
import java.io.FileWriter;      // Import the FileWriter class
import java.io.IOException;     // Import the IOException class to handle errors
import java.util.HashMap;
import java.util.HashSet;

public class UsersOperations
{
    public static String strSelectUser_1(HashMap<String, ClassObject.Person> PersonsData)
    {
        //Admin selects as who he/she is logged into system
        //wybrania którą jest osobą (nie jest potrzebne żadne logowanie, wystarczy wskazanie np. unikalnego numeru osoby)

        String strInputPesel="";
        boolean blnPeselSelected=false;
        int intDisplayCount=0;
        while (blnPeselSelected==false)
        {
            //Loop through all keys in HashMap -> Print all available pesels
            System.out.println("In the system there are following users registered:");
            for (String strKey: PersonsData.keySet())
            {
                if(intDisplayCount==10)
                {
                    System.out.println();
                    intDisplayCount=0;
                }
                System.out.print(PersonsData.get(strKey).ReadName() + " " + PersonsData.get(strKey).ReadSurname() + ": " + strKey + ", ");
                intDisplayCount=intDisplayCount+1;
            }
            System.out.println("\n");

            //Ask for who should he/she log in and validate if exists
            strInputPesel  = JOptionPane.showInputDialog("Please enter Pesel.");
            if(PersonsData.containsKey(strInputPesel))
            {
                System.out.println("You are now logged in as: " + PersonsData.get(strInputPesel).ReadName() + " " + PersonsData.get(strInputPesel).ReadSurname());
                blnPeselSelected=true;
            } else {
                int intYesNo = JOptionPane.showConfirmDialog(null, "Pesel you inserted does not exist, would you like to select it again (Yes) or quit operation (No)?", "Pesel does not exists.", JOptionPane.YES_NO_OPTION);
                if (intYesNo == 1)                  //0 - user clicked "No"
                {
                    System.out.println("User decided not to select another user -> operation will end.\n");
                    blnPeselSelected=true;
                    strInputPesel="";
                }
            }
        }
        return strInputPesel;
    }
    public static void vPrintUsersData_2(String strPesel, HashMap<String, ClassObject.Person> PersonsData)
    {
        //Method to print data of selected user
        //wypisania swoich danych łącznie z wynajętymi pomieszczeniam
        if(strPesel=="")                                                            //Check if pesel is correct
        {
            System.out.println("No user has been selected, please choose one in operation 1, then come back to operation 2" + "\n");
        } else {
            if(PersonsData.containsKey(strPesel))
            {
                String strPersonsData=PersonsData.get(strPesel).ReadPersonsData();
                System.out.println(strPersonsData);
            } else {
                System.out.println("Selected user was not recognized, please choose proper one in operation 1, then come back to operation 2"+ "\n");
            }
        }
    }
    public static void vPrintAvailableLocals_3(HashMap<Integer, ClassObject.Mieszkanie> MieszkanieData, HashMap<Integer, ClassObject.MiejsceParkingowe> ParkingData)
    {
        //Method to print all available / free apartments and Parking spots
        //wyświetlenia wolnych pomieszcze
        String strMieszkaniaAvailable="";
        String strParkingsAvailable="";

        //Get all available apartments
        for (int i: MieszkanieData.keySet())                                      //Assign Parking to Person
        {
            if(MieszkanieData.get(i).GetNumberOfTenants()==0)
            {
                strMieszkaniaAvailable = strMieszkaniaAvailable + " " + i + ",";
            }
        }
        if(strMieszkaniaAvailable!="")
        {
            strMieszkaniaAvailable=strMieszkaniaAvailable.substring(1);      //delete first space
            strMieszkaniaAvailable="Mieszkania of the following Ids are available: \n" + strMieszkaniaAvailable + "\n";
        } else {
            strMieszkaniaAvailable="There are no available apartments. \n";
        }
        //Get all available parking spots
        for (int i: ParkingData.keySet())                                      //Assign Parking to Person
        {
            if(ParkingData.get(i).ReadRenter()=="")
            {
                strParkingsAvailable=strParkingsAvailable + " " + i + ",";
            }
        }
        if(strParkingsAvailable!="")
        {
            strParkingsAvailable=strParkingsAvailable.substring(1);      //delete first space
            strParkingsAvailable="Miejsca parkingowe of the following Ids are available: \n" + strParkingsAvailable + "\n";
        } else {
            strParkingsAvailable="There are no available miejsca parkingowe. \n";
        }

        System.out.println(strMieszkaniaAvailable);
        System.out.println(strParkingsAvailable);
    }
    public static void vRentNewMieszkanie_4(String strPesel,HashMap<Integer, ClassObject.Mieszkanie> MieszkanieData, HashMap<Integer, ClassObject.MiejsceParkingowe> ParkingData,HashMap<String, ClassObject.Person> PersonsData)
    {
        //Rent specific mieszkanie / miejsce parkingowe
        //wynajęcia nowego pomieszczenia, po uprzednim jego wybraniu
        if(strPesel!="")
        {
            String strLocalType="";
            boolean blnLoop=true;
            PairOfValue objResult;
            System.out.println(strLocalType);
            while(blnLoop)
            {
                boolean blnIdCorrect;
                strLocalType= JOptionPane.showInputDialog("Would you like to rent Mieszkanie (0) or Miejsce parkingowe (1)? \n Please type correct value (insert 2 to leave).\n");
                switch(strLocalType)
                {
                    case "0":
                        blnLoop=false;
                        blnIdCorrect=false;
                        while(blnIdCorrect==false)
                        {
                            objResult=vGetIdOfMieszkPark(strLocalType);
                            if(objResult.blnResult==true)               //if Id was found
                            {
                                if(MieszkanieData.containsKey(objResult.intResult()))
                                {
                                    if(MieszkanieData.get(objResult.intResult()).GetNumberOfTenants()==0)
                                    {
                                        MieszkanieData.get(objResult.intResult()).AddTenant(strPesel);
                                        MieszkanieData.get(objResult.intResult()).SetRenter(strPesel);
                                        PersonsData.get(strPesel).AddRent(objResult.intResult(),1,0);
                                        blnIdCorrect=true;
                                    } else {
                                        System.out.println("Mieszkanie of Id '" + objResult.intResult() + "' is already rented by person of pesel: " + MieszkanieData.get(objResult.intResult()).ReadRenter() + ". \n Please select different Mieszkanie. \n");
                                    }
                                } else {
                                    System.out.println("There are no Mieszkanie with Id: " + objResult.intResult() + ". \n Please select different Id. \n");
                                }
                            } else {
                                blnIdCorrect=true;
                            }
                        }
                        break;
                    case "1":
                        blnLoop=false;
                        if(PersonsData.get(strPesel).IsAllowedToRentParking())
                        {
                            blnIdCorrect=false;
                            while(blnIdCorrect==false)
                            {
                                objResult=vGetIdOfMieszkPark(strLocalType);
                                if(objResult.blnResult==true)               //if Id was found
                                {
                                    if(ParkingData.containsKey(objResult.intResult()))
                                    {
                                        if(ParkingData.get(objResult.intResult()).ReadRenter()=="")
                                        {
                                            ParkingData.get(objResult.intResult()).SetRenter(strPesel);
                                            PersonsData.get(strPesel).AddRent(objResult.intResult(),1,1);
                                            blnIdCorrect=true;
                                            System.out.println("You will rent Miejsce Parkingowe of id: " + objResult.intResult() + " for user: " + strPesel);
                                        } else {
                                            System.out.println("Miejsce parkingowe of Id '" + objResult.intResult() + "' is already rented by person of pesel: " + ParkingData.get(objResult.intResult()).ReadRenter() + ". \n Please select different Miejsce Parkingowe.");
                                        }
                                    } else {
                                        System.out.println("There are no Miejsce parkingowe with Id: " + objResult.intResult() + ". \n Please select different Id.");
                                    }
                                } else {
                                    blnIdCorrect=true;
                                }
                            }
                        } else {
                            System.out.println("User with pesel: " + strPesel + " is not allowed to rent any more Miejsce parkingowe.\n");
                        }
                        break;
                    case "2":
                        blnLoop=false;
                        System.out.println("User decided to leave operation.");
                        break;
                    default:
                        System.out.println("Please select correct operation.");
                        break;
                }
            }
        } else {
            System.out.println("User who would rent the local was not selected. \n Please select the user first from operation 1 and then use operation 4 to rent local. \n");
        }
    }
    private static PairOfValue vGetIdOfMieszkPark(String strMieszkPark)
    {
        PairOfValue objResult = new PairOfValue();
        boolean blnIdFound=false;
        String strFoundId="";
        int intFoundId;

        while(blnIdFound==false)
        {
            switch(strMieszkPark)
            {
                case "0":
                    strFoundId= JOptionPane.showInputDialog("Which Mieszkanie would you like to rent for person (please provide Id of free Mieszkanie)?\n");
                    break;
                case "1":
                    strFoundId = JOptionPane.showInputDialog("Which Miejsce parkingowe would you like to rent for person (please provide Id of free Parking)?\n");
                    break;
            }
            if(IsNumeric(strFoundId))
            {
                intFoundId=Integer.valueOf(strFoundId);
                objResult.InsertValues(intFoundId,true);
                blnIdFound=true;
            } else {
                //Provided value was not numeric
                System.out.println("Please insert numeric value as Id.\n");
                String strExit = JOptionPane.showInputDialog("Would you like to select proper Id again (0) or leave(1)?\n");
                if(strExit.equals("1"))
                {
                    blnIdFound=true;
                }
            }
        }
        return objResult;
    }

    public static void vPrintItems_5(String strPesel,HashMap<String, ClassObject.Person> PersonsData,HashMap<Integer, ClassObject.Mieszkanie> MieszkanieData, HashMap<Integer, ClassObject.MiejsceParkingowe> ParkingData,HashMap<Integer, Object> ItemsData)
    {
        // wybrania pomieszczenia które wynajmuje dana osoba oraz wyświetlenia zawartości pomieszczenia
        if(strPesel!="")
        {
            int[][] arrCurrentRent=new int[5][3];
            PersonsData.get(strPesel).ReadRent(arrCurrentRent);

                String strAnswer="Would you like to choose from Mieszkanie (0) or Parking (1)?\n";
                if(strAnswer.equals("0"))
                {
                    for(int i=0;i<arrCurrentRent.length;i++)
                    {
                        if(arrCurrentRent[i][2]==0)
                        {
                            System.out.println(arrCurrentRent[i][0]);
                        }
                    }
                    strAnswer="Which Mieszkanie, provide Id?\n";
                    if(IsNumeric(strAnswer))
                    {
                        int intSpace=Integer.valueOf(strAnswer);
                        HashSet<Integer> Items = new HashSet<Integer>();
                        MieszkanieData.get(intSpace).ReadItems(Items);
                        System.out.println("Id of items are:");
                        for(int i:Items)
                        {
                            System.out.println(i);
                        }
                    }

                } else if(strAnswer.equals("1")){
                    for(int i=0;i<arrCurrentRent.length;i++)
                    {
                        if(arrCurrentRent[i][2]==1)
                        {
                            System.out.println(arrCurrentRent[i][0]);
                        }
                    }
                    strAnswer="Which Parking, provide Id?\n";
                    if(IsNumeric(strAnswer))
                    {
                        int intSpace=Integer.valueOf(strAnswer);
                        HashSet<Integer> Items = new HashSet<Integer>();
                        ParkingData.get(intSpace).ReadItems(Items);
                        System.out.println("Id of items are:");
                        for(int i:Items)
                        {
                            System.out.println(i);
                        }
                    }


                }
        }else{
            System.out.println("You did not choose user.");
        }
    }
    public static void vAllocateItemVehicles_6(String strPesel,HashMap<String, ClassObject.Person> PersonsData,HashMap<Integer, ClassObject.Mieszkanie> MieszkanieData, HashMap<Integer, ClassObject.MiejsceParkingowe> ParkingData,HashMap<Integer, Object> ItemsData)
    {
        //Allocate item of specific Id to specific place
        if(strPesel!="")
        {
            if(ItemsData.size()!=0)
            {
                System.out.println("Which item would you like to store?");
                String strItems="";
                for(int i: ItemsData.keySet())
                {
                    strItems=strItems + "," + ItemsData.get(i).toString();
                }
                strItems=strItems.substring(1); //remove first space/ ','
                System.out.println(strItems + ".\n");


                //Where would you like to store
            }else{
                System.out.println("There are no items to be stored, please create them using different operatin.");
            }
        } else {
            System.out.println("In order to store items, you need to be logged as users (who owns at least 1 local).\n Please select user using operation 1.\n");
        }
    }
    public static void vPutOutItem_7(String strPesel,HashMap<String, ClassObject.Person> PersonsData,HashMap<Integer, ClassObject.Mieszkanie> MieszkanieData, HashMap<Integer, ClassObject.MiejsceParkingowe> ParkingData,HashMap<Integer, Object> ItemsData)
    {
        //Put out specific item from specific location
        if(strPesel=="")
        {
            System.out.println("You are not logged as any user -> Please log in before managing items!\n");

        }else{
            System.out.println("You are not logged as specific user -> you will have access to restriced amount of resourcess.\n");
            int[][] arrRentStatus = new int[5][3];
            PersonsData.get(strPesel).ReadRent(arrRentStatus);          //Get rent data

            HashMap<Integer,Integer> ItemsAndLocation= new HashMap<Integer,Integer>();
            for(int i=0;i<arrRentStatus.length;i++)                     //Loop through locals of the person and get ids of stored items
            {
                if(arrRentStatus[i][1]==1)                              //Check if user is owner
                {
                    int intLocalType=arrRentStatus[i][2];               //get type of local
                    HashSet<Integer> InputHashSet= new HashSet<Integer>();      //Ids of stored items to which user has access
                    switch(intLocalType)
                    {

                        case 0:             //Mieszkanie
                            MieszkanieData.get(arrRentStatus[i][0]).ReadItems(InputHashSet);
                            break;

                        case 1:             //Parking
                            ParkingData.get(arrRentStatus[i][0]).ReadItems(InputHashSet);
                            break;
                    }
                    for(int j:InputHashSet)
                    {
                        ItemsAndLocation.put(j,arrRentStatus[i][0]);    //to item id assign location
                    }
                } else {
                    String strType="";
                    if(arrRentStatus[i][2]==0)  //Mieszkanie
                    {
                        strType="Mieszkanie";
                    } else {
                        strType="Parking";
                    }            //Parking
                    System.out.println("You are not an owner of the local id: " + arrRentStatus[i][0] + " of type: " + strType + "-> you can't manage items.");
                }
            }
            if(ItemsAndLocation.size()!=0)
            {
                System.out.println("Stored items to which user has access are as follows:\n");
                for(int j:ItemsAndLocation.keySet())             //loop through items, print them
                {
                    System.out.println(ItemsData.get(j).toString());    //print id and name of the item
                }
                System.out.println();   //next line
                boolean blnLoopActive=true;
                while(blnLoopActive)
                {
                    String strRemoveItemId= JOptionPane.showInputDialog("Which item would you like to remove?\n Please provide proper Id. \n Press cancel to exit.");
                    if(IsNumeric(strRemoveItemId))
                    {
                        int intKey = Integer.valueOf(strRemoveItemId);
                        if(ItemsAndLocation.containsKey(intKey))                       //check if item key was in set
                        {
                            int intLocalId =ItemsAndLocation.get(intKey);
                            Object objItem = ItemsData.get(intKey);
                            boolean blnFound=false;
                            if (objItem instanceof ClassObject.SamochodTerenowy)
                            {
                                ClassObject.SamochodTerenowy objRecognizedItem = (ClassObject.SamochodTerenowy) objItem;
                                objRecognizedItem.SetItemAllocateStatus(false);                             //change status
                                if(MieszkanieData.get(intLocalId).ContainsItem(intKey))
                                {
                                    MieszkanieData.get(intLocalId).RemoveItem(intKey,objRecognizedItem.ReadSpace());    //remove
                                } else if(ParkingData.get(intLocalId).ContainsItem(intKey)){
                                    ParkingData.get(intLocalId).RemoveItem(intKey,objRecognizedItem.ReadSpace());       //remove
                                }
                                blnFound=true;
                            }
                            if (objItem instanceof ClassObject.SamochódMiejski)
                            {
                                ClassObject.SamochódMiejski objRecognizedItem = (ClassObject.SamochódMiejski) objItem;
                                objRecognizedItem.SetItemAllocateStatus(false);                             //change status
                                if(MieszkanieData.get(intLocalId).ContainsItem(intKey))
                                {
                                    MieszkanieData.get(intLocalId).RemoveItem(intKey,objRecognizedItem.ReadSpace());    //remove
                                } else if(ParkingData.get(intLocalId).ContainsItem(intKey)){
                                    ParkingData.get(intLocalId).RemoveItem(intKey,objRecognizedItem.ReadSpace());       //remove
                                }
                                blnFound=true;
                            }
                            if (objItem instanceof ClassObject.Lodz)
                            {
                                ClassObject.Lodz objRecognizedItem = (ClassObject.Lodz) objItem;
                                objRecognizedItem.SetItemAllocateStatus(false);                             //change status
                                if(MieszkanieData.get(intLocalId).ContainsItem(intKey))
                                {
                                    MieszkanieData.get(intLocalId).RemoveItem(intKey,objRecognizedItem.ReadSpace());    //remove
                                } else if(ParkingData.get(intLocalId).ContainsItem(intKey)){
                                    ParkingData.get(intLocalId).RemoveItem(intKey,objRecognizedItem.ReadSpace());       //remove
                                }
                                blnFound=true;
                            }
                            if (objItem instanceof ClassObject.Motocykl)
                            {
                                ClassObject.Motocykl objRecognizedItem = (ClassObject.Motocykl) objItem;
                                objRecognizedItem.SetItemAllocateStatus(false);                             //change status
                                if(MieszkanieData.get(intLocalId).ContainsItem(intKey))
                                {
                                    MieszkanieData.get(intLocalId).RemoveItem(intKey,objRecognizedItem.ReadSpace());    //remove
                                } else if(ParkingData.get(intLocalId).ContainsItem(intKey)){
                                    ParkingData.get(intLocalId).RemoveItem(intKey,objRecognizedItem.ReadSpace());       //remove
                                }
                                blnFound=true;
                            }
                            if (objItem instanceof ClassObject.Amfibia)
                            {
                                ClassObject.Amfibia objRecognizedItem = (ClassObject.Amfibia) objItem;
                                objRecognizedItem.SetItemAllocateStatus(false);                             //change status
                                if(MieszkanieData.get(intLocalId).ContainsItem(intKey))
                                {
                                    MieszkanieData.get(intLocalId).RemoveItem(intKey,objRecognizedItem.ReadSpace());    //remove
                                } else if(ParkingData.get(intLocalId).ContainsItem(intKey)){
                                    ParkingData.get(intLocalId).RemoveItem(intKey,objRecognizedItem.ReadSpace());       //remove
                                }
                                blnFound=true;
                            }
                            if ((objItem instanceof ClassObject.Item) && (blnFound==false))
                            {
                                ClassObject.Item objRecognizedItem = (ClassObject.Item) objItem;
                                objRecognizedItem.SetItemAllocateStatus(false);                             //change status
                                if(MieszkanieData.get(intLocalId).ContainsItem(intKey))
                                {
                                    MieszkanieData.get(intLocalId).RemoveItem(intKey,objRecognizedItem.ReadSpace());    //remove
                                } else if(ParkingData.get(intLocalId).ContainsItem(intKey)){
                                    ParkingData.get(intLocalId).RemoveItem(intKey,objRecognizedItem.ReadSpace());       //remove
                                }
                            }
                            blnLoopActive=false;
                        } else {
                            System.out.println("You do not have access to item for current selection.\n Choose different one. \n");       //incorrect item
                        }
                    } else if(strRemoveItemId==null)
                    {
                        blnLoopActive=false;
                    }else{
                        System.out.println("Please provide numeric value of the Id -> integer+\n");           //not numeric
                    }
                }

            }else{
                System.out.println("There are no stored items in Locals to which user has access");
            }
        }
    }
    public static void vSaveEstateToFile_8(HashMap<Integer, ClassObject.Estate> EstateData,HashMap<Integer, ClassObject.Block> BlockData, HashMap<Integer, ClassObject.Mieszkanie> MieszkanieData, HashMap<Integer, ClassObject.MiejsceParkingowe> ParkingData, HashMap<String, ClassObject.Person> PersonsData, HashMap<Integer, Object> ItemsData)
    {

        //Prepare file to be saved
        String strFileName = "s24056_Project1_GUI.txt"; //Name of the file to be saved
        boolean blnSaveData=false;                      //defines if data should be saved (true) or not (false)
        try {
            File myObj = new File(strFileName);
            if (myObj.createNewFile())
            {
                System.out.println("File created: " + myObj.getPath());
                blnSaveData=true;                                           //data should be saved to the file
            } else {
                int intYesNo = JOptionPane.showConfirmDialog(null, "File already exists, would you like to delete it and continue (Yes) or stop the operation (No)?", "File already exists.", JOptionPane.YES_NO_OPTION);
                if (intYesNo == 0)                  //0 - user clicked "yes"
                {
                    if(myObj.delete())              //try to delete the file and create it again
                    {
                        myObj.createNewFile();
                        System.out.println("File created: " + myObj.getPath());
                        System.out.println("Please wait for data to be saved, it might take a while, you will be informed after finish.");
                        blnSaveData=true;                                           //data should be saved to the file
                    } else {
                        System.out.println("Something went wrong with deletion of the file, program will end.\n");
                    }
                } else if(intYesNo == 1) {  //1 - user clicked "No"
                    System.out.print("User decided not to delete the file -> operation will end.\n");
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        //Perform the actual export and save
        if(blnSaveData==true)           //Data should be saved -> perform all necessary steps
        {
            String strInsertData="Data are ordered in following way:\n - Estate\n - Blocks\n - Pomieszczenia (Mieszkania + Miejsca Parkingowe ordered by size) and their items (order again by size and name). \n \n At the end of the report - persons living in the estate and their data.\n\n";
            try {
                //Prepare the file to be saved
                FileWriter myWriter = new FileWriter(strFileName);
                for(int intEstate: EstateData.keySet())
                {
                    //List estates
                    strInsertData=strInsertData+"Estate of id: " + intEstate + ", has following blocks:\n";
                    HashSet<Integer> BlocksInEstate = new HashSet<Integer>();
                    EstateData.get(intEstate).ReadBlock(BlocksInEstate);
                    for(int intBlock: BlocksInEstate)
                    {
                        //List blocks
                        strInsertData=strInsertData+", " + intBlock;
                    }
                    //strInsertData=strInsertData.substring(2);
                    strInsertData=strInsertData+"\n\nPomieszczenia located in Estate: " + intEstate + ", are as follows:\n";
                    int[] arrPomieszczeniaId=new int[MieszkanieData.size()+ParkingData.size()];                     //all data will be stored here Id // Size
                    double[] arrPomieszczeniaSize=new double[MieszkanieData.size()+ParkingData.size()];              //all data will be stored here Id // Size
                    String[] arrPomieszczenieType=new String[MieszkanieData.size()+ParkingData.size()];
                    int intCount=0;
                    for(int i:MieszkanieData.keySet())
                    {
                        arrPomieszczeniaId[intCount]=i;                                     //assign ID of Mieszkanie
                        arrPomieszczeniaSize[intCount]=MieszkanieData.get(i).ReadSpace();   //assign size of Mieszkanie
                        arrPomieszczenieType[intCount]="Mieszkanie";
                        intCount=intCount+1;
                    }
                    for(int i:ParkingData.keySet())
                    {
                        arrPomieszczeniaId[intCount]=i;                                     //assign ID of Parking Space
                        arrPomieszczeniaSize[intCount]=ParkingData.get(i).ReadSpace();      //assign size of Parking Space
                        arrPomieszczenieType[intCount]="Miejsce Parkingowe";
                        intCount=intCount+1;
                    }
                    //Sort arrays:
                    vBubbleSort(arrPomieszczeniaId,arrPomieszczeniaSize,arrPomieszczenieType);
                    //Insert into file
                    for(int i=0;i<arrPomieszczeniaId.length;i++)
                    {
                        strInsertData=strInsertData + " " + arrPomieszczenieType[i] + " of Id: " + arrPomieszczeniaId[i] + " with size of: " + arrPomieszczeniaSize[i]+"\n";
                    }
                    //Items
                    strInsertData=strInsertData+"\n\nItems located in Estate: " + intEstate + ", are as follows:\n";
                    int[] arrItemId=new int[ItemsData.size()];                     //all data will be stored here Id // Size
                    double[] arrItemSize=new double[ItemsData.size()];              //all data will be stored here Id // Size
                    String[] arrItemName=new String[ItemsData.size()];              //all data will be stored here Id // Size

                    //Populate array
                    for(int intKey:ItemsData.keySet())
                    {
                        Object objItem = ItemsData.get(intKey);
                        boolean blnFound=false;
                        if (objItem instanceof ClassObject.SamochodTerenowy)
                        {
                            ClassObject.SamochodTerenowy objRecognizedItem = (ClassObject.SamochodTerenowy) objItem;
                            arrItemId[intKey]=intKey;
                            arrItemSize[intKey]=objRecognizedItem.ReadSpace();
                            arrItemName[intKey]=objRecognizedItem.ReadName();
                            blnFound=true;
                        }
                        if (objItem instanceof ClassObject.SamochódMiejski)
                        {
                            ClassObject.SamochódMiejski objRecognizedItem = (ClassObject.SamochódMiejski) objItem;
                            arrItemId[intKey]=intKey;
                            arrItemSize[intKey]=objRecognizedItem.ReadSpace();
                            arrItemName[intKey]=objRecognizedItem.ReadName();
                            blnFound=true;
                        }
                        if (objItem instanceof ClassObject.Lodz)
                        {
                            ClassObject.Lodz objRecognizedItem = (ClassObject.Lodz) objItem;
                            arrItemId[intKey]=intKey;
                            arrItemSize[intKey]=objRecognizedItem.ReadSpace();
                            arrItemName[intKey]=objRecognizedItem.ReadName();
                            blnFound=true;
                        }
                        if (objItem instanceof ClassObject.Motocykl)
                        {
                            ClassObject.Motocykl objRecognizedItem = (ClassObject.Motocykl) objItem;
                            arrItemId[intKey]=intKey;
                            arrItemSize[intKey]=objRecognizedItem.ReadSpace();
                            arrItemName[intKey]=objRecognizedItem.ReadName();
                            blnFound=true;
                        }
                        if (objItem instanceof ClassObject.Amfibia)
                        {
                            ClassObject.Amfibia objRecognizedItem = (ClassObject.Amfibia) objItem;
                            arrItemId[intKey]=intKey;
                            arrItemSize[intKey]=objRecognizedItem.ReadSpace();
                            arrItemName[intKey]=objRecognizedItem.ReadName();
                            blnFound=true;
                        }
                        if ((objItem instanceof ClassObject.Item) && (blnFound==false))
                        {
                            ClassObject.Item objRecognizedItem = (ClassObject.Item) objItem;
                            arrItemId[intKey]=intKey;
                            arrItemSize[intKey]=objRecognizedItem.ReadSpace();
                            arrItemName[intKey]=objRecognizedItem.ReadName();
                            blnFound=true;
                        }
                    }
                    //Sort
                    vBubbleSortDesc(arrItemId,arrItemSize,arrItemName);
                    //Insert
                    for(int i=0;i<arrItemId.length;i++)
                    {
                        strInsertData=strInsertData + " " + arrItemName[i] + " of Id: " + arrItemId[i] + " with size of: " + arrItemSize[i]+"\n";
                        Object objItem = ItemsData.get(i);
                        boolean blnFound=false;
                        if (objItem instanceof ClassObject.SamochodTerenowy)
                        {
                            ClassObject.SamochodTerenowy objRecognizedItem = (ClassObject.SamochodTerenowy) objItem;
                            strInsertData=strInsertData + " " +objRecognizedItem.toString();
                            blnFound=true;
                        }
                        if (objItem instanceof ClassObject.SamochódMiejski)
                        {
                            ClassObject.SamochódMiejski objRecognizedItem = (ClassObject.SamochódMiejski) objItem;
                            strInsertData=strInsertData + " "+ objRecognizedItem.toString();
                            blnFound=true;
                        }
                        if (objItem instanceof ClassObject.Lodz)
                        {
                            ClassObject.Lodz objRecognizedItem = (ClassObject.Lodz) objItem;
                            strInsertData=strInsertData + " "+ objRecognizedItem.toString();
                            blnFound=true;
                        }
                        if (objItem instanceof ClassObject.Motocykl)
                        {
                            ClassObject.Motocykl objRecognizedItem = (ClassObject.Motocykl) objItem;
                            strInsertData=strInsertData + " "+ objRecognizedItem.toString();
                            blnFound=true;
                        }
                        if (objItem instanceof ClassObject.Amfibia)
                        {
                            ClassObject.Amfibia objRecognizedItem = (ClassObject.Amfibia) objItem;
                            strInsertData=strInsertData + " "+ objRecognizedItem.toString();
                            blnFound=true;
                        }
                        if ((objItem instanceof ClassObject.Item) && (blnFound==false))
                        {
                            ClassObject.Item objRecognizedItem = (ClassObject.Item) objItem;
                            strInsertData=strInsertData + " "+ objRecognizedItem.toString();
                            blnFound=true;
                        }
                    }

                    //Persons
                    strInsertData=strInsertData + "\nIn the estate lives following Tenats:\n";
                    for(String strPesel:PersonsData.keySet())
                    {
                        strInsertData=strInsertData+" "+PersonsData.get(strPesel).toString() + "\n";
                    }
                }

                myWriter.write(strInsertData);              //Insert string into file
                myWriter.flush();
                myWriter.close();
                System.out.println("Data have been successfully exported to the file: " + strFileName + ".\n");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }
    public static boolean blnEndProgram_9(String strSelectedOper)
    {
        //Operation to close application
        // zakończenia programu w dowolnym momencie
        System.out.println("User choose operation: " + strSelectedOper);
        System.out.println("Program will end.");        //Line will not be visible for the user if not run from IDE
        return true;
    };
    public static boolean IsNumeric(String strIsItNumber)
    {
        //Validate if provided string is number -> string which consists of numbers only
        return strIsItNumber!= null && strIsItNumber.matches("[0-9]+");
    }
    private static class PairOfValue
    {
        boolean blnResult;
        int intResult;
        PairOfValue()
        {
            this.intResult=0;
            this.blnResult=false;
        }
        public void InsertValues(int intFirst, boolean blnSecond)
        {
            this.intResult=intFirst;
            this.blnResult=blnSecond;
        }
        public boolean blnResult()
        {
            return this.blnResult;
        }
        public int intResult()
        {
            return this.intResult;
        }
    }
    private static void vBubbleSort(int[] arrPomieszczeniaId,double[] arrPomieszczeniaSize, String[] arrPomieszczenieType)
    {
        int n = arrPomieszczeniaSize.length;
        for (int i = 0; i < n-1; i++)
            for (int j = 0; j < n-i-1; j++)
                if (arrPomieszczeniaSize[j] > arrPomieszczeniaSize[j+1])
                {
                    // swap j+1 and j
                    double tempSize = arrPomieszczeniaSize[j];
                    String tempType = arrPomieszczenieType[j];
                    int tempId = arrPomieszczeniaId[j];
                    arrPomieszczeniaSize[j] = arrPomieszczeniaSize[j+1];
                    arrPomieszczenieType[j] = arrPomieszczenieType[j+1];
                    arrPomieszczeniaId[j] = arrPomieszczeniaId[j+1];
                    arrPomieszczeniaSize[j+1] = tempSize;
                    arrPomieszczenieType[j+1] = tempType;
                    arrPomieszczeniaId[j+1] = tempId;
                }
    }
    private static void vBubbleSortDesc(int[] arrPomieszczeniaId,double[] arrPomieszczeniaSize, String[] arrPomieszczenieType)
    {
        int n = arrPomieszczeniaSize.length;
        for (int i = 0; i < n-1; i++)
            for (int j = 0; j < n-i-1; j++)
                if (arrPomieszczeniaSize[j] < arrPomieszczeniaSize[j+1])
                {
                    // swap j+1 and j
                    double tempSize = arrPomieszczeniaSize[j];
                    String tempType = arrPomieszczenieType[j];
                    int tempId = arrPomieszczeniaId[j];
                    arrPomieszczeniaSize[j] = arrPomieszczeniaSize[j+1];
                    arrPomieszczenieType[j] = arrPomieszczenieType[j+1];
                    arrPomieszczeniaId[j] = arrPomieszczeniaId[j+1];
                    arrPomieszczeniaSize[j+1] = tempSize;
                    arrPomieszczenieType[j+1] = tempType;
                    arrPomieszczeniaId[j+1] = tempId;
                }
    }

}
