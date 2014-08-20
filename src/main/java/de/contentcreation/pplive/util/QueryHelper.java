package de.contentcreation.pplive.util;

import java.util.List;
import java.util.Set;

public class QueryHelper {
    
    private static final String empty = "(NULL)";

    public static String getInClause(List<Integer> numbers) {
        if(numbers.size() > 0) {
        String temp = numbers.toString().replace("[", "(");
        String inClause = temp.replace("]", ")");
        return inClause;
        } else {
            return empty;
        }
    }

    public static String getInClauseStrings(Set<String> strings) {
        String inClause = "(";
        for (String s : strings) {
            inClause = inClause + "'" + s + "'" + ",";
        }

        inClause = (strings.size() > 0)? inClause.substring(0, inClause.length() - 1) + ")" : empty;
        return inClause;
    }

    public static String getInClauseStringArray(String[] strings) {
        String inClause = "(";
        for (String s : strings) {
            inClause = inClause + "'" + s + "'" + ",";
        }
        inClause = (strings.length > 0)? inClause.substring(0, inClause.length() - 1) + ")" : empty;
        return inClause;
    }

}
