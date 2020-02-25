package org.filrouge.controllers;

/*
RULES LIST
    required    integer    email
    minlength(int)    maxlength(int)
    regex(string)    matches(string)
    exists(table.field)    unique(table.field)
    equals(int)    min(int)    max(int)

    ***** HOW TO USE *****
 */


import javafx.scene.control.Label;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FormValidation {

    // Rules which will produce an error
    private static List<String> errors = new ArrayList<>();
    // Error messages that will be return
    private static HashMap<String, String> finalMessages = new HashMap<>();
    private static HashMap<String, String> customMessages = new HashMap<>();
    // Rules that need a numerical argument
    private final static String[] numericalArgs = {"equals", "min", "max", "min_length", "max_length"};
    // Rules that need the value to be numerical
    private final static String[] numericalRules = {"equals", "min", "max"};

    private static Connection db;
    private static PreparedStatement stmt;
    private static String query;


    public static void setDatabase(String dburl, String usr, String pwd){
        try {
            db = DriverManager.getConnection(dburl, usr, pwd);
        } catch (SQLException e) {
            System.out.println("Validation error: unable to access database.\n" + e.getMessage());
        }
    }

    public static void setDatabase(Connection con){
        db = con;
    }

    public static boolean validFields(String[] values, String[][] rules){
        int nbErrors = 0;
        List<String> errors = new ArrayList<>();
        for(int i = 0; i < values.length; i++){
            errors = validField(values[i], rules[i]);
            nbErrors = nbErrors + errors.size();
        }
        if(nbErrors == 0){
            return true;
        }
        else{
            return false;
        }
    }

    public static boolean validFields(String[] values, String[][] rules, Label[] errorLabels){
        int nbErrors = 0;
        List<String> errors = new ArrayList<>();
        for(int i = 0; i < values.length; i++){
            errors = validField(values[i], rules[i]);
            nbErrors = nbErrors + errors.size();
            errorLabels[i].setText(getFirstMessage());
        }
        if(nbErrors == 0){
            return true;
        }
        else{
            return false;
        }
    }

    public static List<String> validField(String value, String[] rules){
        errors.clear();
        finalMessages.clear();
        int intArg = 0;
        long intValue = 0;
        String arg = "";

        for(String rule: rules) {
            // Check if an argument has been provided
            if(rule.lastIndexOf('(') != -1 && rule.lastIndexOf(')') != -1){
                arg = rule.substring(rule.indexOf("(") + 1, rule.lastIndexOf(")"));
                rule = rule.substring(0,rule.length()-arg.length()-2);

                // Search for rules that need numerical arg to parse it
                for(String numericalArg: numericalArgs) {
                    if(rule.equals(numericalArg)){
                        try{
                            intArg = Integer.parseInt(arg);
                        }
                        catch(Exception e){
                            System.out.println(e.toString());

                        }
                    }
                }
            }

            // Search if rule needs numerical value and parse it
            for(String numericalRule: numericalRules) {
                if(rule.equals(numericalRule)){
                    try{
                        intValue = Long.parseLong(value);
                    }
                    catch(Exception e){
                    }
                }
            }

            if(!customMessages.containsKey(rule)) {
                customMessages.put(rule, "Error");
            }

            switch(rule){
                case "required":
                    if(value.isBlank()) errors.add("required");
                    break;

                case "integer":
                    integer(value);
                    break;

                case "regex":
                    regex(value, arg);
                    break;

                case "email":
                    email(value);
                    break;

                case "exists":
                    exists(value, arg);
                    break;

                case "unique":
                    unique(value, arg);
                    break;

                case "min_length":
                    if(value.length() < intArg) errors.add("min_length");
                    break;

                case "max_length":
                    if(value.length() > intArg) errors.add("max_length");
                    break;

                case "min":
                    if(intValue < intArg) errors.add("min");
                    break;

                case "max":
                    if(intValue > intArg) errors.add("max");
                    break;

                case "equals":
                    if(intValue != intArg) errors.add("equals");
                    break;

                case "matches":
                    if(!value.equals(arg)) errors.add("matches");
                    break;

                default:
                    System.out.println("Validation error: \'" + rule + "\' rule does not exist");
            }

        }
        return errors;
    }

    private static void exists(String value, String arg) {
        query = "select * from table where field like ?";
        String table = arg.split("\\.")[0];
        String field = arg.split("\\.")[1];
        try{
            if(db == null){
                System.out.println("Validation error: cannot apply database rules.");
            }
            else{
                query = query.replace("table", table);
                query = query.replace("field", field);
                stmt = db.prepareStatement(query);
                stmt.setString(1, value);
                ResultSet res = stmt.executeQuery();
                if(!res.next() && !value.isBlank()){
                    errors.add("exists");
                }
            }
        }
        catch(Exception e){
            System.out.println("A database error occurred, probably due to a bad argument");
            System.out.println(e);
        }
    }

    private static void unique(String value, String arg) {
        query = "select * from table where field like ?";
        String table = arg.split("\\.")[0];
        String field = arg.split("\\.")[1];
        try{
            if(db == null){
                System.out.println("Validation error: cannot apply database rules.");
            }
            else{
                query = query.replace("table", table);
                query = query.replace("field", field);
                stmt = db.prepareStatement(query);
                stmt.setString(1, value);
                ResultSet res = stmt.executeQuery();
                if(res.next() && !value.isBlank()){
                    errors.add("unique");
                }
            }
        }
        catch(Exception e){
            System.out.println("A database error occurred, probably due to a bad argument");
            System.out.println(e);
        }
    }

    private static void regex(String value, String arg) {
        try{
            if(!value.matches(arg))
                errors.add("regex");
        }
        catch(Exception e) {
            System.out.println("Validation error: regex \'" + arg + "\' is not correct");
        }
    }

    private static void email(String value) {
        String emailRegex = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+(?:[a-zA-Z]{2}|aero|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel)$";
        if(!value.matches(emailRegex))
            errors.add("email");
    }

    private static void integer(String value){
        try{
            Long.parseLong(value);
        }
        catch(Exception e){
            if(!value.isBlank())
                errors.add("integer");
        }
    }

    public static void setMessages(HashMap<String, String> messages) {
        FormValidation.customMessages = messages;
    }

    public static void setMessage(String rule, String message){
        FormValidation.customMessages.put(rule, message);
    }

    public static HashMap<String, String> getMessages() {
        customMessages.forEach((key,value)->{
            if(errors.contains(key)){
                finalMessages.put(key, value);
            }
            if(errors.contains(key) && !customMessages.containsKey(key)){
                finalMessages.put(key, "");
            }
        });
        return finalMessages;
    }

    public static String getMessage(String rule){
        if(errors.contains(rule) && customMessages.containsKey(rule)){
            if(customMessages.containsKey("required")){
                return customMessages.get("required");
            }
            else{
                return customMessages.get(rule);
            }
        }
        else{
            return "";
        }
    }

    public static String getFirstMessage(){
        if(!errors.isEmpty() && !customMessages.isEmpty()){
            return customMessages.get(errors.get(0));
        }
        else{
            return "";
        }
    }
}
