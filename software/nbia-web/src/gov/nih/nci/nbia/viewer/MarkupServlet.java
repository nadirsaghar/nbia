/**
* $Id: MarkupServlet.java 14161 2011-04-06 18:25:17Z kascice $
*
* $Log: not supported by cvs2svn $
*/
package gov.nih.nci.nbia.viewer;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

abstract class MarkupServlet extends HttpServlet {
  
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */       
    abstract void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException;
   
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    { 
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
       processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo()
    {
        return "Series file URI list";
    }
    
    public boolean isBlank(String value) {
        return ((value == null) || value.equals("") || value.equals("\n") ||
        (value.length() == 0));
    }
    
    void error(HttpServletResponse response, int code, String msg) throws IOException
    {
        response.setContentType("text/plain;charset=UTF-8");
        response.setStatus(code);
        PrintWriter out = response.getWriter();
        out.println(msg);
        out.close();
    }   
}
