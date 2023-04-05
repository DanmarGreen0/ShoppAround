package com.online_shopping_rest_api.utils;

import com.online_shopping_rest_api.services.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class InputValidator {

   static public boolean checkUsername(String username){
        //source: https://support.google.com/mail/answer/9211434?hl=en#:~:text=Usernames%20can%20contain%20 letters%20a%2Dz,in%20a%20 row.

        boolean isValid = false;
        final int usernameMinLen =  6;
        final int usernameMaxLen =  30;

        final Pattern illegalSymbols = Pattern.compile("[&=_'-+,<>]");
        final Pattern illegalSymbols1 = Pattern.compile("[.]{2,}");

        if(username.length() >= usernameMinLen &&
                username.length() <= usernameMaxLen){ //check length
            if (Character.compare(username.charAt(0),'.') != 0 && !username.endsWith(".")) //check for . at the beginning and end
                if(!illegalSymbols.matcher(username).find() &&
                        !illegalSymbols1.matcher(username).find()) //check for other illegal symbols
                    isValid = true;
        }

        return isValid;
    }

    static public boolean checkName(String name){

        boolean isValid = false;
        final int minNameLen = 3;
        final int maxNameLen = 24;

        if(name.length() >=  minNameLen && name.length() <= maxNameLen)
            isValid = true;

        return isValid;
    }

    static public boolean checkPassword(String password){

        boolean isValid = false;
        final int validPasswordMinLen = 8;
        final int validPasswordMaxLen = 24;
        final int passwordLength =  password.length();

        //remember to delete
        Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

        log.info(passwordLength + " " + validPasswordMinLen);

        if(passwordLength >= validPasswordMinLen &&
                passwordLength <= validPasswordMaxLen)
            isValid = true;

        return isValid;
    }

   static public boolean checkEmail(String email){
        /*link: https://help.xmatters.com/ondemand/trial/valid_email_format.htm#:~:text=A%20valid%20email%20address%20consists,com%22%20is%20the%20email%20domain.*/
        boolean isValid = false;
        final String[] splitEmail1 = email.split("@");

        final Pattern illegalEmailDomain = Pattern.compile("(^[a-zA-Z0-9]{2,})([.][a-zA-Z]{2,}){1,2}$");
        final Pattern illegalEmailPrefixRegex = Pattern.compile("(([a-z-A-Z0-9]+[._-]{2,}[a-z-A-Z0-9]+)" +
                "|([a-z-A-Z0-9]*[._-]+$)|" +
                "(^[._-]+[a-z-A-Z0-9]*))|" +
                "(([\\\\!#$%&'*+/=?^`{}|\\\"(),:;<>@\\[\\]]+[a-zA-Z0-9]*)|" +
                "([a-zA-Z0-9]*[\\\\!#$%&'*+/=?^`{}|\\\"(),:;<>@\\[\\]]+)|" +
                "([a-zA-Z0-9]*[\\\\!#$%&'*+/=?^`{}|\\\"(),:;<>@\\[\\]]+[a-zA-Z0-9]*)|" +
                "([\\\\!#$%&'*+/=?^`{}|\\\"(),:;<>@\\[\\]]+[a-zA-Z0-9]*[\\\\!#$%&'*+/=?^`{}|\\\"(),:;<>@\\[\\]]+))");


        if(splitEmail1.length == 2){

            final String emailPrefix = splitEmail1[0];
            final String emailDomain = splitEmail1[1];

            final Matcher isMatcherEP = illegalEmailPrefixRegex.matcher(emailPrefix);
            final Matcher isMatcherD = illegalEmailDomain.matcher(emailDomain);

            if(!isMatcherEP.find() && isMatcherD.find()) {

                isValid = true;

            }
        }

        return isValid;
    }
}
