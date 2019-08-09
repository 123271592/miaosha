package com.miaosha.project.mq;



import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaProducerDemo {

    private KafkaProducer<String,String> producer;
    public final static String TEST_TOPIC = "test_topic";
    public final static String BROKER_LIST = "localhost:9092";

    public KafkaProducerDemo(){
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,BROKER_LIST);
        properties.put("serializer.class","kafka.serializer.StringEncoder");
        properties.put("key.serializer.class","kafka.serializer.StringEncoder");
        properties.put("request.required.acks","-1");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<String,String>(properties) ;
    }

    private void produce(){
        for(int i = 0; i < 100 ; i++)
        {
            producer.send(new ProducerRecord<String, String>(TEST_TOPIC,"currentIndex"+i));

            try{
                Thread.sleep(1000);
            }catch (Exception e){

            }
        }
    }

    public static void main(String[] args){
        new KafkaProducerDemo().produce();
    }

}
