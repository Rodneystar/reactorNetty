package com.jdog;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.TooLongFrameException;
import reactor.core.Exceptions;

public class CatchingLineBasedFrameDecoder extends ChannelInboundHandlerAdapter {


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) 
        throws Exception {

            ctx.fireChannelRead(cause);
            
    }
}