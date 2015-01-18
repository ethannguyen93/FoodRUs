package ctrl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class OrderController
 */
public class OrderController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("target", "/order.jspx");
		String f = "order";
		String filename = this.getServletContext().getRealPath("/" + f);
		File folder = new File(filename);
		File[] listofFiles = folder.listFiles();
		List<String> file = new ArrayList<String>();
		for(File f1: listofFiles){
			if (f1.isFile()){
				String nfile = f1.getName();
				nfile = nfile.substring(0, nfile.indexOf("."));
				if (!nfile.equals("PO")){
					file.add(nfile);
				}
			}
		}
		request.setAttribute("filenames", file);
		request.getRequestDispatcher("/index.jspx")
		.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
