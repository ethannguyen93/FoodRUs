package ctrl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import model.admin;

/**
 * Servlet implementation class Admin
 */
@WebServlet("/Admin/getPO")
public class Admin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    @Override
    public void init() throws ServletException{
    	super.init();
    	admin model = (admin) this.getServletContext().getAttribute("model");
    	if (model == null){
    		try {
				model = new admin();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    	this.getServletContext().setAttribute("model", model);
    }
	
    public Admin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getRequestURI();
		if(path.endsWith("/Admin/getPO")){
			String startDate = request.getParameter("start_date");
			String f = "order";
			String filename = this.getServletContext().getRealPath("/" + f);
			admin model = (admin) this.getServletContext().getAttribute("model");
			try {
				List<String> filelist = model.getOrderList(startDate, filename);
				StringBuilder builder = new StringBuilder();
				if (filelist.size() > 0){
					builder.append(filelist.remove(0));
					for (String s:filelist){
						builder.append("\n");
						builder.append(s);
					}
				}
				response.getWriter().println(builder.toString());
				return;
			} catch (SAXException | ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		}
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
}
