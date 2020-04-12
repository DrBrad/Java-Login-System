package org.theanarch.login;

import org.theanarch.login.Socket.SRSocket;

import java.security.MessageDigest;

import static org.theanarch.login.Main.*;
import static org.theanarch.login.Socket.SocketHandler.*;

public class ServerThread extends Thread {

    //IF YOU WANT YOU CAN STORE A UUID CONNECTED WITH THE USER FOR A SESSION SO THE USER WONT HAVE TO SEND THE USERNAME
    //AND PASSWORD EVERY TIME THEY WISH TO LOGIN

    private SRSocket socket;

    public ServerThread(SRSocket socket){
        this.socket = socket;
    }

    @Override
    public void run(){
        try{
            //HERE WE SET THE REQUIREMENTS FOR THE SOCKET SO IT WONT TIMEOUT IF THE LATENCY IS TOO MUCH
            socket.setKeepAlive(true);
            socket.setSoTimeout(10000);
            socket.setSoLinger(true, 10000);
            socket.startHandshake();

            //HERE WE GET THE REQUEST AND PARSE IT USING REGEX TO CREATE AN ARRAY
            String[] request = customProtocol(socket.receiveDecryptedText());

            //HERE WE WILL CHECK THE TYPE OF REQUEST
            if(request[0].equalsIgnoreCase("Login")){
                login(request);
            }else if(request[0].equalsIgnoreCase("Create")){
                create(request);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            quickClose(socket);
        }
    }

    public void login(String[] request)throws Exception {
        //WE WILL READ OUR DATABASE - IN THIS CASE WE ARE SAVING IT INTO A PREFERENCE, THIS IS NOT GREAT FOR MORE THAN 5000 USERS THOUGH
        String user = readPreference(request[1].toLowerCase());
        if(!user.equals("")){

            //WE HASH THE PASSWORD TO ENSURE THAT IT IS UN-READABLE IF SOMEONE WHERE TO FIND THE USER FILE
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] fileBytes = messageDigest.digest((request[2]).getBytes());
            String password = byteArrayToHexString(fileBytes);

            //HERE WE CHECK IF THE PASSWORD IS CORRECT
            if(password.equals(user)){
                socket.sendEncryptedText("R: true");
                return;
            }
        }
        socket.sendEncryptedText("R: false");
    }

    public void create(String[] request)throws Exception {
        //WE WILL READ OUR DATABASE - IN THIS CASE WE ARE SAVING IT INTO A PREFERENCE, THIS IS NOT GREAT FOR MORE THAN 5000 USERS THOUGH
        String user = readPreference(request[1].toLowerCase());
        if(user.equals("")){

            //WE MUST HASH THE USERS PASSWORD TO CHECK LEGITIMACY
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] fileBytes = messageDigest.digest((request[2]).getBytes());
            String password = byteArrayToHexString(fileBytes);

            //WE NOW SAVE THE HASHED PASSWORD AND THE USERNAME - WE WILL MAKE THE USERNAME LOWERCASE TO ENSURE NO 2 USERS HAVE THE SAME NAME
            savePreference(request[1].toLowerCase(), password);
            socket.sendEncryptedText("R: true");
            return;
        }
        socket.sendEncryptedText("R: false");
    }

    //HELPS WITH HASH FUNCTIONALITY
    public static String byteArrayToHexString(byte[] b){
        StringBuffer sb = new StringBuffer(b.length * 2);
        for(int i = 0; i < b.length; i++){
            int v = b[i] & 0xff;
            if(v < 16){
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }
}
