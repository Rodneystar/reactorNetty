import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandler;
import reactor.core.publisher.Flux;
import reactor.netty.NettyOutbound;

public class DefaultService {

    AtomicInteger count = new AtomicInteger(0);
    public void persist(Flux<MessageWithSender> line, NettyOutbound out) {
        line.onErrorContinue((error, msg) -> {
            System.out.println("error here");
        })
        .subscribe((l) -> {
            tryReadbytes(l);
        }, (error) -> {
            error.printStackTrace();
        });
    }

    private void tryReadbytes(MessageWithSender l) {
        // l.readBytes(System.out, l.readableBytes());
        // byte[] address = new byte[4];
        // l.readBytes(address);
        // InetAddress inetAddress = Inet4Address.getByAddress(address);
        // System.out.println(inetAddress.getHostName());
        ByteBufInputStream os = new ByteBufInputStream(l.message);
        String stringMessage = "";
        try {
            stringMessage = new String(os.readAllBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(count.getAndIncrement() + " " + l.sender.getHostAddress() + ", " + stringMessage);
    }
}