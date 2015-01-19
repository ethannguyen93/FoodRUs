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
	private static final String ORDER_DIRECTORY_NAME = "order";
	private String orderPath; 
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderController() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public void init() throws ServletException {
    	super.init();
    	orderPath = this.getServletContext().getRealPath("/" + ORDER_DIRECTORY_NAME);
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("target", "/order.jspx");
		File orderFolder = new File(orderPath);
		File[] listofFiles = orderFolder.listFiles();
		List<String> orders = new ArrayList<String>();
		for(File file: listofFiles){
			if (file.isFile()){
				String filename = file.getName();
				filename = filename.substring(0, filename.indexOf("."));
				if (!filename.equals("PO")){
					orders.add(filename);
				}
			}
		}
		request.setAttribute("filenames", orders);
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
