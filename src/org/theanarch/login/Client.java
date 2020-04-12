package org.theanarch.login;

import org.theanarch.login.Socket.SRSocket;

import static org.theanarch.login.Main.*;
import static org.theanarch.login.Socket.SocketHandler.*;

public class Client {

    public static void login(String username, String password){
        SRSocket socket = null;
        try{
            //WE WILL CREATE THE CONNECTION TO THE SERVER
            socket = quickConnect(serverAddress, serverPort);

            //WE WILL START A HANDSHAKE TO CREATE AN ENCRYPTED CONNECTION
            socket.startHandshake();

            //WE WILL SEND THE REQUEST IN A FORMAT THAT REGEX CAN HANDLE TO HELP THE SERVER UNDERSTAND THE REQUEST
            socket.sendEncryptedText("T: Login\r\n" +
                    "U: "+username+"\r\n" +
                    "P: "+password);

            //NOW WE PARSE THE RESPONSE WITH REGEX TO SEE IF WE ARE LOGGED IN
            String[] response = customProtocol(socket.receiveDecryptedText());
            if(response[0].equalsIgnoreCase("true")){
                System.out.println("LOGGED IN");
            }else{
                System.out.println("FAILED TO LOGIN");
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            quickClose(socket);
        }
    }

    public static void create(String username, String password){
        SRSocket socket = null;
        try{
            //WE WILL CREATE THE CONNECTION TO THE SERVER
            socket = quickConnect(serverAddress, serverPort);

            //WE WILL START A HANDSHAKE TO CREATE AN ENCRYPTED CONNECTION
            socket.startHandshake();

            //WE WILL SEND THE REQUEST IN A FORMAT THAT REGEX CAN HANDLE TO HELP THE SERVER UNDERSTAND THE REQUEST
            socket.sendEncryptedText("T: Create\r\n" +
                    "U: "+username+"\r\n" +
                    "P: "+password);

            //NOW WE PARSE THE RESPONSE WITH REGEX TO SEE IF WE ARE CREATED THE USER
            String[] response = customProtocol(socket.receiveDecryptedText());
            if(response[0].equalsIgnoreCase("true")){
                System.out.println("CREATED USER");
            }else{
                System.out.println("FAILED TO CREATE USER");
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            quickClose(socket);
        }
    }
}
