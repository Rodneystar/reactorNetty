package com.jdog;
import java.net.InetAddress;

import io.netty.buffer.ByteBuf;

public class MessageWithSender {

    public ByteBuf message;
    public InetAddress sender;

    public MessageWithSender(Object message, InetAddress sender) {
        this.message = (ByteBuf)message;
        this.sender = sender;
    }
}