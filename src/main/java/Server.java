
import reactor.netty.tcp.TcpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.json.JsonObjectDecoder;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
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
        AtomicInteger count = new AtomicInteger(0);

        DisposableServer server = TcpServer.create().host("localhost").port(port)
                .handle((NettyInbound in, NettyOutbound out) -> {
                    in.withConnection(conn -> {
                        conn.addHandlerLast("codec", new LineBasedFrameDecoder(8 * 1024));
            }).receive()
            .asString()
                .subscribeOn(Schedulers.parallel(), false)
                .publishOn(Schedulers.parallel())
                .subscribe((s) -> {
                
                System.out.println("req no: " + count.getAndIncrement() + " " + s);
            });
            return Mono.never();
        }).bindNow();

        return server;
    }
    
}
