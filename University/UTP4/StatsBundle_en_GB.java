package zad1;

import java.util.*;

public class StatsBundle_en_GB extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }
    private Object[][] contents = {
            { "btnRefreshTable", new String("Refresh table") },
            { "jlChooseRegion", new String("Choose language and region of the offer") },
            { "TableTitle", new String("Present data on JTable") }
    };
}