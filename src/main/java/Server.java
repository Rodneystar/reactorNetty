
import reactor.netty.tcp.TcpServer;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.json.JsonObjectDecoder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.netty.DisposableServer;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;

public class Server {

    CountDownLatch latch = new CountDownLatch(1);

    DefaultService service = new DefaultService();
    public void testout() {
        System.out.println("we are printing");
    }

    public DisposableServer startServer(int port) {
        AtomicBoolean shutdown = new AtomicBoolean();
        AtomicInteger count = new AtomicInteger(0);

        DisposableServer server = TcpServer.create().host("localhost").port(port)
                .handle((NettyInbound in, NettyOutbound out) -> {
                    Flux<String> linesIn = in.withConnection(conn -> {
                        conn.addHandlerLast("codec", new LineBasedFrameDecoder(8 * 1024));
                        conn.addHandlerLast("tacIp", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf oMsg;
                                if(msg instanceof ByteBuf) {
                                    oMsg = (ByteBuf) msg;
                                    int newCapacity = oMsg.capacity() + 32;
                                    oMsg.capacity(newCapacity);
                                    oMsg.writeBytes(conn.address().getAddress().getAddress());
                                    ctx.fireChannelRead(oMsg);
                                } else {
                                    ctx.fireChannelRead(msg);
                                }
                            }
                        });
                    })
                    .receive()
                    .asString()
                    .subscribeOn(Schedulers.parallel());
                  
                    service.persist(linesIn, out);
                    
                    return Mono.never();
                }).bindNow();

        return server;
    }

}
