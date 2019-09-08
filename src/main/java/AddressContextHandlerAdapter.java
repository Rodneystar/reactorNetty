import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class AddressContextHandlerAdapter extends ChannelInboundHandlerAdapter {
    
    private byte[] address;
    public AddressContextHandlerAdapter(byte[] address){
        this.address = address;
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf oMsg = ctx.alloc().buffer();
        oMsg.writeBytes(address);
        oMsg.writeBytes((ByteBuf) msg);
        ctx.fireChannelRead(oMsg);
    }

}