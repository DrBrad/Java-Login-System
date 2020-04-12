package org.theanarch.login;

import org.theanarch.login.Socket.SRServerSocket;
import org.theanarch.login.Socket.SRSocket;

import static org.theanarch.login.Main.*;

public class Server {

    public Server(){
        new Thread(new Runnable(){
            private SRSocket socket;

            @Override
            public void run(){
                SRServerSocket serverSocket = null;
                try{
                    serverSocket = new SRServerSocket(serverPort);
                    System.out.println("Server started on port: "+serverPort);

                    while((socket = serverSocket.accept()) != null){
                        //WE SEND THE REQUEST TO A NEW THREAD SO THAT IT DOESNT INTERFERE WITH NEW CONNECTIONS
                        (new ServerThread(socket)).start();
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }finally{
                    try{
                        serverSocket.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
