import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

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

}