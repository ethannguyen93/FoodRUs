package ctrl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Cart;
import model.bean.CartBean;
import model.bean.ItemBean;

/**
 * Servlet implementation class CartController
 */
@WebServlet("/CartController")
public class CartController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CartController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	// Input
	// (updateItems)? : true 
	//     (quantity_<item_number>)+ : <quantity>
	// removeItem : <item_number>
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("updateItems") != null){
			// get all the items in the request
			Map<String,String[]> paramMap = request.getParameterMap();
			String[] item = paramMap.get("item");
			Map<String, String> items = new HashMap<String, String>();
			for (String i: item){
				items.put(i, paramMap.get("quantity_"+ i)[0]);
			}
			
			// give to the model to validate
			Cart cart = (Cart) this.getServletContext().getAttribute("cart");
			cart.updateCart(items);
		} else if (request.getParameter("removeItem") != null) {
			String itemNumber = request.getParameter("removeItem");
			Cart cart = (Cart) this.getServletContext().getAttribute("cart");
			cart.removeItem(itemNumber);
			request.setAttribute("itemremoved", true);
		} 
		
		goToCartJSP(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// check if the cart needs to be updated, otherwise give back the cart page
		// test
		doGet(request, response);
	}

	/**
	 * Convenience method for redirecting to the cart jsp. 
	 * Gathers the necessary information to redirect to the cart jsp
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void goToCartJSP(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
			// get all the cart related attributes
		Cart cm = (Cart) this.getServletContext().getAttribute("cart");
		CartBean c = cm.cart;
		
		double subTotal = c.getSubTotal();
		double tax = cm.getTax(c);
		double shipping = cm.getShipping(c);
		double grandTotal = cm.getGrandTotal(c);
		
		Map<ItemBean, Integer> itemlist = cm.getCart();
		request.setAttribute("target", "/cart.jspx");
		request.setAttribute("itemlist", itemlist);
		request.setAttribute("cart", c);
		request.setAttribute("subTotal", subTotal);
		request.setAttribute("tax", tax);
		request.setAttribute("shipping", shipping);
		request.setAttribute("grandTotal", grandTotal);
		request.getRequestDispatcher("/index.jspx")// FIXME should be under web-inf
				.forward(request, response);
	}
}