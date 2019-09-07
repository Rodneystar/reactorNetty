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
        
        sock.close();
        // Thread.sleep(1000);
    }

    @Test
    public void testServerjson() throws UnknownHostException, IOException, InterruptedException {
        int testPort = 9000;
        DisposableServer server = s.startServer(testPort);
         
        System.out.println(server.isDisposed());
        Socket sock = new Socket("localhost", testPort);
     
        
        PrintWriter out = new PrintWriter(sock.getOutputStream(), true );
        
        Flux.interval(Duration.ofNanos(200) ).take(Duration.ofSeconds(5))
        .subscribe((i ) -> {
            out.println("hello worlds");
        });

        
        
        Thread.sleep(5000);
        sock.close();


    }
  
@Test
    public void learninterval() throws InterruptedException {
        Flux.interval( Duration.ofMillis(2)).subscribe((i) -> System.out.println(i));
        Thread.sleep(5000);
    }

}