package zad1;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TravelData
{
    private HashMap<String, String> dicMiejscePL;
    private HashMap<String, String> dicMiejsceEN;
    private File fileFolderWithOffers;
    private File[] lstFilesWithOffers;
    private SimpleDateFormat simpleDateFormatInput = null;              //Input date format
    private SimpleDateFormat simpleDateFormatOutput = null;             //Output date format
    private List<String> lstTravelOffers;     //Travel offers

    TravelData(File dataDir)        //Constructor
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        this.fileFolderWithOffers= dataDir;          //set file
        this.lstFilesWithOffers = dataDir.listFiles();  //get list of files in data folder
        this.lstTravelOffers = new ArrayList<String>();  //Set list with offers
        PopulateMiejsceDictionary();                //populate dicitonary with translations
    }
    public List<String> getOffersDescriptionsList(String loc, String dateFormat)
    {
        List<String> lstResult = new ArrayList<String>();               //result list
        //Set date parsers
        this.simpleDateFormatInput = new SimpleDateFormat("yyyy-MM-dd");    //Input date format
        this.simpleDateFormatOutput = new SimpleDateFormat(dateFormat);            //Output date format

        for(int i=0;i<this.lstFilesWithOffers.length;i++)
        {
            try {
                Scanner scnFileData = new Scanner(this.lstFilesWithOffers[i]); //open scanner on given file
                while (scnFileData.hasNextLine())                           //loop through whole file row by row
                {
                    String strLine = scnFileData.nextLine();                //assing line under variable
                    String[] arrLine = strLine.split("\t");           //split line into array
                    String strLanguage=arrLine[0];
                    String strCountry=TranslateCountry(arrLine[1],strLanguage,loc);
                    String strDateDeparture=arrLine[2];                 //get Date as string
                    String strDateReturn=arrLine[3];                    //get Date as string
                    Date dtmDateDeparture=null;
                    Date dtmDateReturn=null;
                    try {
                        //Convert dates
                        dtmDateDeparture=this.simpleDateFormatInput.parse(strDateDeparture);     //Parse String date into date
                        dtmDateReturn=this.simpleDateFormatInput.parse(strDateReturn);           //Parse String date into date
                        strDateDeparture=this.simpleDateFormatOutput.format(dtmDateDeparture);   //Convert date to output String format
                        strDateReturn=this.simpleDateFormatOutput.format(dtmDateReturn);         //Convert date to output String format
                    } catch(ParseException a)
                    {
                        //System.out.println(a);
                    }
                    String strLocation=TranslateMiejsce(arrLine[4],strLanguage,loc);
                    String strPrice=TranslatePrice(arrLine[5],strLanguage,loc);
                    //1 Kraj; 2 Data wyjazdu; 3 Data powrotu; 4 Miejsce, 5 Cena; 6 Symbol
                    String strInsertLine = strCountry + " " + strDateDeparture + " " + strDateReturn + " " + strLocation + " " + strPrice + " " + arrLine[6];
                    lstResult.add(strInsertLine);   //add offer in string to list
                    String strInsOffTbl = loc + "\t" + strCountry + "\t" + strDateDeparture + "\t" + strDateReturn + "\t" + strLocation + "\t" + arrLine[5] + "\t" + arrLine[6];
                    this.lstTravelOffers.add(strInsOffTbl);
                }
                scnFileData.close();                                    //close scanner
            } catch (FileNotFoundException e) {
                //System.out.println(e);
            }
        }
        return lstResult;
    }
    public SimpleDateFormat getInputDateFormat()
    {
        //returns input date format
        return this.simpleDateFormatInput;
    }
    public SimpleDateFormat getOutputDateFormat()
    {
        //returns output date format
        return this.simpleDateFormatOutput;
    }
    public List<String> getListTravelOffersTable()
    {
        //return list with travel offers for the database
        return this.lstTravelOffers;
    }
    private void PopulateMiejsceDictionary()
    {
        this.dicMiejscePL = new HashMap<String, String>();
        dicMiejscePL.put("morze", "sea");
        dicMiejscePL.put("jezioro", "lake");
        dicMiejscePL.put("góry", "mountains");
        this.dicMiejsceEN = new HashMap<String, String>();
        dicMiejsceEN.put("sea", "morze");
        dicMiejsceEN.put("lake", "jezioro");
        dicMiejsceEN.put("mountains", "góry");
    }
    private String TranslateCountry(String strCountry, String strInputLangauge, String strOutputLangauge)
    {
        String strResult="";
        if(strInputLangauge.equals("pl_PL") || strInputLangauge.equals("pl-PL"))   //for some reason, getting Locale from pl_PL gives Poland instead of Polska
        {
            strInputLangauge="pl";
        }
        if(strOutputLangauge.equals("pl_PL") || strInputLangauge.equals("pl-PL"))   //for some reason, getting Locale from pl_PL gives Poland instead of Polska
        {
            strOutputLangauge="pl";
        }
        //Translation required
        Locale inputLocale = Locale.forLanguageTag(strInputLangauge);   //input language
        Locale outputLocale = Locale.forLanguageTag(strOutputLangauge); //output language
        for (Locale l : Locale.getAvailableLocales())                   //loop through every locale installed
            {
                if (l.getDisplayCountry(inputLocale).equals(strCountry))   //get the country or region name for the specified locale
                {
                    strResult=l.getDisplayCountry(outputLocale);
                    break;
                }
            }
        return strResult;
    }
    private String TranslateMiejsce(String strMiejsce, String strInputLangauge, String strOutputLangauge)
    {
        String strResult="";
        if(strInputLangauge.equals("pl_PL") || strInputLangauge.equals("pl-PL"))
        {
            strInputLangauge="pl";
        }
        if(strOutputLangauge.equals("pl_PL") || strOutputLangauge.equals("pl-PL"))
        {
            strOutputLangauge="pl";
        }
        if(strInputLangauge.equals(strOutputLangauge))
        {
            strResult=strMiejsce;
        } else {
            switch(strOutputLangauge)
            {
                case "pl":
                    if(this.dicMiejsceEN.containsKey(strMiejsce))
                    {
                        strResult=dicMiejsceEN.get(strMiejsce);
                    }
                    break;
                case "en_GB":
                    if(this.dicMiejscePL.containsKey(strMiejsce))
                    {
                        strResult=dicMiejscePL.get(strMiejsce);
                    }
                    break;
                default:
                    strResult=strMiejsce;
            }
        }
        return strResult;
    }
    private String TranslatePrice(String strPrice, String strInputLangauge, String strOutputLangauge)
    {
        String strResult="";
        if(strInputLangauge.equals("pl_PL") || strInputLangauge.equals("pl-PL"))   //for some reason, getting Locale from pl_PL gives Poland instead of Polska
        {
            strInputLangauge="pl";
        }
        if(strOutputLangauge.equals("pl_PL") || strInputLangauge.equals("pl-PL"))   //for some reason, getting Locale from pl_PL gives Poland instead of Polska
        {
            strOutputLangauge="pl";
        }

        Locale inputLocale = Locale.forLanguageTag(strInputLangauge);   //input language
        Locale outputLocale = Locale.forLanguageTag(strOutputLangauge); //output language
        try{
            NumberFormat nf = NumberFormat.getInstance(inputLocale);
            double myNumber = nf.parse(strPrice).doubleValue();         //convert string number into double
            nf = NumberFormat.getInstance(outputLocale);
            strResult=nf.format(myNumber);
        } catch (ParseException e) {
            System.out.println(e);
        }
        return strResult;
    }

}
