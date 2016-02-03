package de.contentcreation.pplive.util;

import de.contentcreation.pplive.model.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Dies ist eine Hilfsklasse, die verschiedene Funktionen zur Verfügung stellt.
 *
 * @author Gösta Ostendorf (goesta.o@gmail.com)
 */
public class QueryHelper {

    private static final String empty = "(NULL)";

    public static String getInClause(List<Integer> numbers) {
        if (numbers.size() > 0) {
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

        inClause = (strings.size() > 0) ? inClause.substring(0, inClause.length() - 1) + ")" : empty;
        return inClause;
    }

    public static String getInClauseStringArray(String[] strings) {
        String inClause = "(";
        for (String s : strings) {
            inClause = inClause + "'" + s + "'" + ",";
        }
        inClause = (strings.length > 0) ? inClause.substring(0, inClause.length() - 1) + ")" : empty;
        return inClause;
    }

    public static String getInClauseUserList(List<User> userList) {
        String inClause = "(";
        for (User u : userList) {
            inClause = inClause + u.getId() + ",";
        }
        inClause = (userList.size() > 0) ? inClause.substring(0, inClause.length() - 1) + ")" : empty;
        return inClause;
    }

    public static List<Integer> StringParameterToIntegerList(String s) {
        ArrayList<Integer> list = new ArrayList<>();
        s = s.replace("[", "");
        s = s.replace("]", "");
        String[] array = s.split("_");
        for (String e : array) {
            try {
                list.add(Integer.parseInt(s));
            } catch (NumberFormatException ex) {

            }
        }
        return list;
    }
    
    public static String IntegerListToStringParameter(List<Integer> list) {
        String partnerListParameter = Arrays.toString(list.toArray());
        partnerListParameter = partnerListParameter.replace(",", "_");
        partnerListParameter = partnerListParameter.replace(" ", "");
        partnerListParameter = partnerListParameter.replace("[", "");
        partnerListParameter = partnerListParameter.replace("]", "");
        return partnerListParameter;
    }

}
