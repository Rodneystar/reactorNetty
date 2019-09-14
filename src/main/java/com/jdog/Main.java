package com.jdog;
import reactor.netty.DisposableServer;

public class Main {

    
    public static void main(String[] args) throws InterruptedException {
        Server s = new Server() ;
        DisposableServer ds = s.startServer(9000, getService());
        System.out.println("starting up... ");
        ds.onDispose().block();
    }
    
    private static CdrService getService() {
        if(System.getenv("CDR_PROFILE"))
        return null;
    }

    
}