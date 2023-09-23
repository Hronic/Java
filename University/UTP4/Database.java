package zad1;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.util.*;
import java.util.List;

public class Database
{

    private TravelData fileTravelData;
    private String strDbName = "UTP4_BM_S24056_Database";
    private String strOfferTableName="tblOffers";
    private String strURL;

    Database(String strURL, TravelData travelData)      //Constructor
    {
        this.fileTravelData=travelData;
        this.strURL=strURL;
        try {
            Connection connection=connConnectToDatabase();
            Statement st = connection.createStatement();
            st.execute("CREATE DATABASE IF NOT EXISTS " + this.strDbName);
            String strSQL="CREATE TABLE IF NOT EXISTS " + this.strDbName + "." + this.strOfferTableName + " (IdKraju varchar(20), Kraj varchar(40), DataWyjazdu varchar(10), DataPowrotu varchar(10), Miejsce varchar(20), Cena varchar(20), SymbolWaluty varchar(5));";
            st.execute(strSQL);
            st.close();
            connection.close();
        } catch(SQLException exc) {  // nieudane połączenie
            System.out.println("Nieudane połączenie z " + strURL);
            System.out.println(exc);
            System.exit(1);
        }
    }
    public void create()
    {
        //wpisanie do bazy wszystkich ofert, wczytanych z plików
        List<String> lstTravelOffers = fileTravelData.getListTravelOffersTable();
        try {
            Connection connection=connConnectToDatabase();
            PreparedStatement preparedStatement=connection.prepareStatement("INSERT INTO " + this.strDbName + "."+ this. strOfferTableName + " VALUES(?,?,?,?,?,?,?)");
            for (int i=0; i < lstTravelOffers.size(); i++)
            {
                String[] arrLine = lstTravelOffers.get(i).split("\t");     //split line into array
                preparedStatement.setString(1, arrLine[0]);         //IdKraju
                preparedStatement.setString(2, arrLine[1]);         //Kraj
                preparedStatement.setString(3, arrLine[2]);         //DataWyjazdu
                preparedStatement.setString(4, arrLine[3]);         //DataPowrotu
                preparedStatement.setString(5, arrLine[4]);         //Miejsce
                preparedStatement.setString(6, arrLine[5]);         //Cena
                preparedStatement.setString(7, arrLine[6]);         //SymbolWaluty
                preparedStatement.execute();
            }
            preparedStatement.close();
            connection.close();
        } catch(SQLException exc) {  // nieudane połączenie
            System.out.println("Nieudane połączenie z " + strURL);
            System.out.println(exc);
            System.exit(1);
        }
    }
    public void createDB()
    {
        //W poleceniu zadania, metoda ma się nazywać createDB(), w metodzie main jest create()-> stad obie wersje
        create();
    }
    public void showGui()
    {
        //otwarcie GUI z tabelą, pokazującą wczytane oferty
        new ShowResultsInJtable();
    }
    private class ShowResultsInJtable extends JFrame
    {
        private Connection conn = null;
        private Statement stmt=null;
        private JTable tblQueryResults = new JTable();
        private JButton btnRefreshTable;
        private JLabel jlChooseRegion;

        ShowResultsInJtable()                                       //Constructor
        {
            //Labels and buttons
            //String[] arrLanguage=arrGetDescriptionInProperLanguage(new Locale("pl","PL"));    //lokalizacja polska
            String[] arrLanguage=arrGetDescriptionInProperLanguage(Locale.getDefault());        //lokalizacja domyślna
            btnRefreshTable= new JButton (arrLanguage[0]);
            jlChooseRegion= new JLabel(arrLanguage[1]);

            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.setTitle(arrLanguage[2]);
            String strTempSQL;
            JPanel panel =new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            JPanel jpInner1 =new JPanel();
            jpInner1.setLayout(new BoxLayout(jpInner1, BoxLayout.Y_AXIS));

            //Create combo box
            strTempSQL = "SELECT DISTINCT IdKraju FROM " + strDbName + "." + strOfferTableName;
            Vector<String> cboList = lstGetValuesFromColumn(strTempSQL,"IdKraju");
            JComboBox  cboChooseRegion = new JComboBox(cboList);
            cboChooseRegion.setMaximumSize(new Dimension(100,100));

            strTempSQL = "SELECT * FROM " + strDbName + "." + strOfferTableName;
            tblQueryResults=jtblPopulateTableWithData(strTempSQL);
            tblQueryResults.setShowGrid(true);
            tblQueryResults.setShowVerticalLines(true);
            JScrollPane sp = new JScrollPane(tblQueryResults);      //adding it to JScrollPane

            //Action on "Odswiez" button -> reload JTable
            btnRefreshTable.addActionListener(e ->
            {
                String strTemp=cboChooseRegion.getSelectedItem().toString();
                if(strTemp.equals("Wszystko"))
                {
                    strTemp="SELECT * FROM " + strDbName + "." + strOfferTableName;
                } else {
                    strTemp = "SELECT * FROM " + strDbName + "." + strOfferTableName + " WHERE IdKraju ='" + strTemp + "'";
                }
                tblQueryResults=jtblPopulateTableWithData(strTemp);
                sp.setViewportView(tblQueryResults);
            });

            //add elements to panel
            jpInner1.add(jlChooseRegion);
            jpInner1.add(cboChooseRegion);
            jpInner1.add(btnRefreshTable);

            panel.add(jpInner1);
            panel.add(sp);

            //Finalize frame
            this.add(panel);
            this.setSize(600,800);
            this.show();
        }
        private String[] arrGetDescriptionInProperLanguage(Locale currentLocale)
        {
            ResourceBundle stats = ResourceBundle.getBundle("zad1.StatsBundle",currentLocale);
            String[] arrResult=new String[3];
            arrResult[0]=(String)stats.getObject("btnRefreshTable");
            arrResult[1]=(String)stats.getObject("jlChooseRegion");
            arrResult[2]=(String)stats.getObject("TableTitle");
            return arrResult;
        }
        private JTable jtblPopulateTableWithData(String strSQL)
        {
            Vector columnNames = new Vector();
            Vector data = new Vector();
            try {
                this.conn = connConnectToDatabase();
                this.stmt = conn.createStatement();
                ResultSet resultSet = stmt.executeQuery(strSQL);
                ResultSetMetaData metaData = resultSet.getMetaData();

                int columns = metaData.getColumnCount();
                for (int i = 1; i <= columns; i++)
                {
                    columnNames.addElement(metaData.getColumnName(i));
                }
                while (resultSet.next())
                {
                    Vector row = new Vector(columns);
                    for (int i = 1; i <= columns; i++)
                    {
                        row.addElement(resultSet.getObject(i));
                    }
                    data.addElement(row);
                }
                resultSet.close();
                this.stmt.close();
                this.conn.close();
            } catch (Exception exc)  {
                System.out.println(exc.getMessage());
                System.exit(1);
            }
            JTable tblResult = new JTable(data, columnNames);
            return tblResult;
        }
        private Vector<String> lstGetValuesFromColumn(String strSQL, String strColumn)
        {
            Vector<String> vecReturn = new Vector<String>(0);
            vecReturn.add("Wszystko");
            try {
                this.conn = connConnectToDatabase();
                this.stmt = conn.createStatement();
                ResultSet resultSet = stmt.executeQuery(strSQL);
                while(resultSet.next())
                {
                    vecReturn.add(resultSet.getString(strColumn));
                }
                stmt.close();
                conn.close();
            } catch (Exception exc)  {
                System.out.println(exc.getMessage());
                System.exit(1);
            }
            return vecReturn;
        }
    }
    private Connection connConnectToDatabase()
    {
        //make connection to database
        String strUsername = System.getProperty("user.name");           //Get Window's username
        Connection connResult=null;
        try {
            if(!strUsername.equals("Hronic"))
            {
                connResult =  DriverManager.getConnection(strURL);
            } else {
                connResult =  DriverManager.getConnection(strURL, "root", "MySqlPassword");
            }
        } catch(SQLException exc) {  // nieudane połączenie
            System.out.println("Nieudane połączenie z " + strURL);
            System.out.println(exc);
            System.exit(1);
        }
        return connResult;
    }
}
