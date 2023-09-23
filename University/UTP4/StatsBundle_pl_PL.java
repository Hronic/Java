package zad1;

import java.util.*;

public class StatsBundle_pl_PL extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }
    private Object[][] contents = {
            { "btnRefreshTable", new String("Odswiez tabele") },
            { "jlChooseRegion", new String("Wybierz jezyk i region ofert") },
            { "TableTitle", new String("Przedstaw dane w tabeli") }
    };
}