package org.theanarch.login;

import java.util.prefs.Preferences;

import static org.theanarch.login.Client.*;

public class Main {

    public static String serverAddress = "127.0.0.1";
    public static int serverPort = 8000;

    public static void main(String[] args){
        new Server();

        try{
            Thread.sleep(1000);
            create("USERNAME", "PASSWORD");

            login("USERNAME", "PASSWORD");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void savePreference(String key, String value){
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        prefs.put(key, value);
    }

    public static String readPreference(String key){
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        return prefs.get(key, "");
    }
}
