
package com.jdog;
import java.net.InetAddress;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class AddressContextHandlerAdapter extends ChannelInboundHandlerAdapter {
    
    private InetAddress address;
    public AddressContextHandlerAdapter(InetAddress address){
        this.address = address;
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // ByteBuf oMsg = ctx.alloc().buffer();
        // oMsg.writeBytes(address);
        // oMsg.writeBytes((ByteBuf) msg);
        MessageWithSender mws = new MessageWithSender(msg, this.address);
        ctx.fireChannelRead(mws);
    }

}