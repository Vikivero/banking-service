package com.banking.service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Validator {

/*    static void wordChecker() {

    } */

    public boolean validateName(String name) {
        String regex = ".*\\d.*";  // regex to check if string contains any numbers
        Pattern pattern = Pattern.compile(regex);  // compiles the regex

        // find match between given string and pattern
        Matcher matcherText = pattern.matcher(name);

        // return true if the string matched the regex
        Boolean textMatches = matcherText.matches();

        return name.trim().length() > 0 && textMatches == true;
    }

    public boolean validateSurname(String surname) {
        String regex = ".*\\d.*";  // regex to check if string contains any numbers
        Pattern pattern = Pattern.compile(regex);  // compiles the regex

        // find match between given string and pattern
        Matcher matcherText = pattern.matcher(surname);

        // return true if the string matched the regex
        Boolean textMatches = matcherText.matches();

        return surname.trim().length() > 0 && textMatches == true;
    }

    public boolean validatePesel(long pesel) {
        try {
            //why does it want me to wrap it??
            Long.parseLong(pesel);
        } catch(NumberFormatException nfe) {
            return false;
        }

        return String.valueOf(pesel).length() == 11;
    }
}
