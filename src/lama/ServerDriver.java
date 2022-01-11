package lama;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerDriver {
    private static int sa;
    private static int ba;
    private static String ip;
    private final int mode;
    private int rounds;
    private final int[] difficulty;

    ServerDriver(int sa,int ba,String ip, int mode,int[] difficulty){
        ServerDriver.sa = sa;
        ServerDriver.ba = ba;
        ServerDriver.ip = ip;
        this.mode = mode;
        this.difficulty = difficulty;
    }

    ServerDriver(int sa,int ba,String ip, int mode, int rounds,int[] difficulty){
        ServerDriver.sa = sa;
        ServerDriver.ba = ba;
        ServerDriver.ip = ip;
        this.rounds = rounds;
        this.mode = mode;
        this.difficulty = difficulty;
    }


    public synchronized void start() throws RemoteException{
        System.setProperty("java.rmi.server.hostname",ip);
        //System.setProperty("java.rmi.server.codebase", "25.38.253.90");
        ServerIF server;
        if (mode == 2) {
            server = new Server(sa,ba, mode, rounds,difficulty);
        }
        else {
            server = new Server(sa,ba,mode,difficulty);
        }
        Registry registry = LocateRegistry.createRegistry(1091);
        System.setProperty("java.security.policy", "file:./security.policy");
        registry.rebind("Chat", server);
        System.err.println("Server Running...");
    }
}
