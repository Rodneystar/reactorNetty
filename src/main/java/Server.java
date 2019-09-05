
import reactor.netty.tcp.TcpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import io.netty.handler.codec.json.JsonObjectDecoder;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;

public class Server {

    CountDownLatch latch = new CountDownLatch(1);

    public void testout() {
        System.out.println("we are printing");
    }

    public DisposableServer startServer(int port) {
        AtomicBoolean shutdown = new AtomicBoolean();

        DisposableServer server = TcpServer.create().host("localhost").port(port)
                .handle((NettyInbound in, NettyOutbound out) -> {
                    in.withConnection(conn -> {
                        conn.addHandlerFirst(new JsonObjectDecoder());
            }).receive()
                .asString()
                .subscribe((s) -> {
                System.out.println(s);
            });
            return Mono.never();
        }).bindNow();

        return server;
    }
    
}
