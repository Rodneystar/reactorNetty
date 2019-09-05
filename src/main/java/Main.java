import reactor.netty.DisposableServer;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Server s = new Server() ;
        DisposableServer ds = s.startServer(9000);
        
        ds.onDispose().block();
        
        
    }
}