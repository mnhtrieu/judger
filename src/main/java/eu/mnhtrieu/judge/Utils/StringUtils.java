package eu.mnhtrieu.judge.Utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtils {


    public static String webalize(String str){
        String norm = Normalizer.normalize(str,Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}}+");
        return pattern.matcher(norm).replaceAll("")
                .replaceAll("[^a-zA-Z0-9. ]","")
                .replace(" ","-")
                .toLowerCase();
    }
}
