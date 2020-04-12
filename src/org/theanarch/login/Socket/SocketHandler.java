package org.theanarch.login.Socket;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SocketHandler {

    public static SRSocket quickConnect(String address, int port){
        try{
            SRSocket socket = new SRSocket(address, port);
            socket.setTcpNoDelay(true);
            return socket;
        }catch(Exception e){
            //e.printStackTrace();
            return null;
        }
    }

    public static void quickClose(SRSocket socket){
        try{
            if(!socket.isOutputShutdown()){
                socket.shutdownOutput();
            }
            if(!socket.isInputShutdown()){
                socket.shutdownInput();
            }

            socket.close();
        }catch(Exception e){
            //e.printStackTrace();
        }
    }

    public static String[] customProtocol(String response){
        String[] content = new String[response.split("\r\n").length];
        Pattern pattern = Pattern.compile("(.*?): (.*)");

        int count = 0;
        for(String line : response.split("\r\n")){
            Matcher matcher = pattern.matcher(line);
            if(matcher.matches()){
                content[count] = matcher.group(2);
                count++;
            }
        }
        return content;
    }
}
