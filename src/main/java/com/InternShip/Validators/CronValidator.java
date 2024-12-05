package com.InternShip.Validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CronValidator {

    private static final String[] cronRegexParts = {
        // Секунди: 0-59, *, */N, X,Y,Z, X-Y
        "^([0-5]?[0-9]|\\*|\\*/[1-5]?[0-9]|([0-5]?[0-9](,[0-5]?[0-9])*)|([0-5]?[0-9]-[0-5]?[0-9]))$",
    
        // Хвилини: 0-59, *, */N, X,Y,Z, X-Y
        "^([0-5]?[0-9]|\\*|\\*/[1-5]?[0-9]|([0-5]?[0-9](,[0-5]?[0-9])*)|([0-5]?[0-9]-[0-5]?[0-9]))$",
    
        // Години: 0-23, *, */N, X,Y,Z, X-Y
        "^([01]?[0-9]|2[0-3]|\\*|\\*/[1-2]?[0-3]|([01]?[0-9](,[01]?[0-9]|2[0-3])*)|([01]?[0-9]-[01]?[0-9]|2[0-3]))$",
    
        // День місяця: 1-31, *, */N, X,Y,Z, X-Y
        "^([1-9]|[12][0-9]|3[01]|\\*|\\*/([1-9]|[12][0-9]|3[01])|([1-9](,[1-9]|[12][0-9]|3[01])*)|([1-9]-[1-9]|[12][0-9]|3[01]))$",

    
        // Місяць: 1-12, *, */N, X,Y,Z, X-Y
        "^([1-9]|1[0-2]|\\*|\\*/[1-9]|([1-9](,[1-9]|1[0-2])*)|([1-9]-[1-9]|1[0-2]))$",
    
        // День тижня: 0-6 (або 7 для неділі), *, */N, X,Y,Z, X-Y
        "^([0-6]|\\*|\\*/[1-6]|([0-6](,[0-6])*)|([0-6]-[0-6]))$"
    };
    

    
    public static Boolean isCronExpressionValid(String cronExpression, StringBuilder errorMessage) {
        String[] cronParts = cronExpression.split("\\s+");

        if (cronParts.length != 6) {
            errorMessage.append("Невірна кількість полів у cron-виразі. Має бути 6 полів.");
            return false;
        }

        
        for (int i = 0; i < cronParts.length; i++) {
            String part = cronParts[i];
            Pattern pattern = Pattern.compile(cronRegexParts[i]);
            Matcher matcher = pattern.matcher(part);
            
            if (!matcher.matches()) {
                errorMessage.append("Помилка в полі " + (i + 1) + " (" + getFieldName(i) + "): " + part);
                return false; 
            }
        }

        errorMessage.append("");
        return true;
        
    }

    
    private static String getFieldName(int index) {
        switch (index) {
            case 0: return "Секунд";
            case 1: return "Хвилин";
            case 2: return "Годин";
            case 3: return "День місяця";
            case 4: return "Місяць";
            case 5: return "День тижня";
            default: return "Невідоме поле";
        }
    }

}
