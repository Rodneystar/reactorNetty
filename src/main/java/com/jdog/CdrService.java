
package com.jdog;

import reactor.core.publisher.Flux;
import reactor.netty.NettyOutbound;

public interface CdrService {

    public void persist(Flux<MessageWithSender> line, NettyOutbound out);
}
