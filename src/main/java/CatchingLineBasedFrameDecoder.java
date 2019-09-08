import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.TooLongFrameException;
import reactor.core.Exceptions;

public class CatchingLineBasedFrameDecoder extends ChannelInboundHandlerAdapter {


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) 
        throws Exception {

            
            if(cause instanceof TooLongFrameException) {
                ctx.fireChannelRead("errorhappened".getBytes());
            } else {
                ctx.fireExceptionCaught(Exceptions.propagate(cause));
            }
    }
}