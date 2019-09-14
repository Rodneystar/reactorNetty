package com.jdog;
import reactor.netty.DisposableServer;

public class Main {

    Server server;
    Environment env;

    public Main(Server server, Environment env) {
        this.server = server;
        this.env = env;
    }

    public void run() {
        System.out.println("starting up... ");

        DisposableServer ds = server.startServer(9000, getService());
        ds.onDispose().block();
    }
    public static void main(String[] args) throws InterruptedException {
        Server s = new Server();
        new Main(s, new Environment()).run();
    
    }
    
    CdrService getService() {
        switch(this.env.getCdrProfile()) {
            case KAFKA: return new KafkaProducingService("localhost:9092", "test");
            case CONSOLE: return new DefaultService();
            default: return new DefaultService();
        }
    }

    
}