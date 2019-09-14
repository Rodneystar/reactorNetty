
package com.jdog;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;
import reactor.netty.NettyOutbound;

public class KafkaProducingService implements CdrService {
    

    String host;
    String topic;
    KafkaSender<Integer, String> sender;


    public KafkaProducingService(String host, String topic){
        this.host = host;
        this.topic = topic;

        SenderOptions<Integer, String> options = configureSender();
        this.sender = KafkaSender.create(options);


    }


    private SenderOptions<Integer, String> configureSender() {
        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, host);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

       return SenderOptions.<Integer, String>create(producerProps)       
                 .maxInFlight(1024);      
    }
    
    @Override
    public void persist(Flux<MessageWithSender> line, NettyOutbound out) {
        Flux<SenderRecord<Integer, String, Integer>> outboundFlux = 
            line.map(i -> SenderRecord.create(topic, null, null, null, i.asString(),i.hashCode()));
        
        sender.send(outboundFlux)
            .subscribe(r -> System.out.println("we sent something" +r),
            err -> System.err.println(err));
    }


}
