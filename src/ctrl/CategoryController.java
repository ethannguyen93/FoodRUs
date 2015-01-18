package ctrl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Category;
import model.Item;
import model.bean.CategoryBean;
import model.bean.ItemBean;

/**
 * Servlet implementation class Category
 */
public class CategoryController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CategoryController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			request.setAttribute("target", "item.jspx");
			String path = request.getPathTranslated();
			//path = path.replace("%20", " ");
			Category c = (Category) this.getServletContext().getAttribute("category");
			Item im = (Item) this.getServletContext().getAttribute("item");
			List<ItemBean> items;

			// if path starts with /category, specific category is selected
			// otherwise, if path starts with /allItems, all items are selected
			if (request.getParameter("report") != null){
				String itemName = request.getParameter("search");
				items = im.getItembyName(itemName);
			}else if (path.startsWith("/Category")){
				// get the information after "/category/" in the path			
				String categoryName = path.substring(
						path.substring(1) // removes the first slash
						.indexOf('/')+2); // removes everything before the second slash

				// check if category exists
				if (categoryName.equals("allItems")){
					items = im.getAllItems();
				}else{
				// get all the items for this category
					int categoryID = c.getCategory(categoryName);
					items = im.getItemsByCategoryName(categoryID);
					request.setAttribute("selectedCategory", categoryName);
				}
			} else {
				// TODO log error in a better way
				throw new ServletException("Category controller was reached but path is not acceptable: path = " + path);
			}

			// redirect to category jsp
			if (items.size() == 0){
				request.setAttribute("hasItem", true);
			}else{
				request.setAttribute("hasItem", false);
			}
			//request.setAttribute("categories", allCategories);
			request.setAttribute("items", items);
			request.getRequestDispatcher("/index.jspx")
			.forward(request, response);
		} catch (SQLException e) {
			// TODO need a message?
			throw new ServletException(e);
			
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
