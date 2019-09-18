
package com.jdog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Test;

import io.netty.handler.codec.LineBasedFrameDecoder;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.netty.Connection;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpClient;

public class MainTests {
    
    private PrintStream out = System.out;
    
    @Test
    public void test_getProfileEnvKafka() {
        Environment env = mock(Environment.class);
        Main main = new Main(new Server(), env);
        when(env.getCdrProfile()).thenReturn(Environment.CdrProfile.KAFKA);
        
        assertEquals(KafkaProducingService.class, main.getService().getClass());
    }
    
    @Test
    public void test_newService() throws InterruptedException {
        
        Environment env = mock(Environment.class);
        when(env.getCdrProfile()).thenReturn(com.jdog.Environment.CdrProfile.NONE);
        
        Main main = new Main(new Server(), env);
        DisposableServer ds = main.runAsync();
    

        final CountDownLatch latch = new CountDownLatch(3);

        final TcpClient client = TcpClient.create()
		                                  .host("localhost")
		                                  .port(9000);
                                          
        Connection connectedClient = client.handle((in, out) -> {
			//in
			in.withConnection(c -> {
                c.addHandler(new LineBasedFrameDecoder(152));
            }).receive()
			  .asString()
			  .log("receive")
			  .subscribe(data -> {
                System.out.println("received response");  
                latch.countDown();});

             char[] large = new char[200]; Arrays.fill(large, 'L');
             char[] small = new char[100]; Arrays.fill(small, 'S');
			//out
            return out.sendString(Flux.just( 
                new String(small) + System.lineSeparator(),
                new String(small) + System.lineSeparator(), 
                new String(small) + System.lineSeparator()
            )).neverComplete();
        })
        // .wiretap(true)
        .connectNow();
        
    assertTrue(latch.await(2, TimeUnit.SECONDS));
    assertEquals(latch.getCount(), 0);
    }

    @Test
    public void test_getProfileEnvNone() {
        Environment env = mock(Environment.class);
        Main main = new Main(new Server(), env);
        when(env.getCdrProfile()).thenReturn(com.jdog.Environment.CdrProfile.NONE);

        assertEquals(DefaultService.class,  main.getService().getClass());
    }

    @Test
    @Ignore
    public void learnEnv() {
        out.println(System.getenv("NEWVAR"));
    }

    
}
