import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import javax.swing.JFileChooser;

public class MergeCSV {
    public static void main(String[] args)
    {
        Integer intFileCount=0;
        try
        {
            //Get folder location for merging
            String strDirPath = fnSelectDirectory();

            if(strDirPath!=null)
            {
                File dir = new File(strDirPath);                        //set location
                File[] directoryListing = dir.listFiles();              //import file names into array
                fnCreateFile("MergeCSV.csv");           //create merge file
                String strMergeFile=strDirPath + "\\MergeCSV.csv";      //path to merge file
                FileWriter file = new FileWriter(strMergeFile);         // Creates a FileWriter
                BufferedWriter output = new BufferedWriter(file);       // Creates a BufferedWriter

                for (File child : directoryListing)                                                     //Loop through files in selected directory
                {
                    String strFileName=child.getName();                                                 //Get file name
                    String strExtension = strFileName.substring(strFileName.lastIndexOf('.') + 1);  //Get extension
                    if(strExtension.toUpperCase().equals("CSV"))                                        //Check if extension is CSV -> only merge CSV
                    {
                        intFileCount=intFileCount+1;                //increase count of CSV files
                        String strCurrentLine;
                        BufferedReader objReader = new BufferedReader(new FileReader(child));
                        while ((strCurrentLine = objReader.readLine()) != null)
                        {
                            output.append(strCurrentLine);          //append new line
                            output.append(";" + child.getName());   //add file name in new column
                            output.newLine();                       //move to next line
                        }
                    }
                }
                output.close();
                System.out.println("Number of merged files: " + intFileCount);
            } else {        //case folder was not selected
                System.out.println("Script will end, please select folder next time.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //==================================================================================================================
    public static String fnSelectDirectory()
    {
        JFileChooser chooser;
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));         //Set default directory
        chooser.setDialogTitle("Please select folder");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);         //User see only folders
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION)
        {
            System.out.println("Selected path: '" + chooser.getSelectedFile().toString() + "'.");
            return chooser.getSelectedFile().toString();
        }
        else
        {
            System.out.println("No folder has been selected.");
            return null;
        }
    }
    //==================================================================================================================
    public static void fnCreateFile(String strFileNameToCreate)
    {
        try {
            File myObj = new File(strFileNameToCreate);
            if (myObj.createNewFile())
            {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}
