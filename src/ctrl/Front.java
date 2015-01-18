package ctrl;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Category;
import model.Item;
import model.admin;

/**
 * Servlet implementation class Front
 */
@WebServlet({"/Front/*"})
public class Front extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException
	{	
		try {
			ServletContext sc = this.getServletContext();
			Category category = new Category();
			Item item = new Item();
			// save them to the application scope
			sc.setAttribute("category", category);
			sc.setAttribute("item", item);
		} catch (SQLException e) {
			throw new ServletException("failed to initialize models", e);
		} 
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Front()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		showCategory(request);
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")  || pathInfo.equals("/*"))
		{
			response.sendRedirect(request.getContextPath() + "/Front/Home");
			return;
		}
	
		String path = pathInfo.substring(1);
		if (path.equals("Home"))
		{
			this.getServletContext().getNamedDispatcher(path).forward(request, response);
		}else if (path.startsWith("Category"))
		{
			this.getServletContext().getNamedDispatcher("Category").forward(request, response);
		}
		else
		{
			request.setAttribute("target", "404");
			request.getRequestDispatcher("/404.jspx").forward(request, response);
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		this.doGet(request, response);
	}
	
	void showCategory(HttpServletRequest request){
		Category c = (Category) this.getServletContext().getAttribute("category");
		try {
			request.setAttribute("categories", c.getCategories());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
