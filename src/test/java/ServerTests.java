import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Duration;

import org.junit.Before;
import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.netty.DisposableServer;

public class ServerTests {
    Server s;

    @Before
    public void setup() {
        s = new Server();
    }


    @Test
    public void testtestout() {
        s.testout();
    }
    @Test
    public void testServer() throws UnknownHostException, IOException, InterruptedException {
        int testPort = 9000;
        DisposableServer server = s.startServer(testPort);
         
        System.out.println(server.isDisposed());
        Socket sock = new Socket("localhost", testPort);
     
        
        PrintWriter out = new PrintWriter(sock.getOutputStream(), true );
        out.println("hello world");
        
        Thread.sleep(1000000);
        sock.close();
    }
    @Test
    public void testLargeLine() throws UnknownHostException, IOException, InterruptedException {
        int testPort = 9000;
        DisposableServer server = s.startServer(testPort);
         
        System.out.println(server.isDisposed());
        Socket sock = new Socket("localhost", testPort);
     
        
        PrintWriter out = new PrintWriter(sock.getOutputStream(), true );
        
        // Flux.interval(Duration.ofMillis(100) ).take(Duration.ofSeconds(2))
        // .subscribe((i ) -> {
            // if(i%2 == 0) {
                out.println("hello there");
            // } else {
                out.println("hello worldsapsdifjpaiejpafi jsbpaisej pfiasjpijsepaifjpesiajfpioasdjfpaisjdfpaisjepfiajfpisaejfpaisf" + 
                "iopsajfpaisehucnasdiopajfpsiedfjdpeasijpeijfpasiefjpasdiufjbipasixcmpaisjd\ncpasjfpeifjapsoiejfpaismcpaisjfepajsp");
                Thread.sleep(1000);
                System.out.println("closed: " + sock.isClosed() + " connected: " + sock.isConnected() + " in shut down: " 
                    + sock.isInputShutdown() + " out shut down: " + sock.isOutputShutdown());
                out.println("hello there");
             
            // }
        // });

        
        Thread.sleep(1000);

    }

    @Test
    public void testServerjson() throws UnknownHostException, IOException, InterruptedException {
        int testPort = 9000;
        DisposableServer server = s.startServer(testPort);
         
        System.out.println(server.isDisposed());
        Socket sock = new Socket("localhost", testPort);
     
        
        PrintWriter out = new PrintWriter(sock.getOutputStream(), true );
        
        Flux.interval(Duration.ofNanos(200) ).take(Duration.ofSeconds(2))
        .subscribe((i ) -> {
            out.println("hello worlds");
        });

        
        Thread.sleep(1000);

    }
  
@Test
    public void learninterval() throws InterruptedException {
        Flux.interval( Duration.ofMillis(2)).subscribe((i) -> System.out.println(i));
        Thread.sleep(5000);
    }

}