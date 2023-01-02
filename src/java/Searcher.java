
import com.google.common.collect.Lists;
import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONObject;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author conspd
 */
public class Searcher {

    public static void run(PrintWriter out,String w1, String w2, String w3,int numTweets) throws IOException{
        String location,name,device,text,keyword;
        States states = new States();
        
//        clearLog();
        Properties props = new Properties();
        props.put("bootstrap.servers","localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

        Schema schema = new Schema.Parser().parse(new File("/Users/conspd/School/Big Data/Μy_Kafka_Avro/schema/myrecord.avsc"));
        Injection<GenericRecord,byte[]> recordInjection = GenericAvroCodecs.toBinary(schema);
        
        
        KafkaProducer<String,byte[]> producer = new KafkaProducer<>(props);

        try {
            String consumerKey = "WxFjsPcFwBDEukeoy5waw3wSX";
            String consumerSecret = "sZNUFFRfJhRqU4IArTRdlJz9wQ0bpNk68xbwpKMJ1HS02iWOhS";
            String token = "1518662938059546626-UCV3pa7Zs3CJSzl8YUQV6FKWha9xdg";
            String secret = "L4x2FebIzIpXiGtDNxwoOADvZTmgUY27bcAjAOxa1RClf";

            BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10000);
            StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
            endpoint.trackTerms(Lists.newArrayList(w1,w2,w3));
            Authentication auth = new OAuth1(consumerKey,consumerSecret,token,secret);

            Client client = new ClientBuilder()
                            .hosts(Constants.STREAM_HOST)
                            .endpoint(endpoint)
                            .authentication(auth)
                            .processor(new StringDelimitedProcessor(queue))
                            .build();
            client.connect();



            for(int i=0;i<numTweets;i++) {
                String msg = queue.take();
                JSONObject obj = new JSONObject(msg);
                location = obj.getJSONObject("user").get("location").toString();
                if((location == null) || (states.checkCountry(location)).equals("None")) {
                    i--;
                    continue;
                }

                GenericData.Record avroRecord = new GenericData.Record(schema);

                location = states.checkCountry(location);
                name = obj.getJSONObject("user").getString("name").replace("\n", " ");
                device = obj.getString("source").replace("\n", " ");
                
                if(device.contains("iPhone") || device.contains("iPad"))
                    device = "iPhone";
                else if (device.contains("Android"))
                    device = "Android";
                else if (device.contains("Web App"))
                    device = "PC";
                else
                    device = "Other";
                
                out.println("<tr><td>"+name+"</td>");
                out.println("<td>"+location.replaceAll("_", " ")+"</td>");

                if(obj.getBoolean("truncated")) // Truncated = True όταν το tweet είναι μεγάλο και δεν αναγράφεται όλο στο text
                    text = obj.getJSONObject("extended_tweet").getString("full_text");
                else // False όταν όλο το κείμενο αναγράφεται στο text
                    text = obj.getString("text");
                
                out.println("<td>"+text+"</td>");
                out.println("<td>"+device+"</td></tr>");
                
                text = text.replaceAll("[^a-zA-Z]+", " ");
                if(text.contains(w1))
                    keyword = w1;
                else if(text.contains(w2))
                    keyword = w2;
                else
                    keyword = w3;

                avroRecord.put("text",keyword+"\n");
                //avroRecord.put("id_str", (i+1) + "\n");
                //avroRecord.put("name", name+"\n");                
                avroRecord.put("location",location+"\n");
                avroRecord.put("device", device+"\n");
                
                
                byte[] bytes = recordInjection.apply(avroRecord);				

                ProducerRecord<String,byte[]> record =  new ProducerRecord<>("my_first_twitter",bytes);

                producer.send(record);
                
            }
            producer.close();
            client.stop();
        }catch(Exception e) {
                System.out.println(e);
        }
    }
}
