import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
/**
 *
 * @author conspd
 */
public class Analyser {
    private PrintWriter out;
    private Scanner sc;
    private FSDataInputStream inputStream;
    private FSDataOutputStream outputStream;
    private String w1,w2,w3;
    private final ArrayList<String> keyWords;
    private final ArrayList<String> devices;
    private final String[] arrName = {"Keywords","Devices","Countries"};
    private int numTweets;

    
    Analyser(PrintWriter out, String w1, String w2, String w3,int numTweets){
        this.out = out;
        this.w1 = w1.replaceAll("[^a-zA-Z_0-9]+", "");
        this.w2 = w2.replaceAll("[^a-zA-Z_0-9]+", "");
        this.w3 = w3.replaceAll("[^a-zA-Z_0-9]+", "");
        this.numTweets = numTweets;
        
        keyWords = new ArrayList<>();
        keyWords.add(w1);
        keyWords.add(w2);
        keyWords.add(w3);
        
        devices = new ArrayList<>();
        devices.add("iPhone");
        devices.add("Android");
        devices.add("PC");
        devices.add("Other");
    }
    
    
    public void run() throws Exception{
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://localhost:9000");
        FileSystem fileSystem = FileSystem.get(conf);
        calcStatistics(fileSystem);
        prepareMahoutInput(fileSystem);
        fileSystem.close();
        drawLast();
    }
    
    public void calcStatistics(FileSystem fileSystem) throws IOException{
        MyRunner.mapRedRun();
        Path mapRedReadPath = new Path("hdfs://localhost:9000/twitter_data/MapRedOutput/part-00000");
        String key;
        int val;
        
        for(int i=0;i<3;i++){
            inputStream = fileSystem.open(mapRedReadPath);
            sc = new Scanner(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            out.println("<table>"
                + "<tr>"
                    + "<th>"+arrName[i]+"</th>"
                    + "<th>Frequency</th>"
                + "</tr>");
            while(sc.hasNext()){
                key = sc.next().replaceAll("[^a-zA-Z_]", "");
                val = sc.nextInt();
                if(i == 0)
                    if(keyWords.contains(key))
                        out.print("<tr><td>"+key+"</td><td>"+(((float)val/numTweets)*100)+"%</td></tr>");  
                if(i == 1)
                    if(devices.contains(key))
                        out.print("<tr><td>"+key+"</td><td>"+(((float)val/numTweets)*100)+"%</td></tr>");  
                if(i == 2)
                    if(!keyWords.contains(key) && !devices.contains(key))
                        out.print("<tr><td>"+key+"</td><td>"+(((float)val/numTweets)*100)+"%</td></tr>");  
            }
            out.println("</table><br>");
            sc.close();
            inputStream.close();
        }
    }
    
    public void prepareMahoutInput(FileSystem fileSystem) throws IOException{
        String w, d, c;
        ArrayList<String> countries = new ArrayList<>();
        outputStream = fileSystem.create(new Path("hdfs://localhost:9000/twitter_data/KmeansData.dat"));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(outputStream),true);
        Path mapRedReadPath = new Path("hdfs://localhost:9000/twitter_data/");
        
        List<String> files = getAllFilePath(mapRedReadPath, fileSystem);
        
        for(String f:files){
            inputStream = fileSystem.open(new Path("hdfs://localhost:9000/twitter_data/"+f));
            sc = new Scanner(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            while(sc.hasNext()){
                c = sc.next().replaceAll("[^a-zA-Z_0-9]+", "");
                w = sc.next().replaceAll("[^a-zA-Z_0-9]+", "");
                d = sc.next().replaceAll("[^a-zA-Z_0-9]+", "");
                if(!countries.contains(c.toString()))
                    countries.add(c);
                pw.print((countries.indexOf(c)+1)+" ");
                for(int i=0;i<3;i++)
                    if(w.contains(keyWords.get(i)))
                        pw.print((i+1)+" ");
                for(int i=0;i<4;i++)
                    if(d.contains(devices.get(i)))
                        pw.println((i+1)+" ");
            }
            inputStream.close();
            sc.close(); 
        }
        
        pw.close();
        out.close();
    }
  
    public static List<String> getAllFilePath(Path filePath, FileSystem fs) throws FileNotFoundException, IOException {
        List<String> fileList = new ArrayList<String>();
        FileStatus[] fileStatus = fs.listStatus(filePath);
        for (FileStatus fileStat : fileStatus){
            if (fileStat.isFile()){
                String fileName = fileStat.getPath().toString();
                if(fileName.contains("FlumeData"))
                    fileList.add(fileName.substring(fileName.lastIndexOf("/") + 1));
            }
        }
        return fileList;    
    }
    
    public void drawLast(){
        out.println(" <a href=\"http://localhost:8080/HadoopPage/\"><button class=\"btn\" style=\"vertical-align:middle\"><span>Main Page</span></button></a>");
        out.println(" <a href=\"http://localhost:9870/explorer.html#/twitter_data\"><button class=\"btn\" style=\"vertical-align:middle\"><span>HDFS Site</span></button></a>");
        out.println("</div></body>");
        out.println("</html>");
    }
}
