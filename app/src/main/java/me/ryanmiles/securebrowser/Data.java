package me.ryanmiles.securebrowser;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import me.ryanmiles.securebrowser.model.TabOut;

/**
 * Created by Ryan Miles on 8/22/2016.
 */
public class Data {
    public static final String SHAREDPREF_FIRST_NAME_KEY = "first_name";
    public static final String SHAREDPREF_LAST_NAME_KEY = "last_name";
    public static final String SHAREDPREF_STUDENT_ID_KEY = "student_id";

    public static String EMAIL_ADDRESS = "";
    public static ArrayList<TabOut> TABOUTLIST;
    public static LinkedHashMap<String, String> EMAIL_LIST = new LinkedHashMap<String, String>() {{
        put("Select Teacher", "");
        put("Miles, Ryan", "ryanm1114@gmail.com");
        put("Vamplew, Ryan", "Ryan_vamplew@gwinnett.k12.ga.us");
        put("Rutledge, Abby", "Abby_Rutledge@gwinnett.k12.ga.us");
    }};
    public static String FIRST_NAME;
    public static String LAST_NAME;
    public static int STUDENT_ID;


}
