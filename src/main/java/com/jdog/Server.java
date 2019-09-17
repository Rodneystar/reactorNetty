
package com.jdog;

import reactor.netty.tcp.TcpServer;

import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.json.JsonObjectDecoder;
import reactor.core.Exceptions;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.netty.DisposableServer;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;

public class Server {

    CountDownLatch latch = new CountDownLatch(1);

    CdrService service;

    public void testout() {
        System.out.println("we are printing");
    }

    Consumer<?> propagateErrors = ob -> {
        if (ob instanceof TooLongFrameException) {
            Exceptions.propagate((Error) ob);
        }
    };

    public DisposableServer startServer(int port, CdrService service) {
        this.service = service;
        AtomicBoolean shutdown = new AtomicBoolean();
        AtomicInteger count = new AtomicInteger(0);

        DisposableServer server = TcpServer.create().port(port)
                .handle((NettyInbound in, NettyOutbound out) -> {
                    
                    EmitterProcessor<MessageWithSender> sensorDataProcessor = EmitterProcessor.create();


                    in.withConnection(conn -> {
                        conn.addHandlerLast(new LineBasedFrameDecoder(152));
                        conn.addHandlerLast("tacIp",
                                new AddressContextHandlerAdapter(conn.address().getAddress()));
                        conn.addHandlerLast(new CatchingLineBasedFrameDecoder());
                    }).receiveObject().map(mws -> {
                        if (mws instanceof TooLongFrameException) {
                            throw Exceptions.propagate((TooLongFrameException) mws);
                        }
                        else {
                            return (MessageWithSender) mws;
                        }
                    }).subscribeOn(Schedulers.parallel()).doOnNext(s -> {
                        System.out.println("we are in the doonnext" + s.asString() );
                    }).subscribe(sensorDataProcessor);
                   


                    // service.persist(linesIn, out);
                    return  out.sendString(sensorDataProcessor.map( e -> e.asString()+ System.lineSeparator()));
                }).bindNow();

        return server;
    }

    private InetAddress readAddress(ByteBuf fullMessage) {

        // InetAddress a = Inet4Address.getByAddress("unknown", new
        // byte[]{123,123,123,123});
        InetAddress a = InetAddress.getLoopbackAddress();

        try {
            byte[] addBytes = new byte[4];
            fullMessage.readBytes(addBytes);
            a = Inet4Address.getByAddress(addBytes);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return a;
    }
}
