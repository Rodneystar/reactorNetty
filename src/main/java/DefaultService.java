import io.netty.channel.ChannelHandler;
import reactor.core.publisher.Flux;
import reactor.netty.NettyOutbound;

public class DefaultService {


    public void persist(Flux<String> line, NettyOutbound out) {
        line.subscribe((l) -> {

            String msg = String.format("req no: %s", l);
            System.out.println(msg);

        }, (error) -> {
            System.out.println("error happened");
        });
       
    }
}