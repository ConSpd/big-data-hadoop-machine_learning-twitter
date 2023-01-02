
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
/**
 *
 * @author conspd
 */
@WebServlet(urlPatterns = {"/KafkaTwitter"})
public class KafkaTwitter extends HttpServlet {
    private String w1,w2,w3;
    private int numTweets = 100;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            drawFirst(out);
            
            
            if(request.getParameter("search") != null){
                w1 = request.getParameter("k1");
                w2 = request.getParameter("k2");
                w3 = request.getParameter("k3");
                out.println("<table>"
                            + "<tr>"
                                + "<th>User</th>"
                                + "<th>Location</th>"
                                + "<th>Tweet</th>"
                                + "<th>Device</th>"
                            + "</tr>");
                Searcher.run(out,w1,w2,w3,numTweets);
                out.println("</table>");
                out.println(""
                        + "<form action=\"KafkaTwitter\" method=\"POST\">"
                        + "<input name=\"analyse\" type=\"submit\" value=\"Analyse\" class=\"btn\">"
                        + "</form>");
            }
            if(request.getParameter("analyse") != null){
                drawFirst(out);
                Analyser a = new Analyser(out,w1.toString(),w2.toString(),w3.toString(),numTweets);
                try{
                    a.run();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            drawLast(out);
        }
    }
    
    public void drawFirst(PrintWriter out){
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet KafkaTwitter</title>");            
        out.println("<style>"
                + "div{"
                    + "text-align:center;" 
                    + "top:50%;"
                    + "background-color: #FF847C;"
                    + "}"
                + "table{"
                    + "border-collapse: collapse;"
                    + "margin-left:auto;"
                    + "margin-right:auto;"
                    + "}"
                + "th, td{"
                    //+ "background-color: #F8B195;"
                    + "text-align:left;"
                    + "padding:8px;"
                    + "border-bottom: 1px solid #ddd;"
                    + "border-style:dashed"
                    + "}"
                + "th{ background-color: #99B898; text-align:center;} "
                + "tr:nth-child(even) {background-color: #Fdd6ba;}"
                + "tr {background-color: #F8B195;}"
                + "body{"
                    + "background-color: #2A363B;" 
                    +"}"
                + ".btn {" +
                    "display: inline-block;" +
                    "border-radius: 4px;" +
                    "background-color: #99B898;" +
                    "border: none;" +
                    "color: #FFFFFF;" +
                    "text-align: center;" +
                    "font-size: 15px;" +
                    "font-family: \"Lucida Console\", \"Courier New\", monospace;" +
                    "padding: 5px;" +
                    "width: 120px;" +
                    "transition: all 0.5s;" +
                    "cursor: pointer;" +
                    "margin: 5px;" +
                    "}" +
                ".btn span{" +
                    "cursor: pointer;" +
                    "display: inline-block;" +
                    "position: relative;" +
                    "transition: 0.5s;" +
                    "}" +
                ".btn span:after{" +
                    "content: '\\00bb';" +
                    "position: absolute;" +
                    "opacity: 0;" +
                    "top: 0;" +
                    "right: -20px;" +
                    "transition: 0.5s;" +
                    "}" +
                ".btn:hover span {" +
                    "padding-right: 25px;" +
                    "}" +
                    "" +
                ".btn:hover span:after {" +
                    "opacity: 1;" +
                    "right: 0;" +
                    "}"
                + "</style>");
        out.println("</head>");
        out.println("<body><div>");
    }
    
    public void drawLast(PrintWriter out){
        out.println(" <a href=\"http://localhost:8080/HadoopPage/\"><button class=\"btn\" style=\"vertical-align:middle\"><span>Main Page</span></button></a>");
        out.println(" <a href=\"http://localhost:9870/explorer.html#/twitter_data\"><button class=\"btn\" style=\"vertical-align:middle\"><span>HDFS Site</span></button></a>");
        out.println("</div></body>");
        out.println("</html>");
    }
    
    
        
    public void clearLog(){
        try(PrintWriter out = new PrintWriter("/opt/homebrew/Cellar/kafka/3.1.0/kafka-logs/my_first_twitter-0/00000000000000000040.log")){
            out.write("");
        }catch(FileNotFoundException e){
                
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
