import java.io.IOException;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

/**
 *
 * @author conspd
 */
public class MyRunner {
    public static void mapRedRun() throws IOException{
        System.out.println("Entered Runner");
        JobConf conf = new JobConf(MyRunner.class);
        conf.setJobName("WordCount");
        
        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(IntWritable.class);
        conf.setMapperClass(MyMapper.class);
        conf.setCombinerClass(MyReducer.class);
        conf.setReducerClass(MyReducer.class);
        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);
        
        String input = "hdfs://localhost:9000/twitter_data/";
        String output = "hdfs://localhost:9000/twitter_data/MapRedOutput";
        
        FileInputFormat.setInputPaths(conf, new Path(input));
        FileOutputFormat.setOutputPath(conf, new Path(output));
        
        try{
            JobClient.runJob(conf);
            System.out.println("Job was Successful");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Job was not Successful");
        }
    }
}
