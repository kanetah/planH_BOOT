package top.kanetah.planH.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTool {
    public static String lastRegex(String string, String regex) {
        String returnString = "";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(string);
        while (m.find()) {
            returnString = m.group();
        }
        return returnString;
    }
}
