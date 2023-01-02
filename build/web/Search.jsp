<%-- 
    Document   : Search
    Created on : Jun 12, 2022, 4:02:28 PM
    Author     : conspd
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <%@ page    import="java.util.Properties,
                    java.util.concurrent.BlockingQueue,
                    com.google.common.collect.Lists,
                    com.twitter.hbc.ClientBuilder,
                    com.twitter.hbc.core.Client,
                    com.twitter.hbc.core.Constants,
                    com.twitter.hbc.core.endpoint.StatusesFilterEndpoint,
                    com.twitter.hbc.core.processor.StringDelimitedProcessor,
                    com.twitter.hbc.httpclient.auth.Authentication,
                    com.twitter.hbc.httpclient.auth.OAuth1,
                    com.twitter.bijection.Injection,
                    com.twitter.bijection.avro.GenericAvroCodecs,
                    org.apache.avro.Schema,
                    org.apache.avro.generic.GenericData,
                    org.apache.avro.generic.GenericRecord,
                    org.apache.kafka.clients.producer.KafkaProducer,
                    org.apache.kafka.clients.producer.ProducerRecord,
                    java.io.File,
                    java.io.IOException,
                    org.json.*"
                %>
    </head>
    <body>
        <%      Properties props = new Properties();
		props.put("bootstrap.servers","localhost:9092");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        %>
        <h1>Hello World!</h1>
    </body>
</html>
