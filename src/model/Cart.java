package model;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import model.bean.CartBean;
import model.bean.ItemBean;
import model.dao.CategoryDAO;
import model.dao.ItemDAO;

/**
 * Models the cart in the store
 *
 */
public class Cart {
	
	// FIXME replace with web.xml parameters
	public static final double HST_RATE = 0.13;
	private double freeShippingCutoff;
	private double defaultShippingCost;
	public CartBean cart;
	private ItemDAO dao;
	@SuppressWarnings("unused")
	private Cart() {}
	
	// initialize the model 
	public Cart(double freeShippingCutoff, double defaultShippingCost) throws SQLException {
		this.freeShippingCutoff = freeShippingCutoff;
		this.defaultShippingCost = defaultShippingCost;
		this.cart = new CartBean();
		dao = new ItemDAO();
	}
	/**
	 * Validates the given item quantities in the cart and returns items with non-zero quantity
	 * @param itemQuantity the item quantities
	 * @throws InvalidInputException when a item 
	 */
	public Map<String, String> process(Map<String, String> itemQuantity) throws InvalidInputException {
		// TODO code review needed
		
		Map<String, String> results = new HashMap<String,String>();
		for (String itemNumber : itemQuantity.keySet()){
			String quantity = itemQuantity.get(itemNumber);
			try {
				int q = Integer.parseInt(quantity);
				if (q > 0) {
					results.put(itemNumber, quantity);
				} else if (q < 0) {
					throw new InvalidInputException("Quantity '" + quantity + 
							"' for item number '" + itemNumber + "' is negative. "
							+ "Please enter a positive whole number for quantity.");
				}
			} catch (NumberFormatException e){
				throw new InvalidInputException("Quantity '" + quantity + 
						"' for item number '" + itemNumber + "' is not a whole number."
						+ "Please enter a positive whole number for quantity.");
			}
		}
		return results;
	}
	
	public void addItem(String itemID, int itemQuantity){
		try {
			if (cart.isContains(itemID)){
				cart.addToCart(cart.getItemByID(itemID), itemQuantity);
			}else{
				cart.addToCart(dao.getItembyID(itemID), itemQuantity);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void removeItem(String itemID){
		cart.removefromCart(cart.getItemByID(itemID));
	}
	public Map<ItemBean, Integer> getCart(){
		return cart.getMap();
	}
	public CartBean createCart(Map<String,String> itemQuantities, Item im) throws SQLException, NumberFormatException{
		CartBean c = new CartBean();
		for (String itemNumber : itemQuantities.keySet()){
			ItemBean i = im.getItem(itemNumber);
			int quantity = Integer.parseInt(itemQuantities.get(itemNumber));
			c.addToCart(i, quantity);
		}
		return c;
	}
	public void updateCart(Map<String, String> item){
		for (String i: item.keySet()){
			int q = Integer.parseInt(item.get(i));
			cart.updateCart(cart.getItemByID(i), q);
		}
	}
	public double getTax(CartBean cart){
		return cart.getSubTotal() * HST_RATE;
	}
	
	public double getShipping(CartBean cart){
		 
		if (cart.getSubTotal() > freeShippingCutoff || cart.getMap().size() == 0){
			return 0;
		} else {
			return defaultShippingCost;
		}  
	}

	public double getGrandTotal(CartBean cart){
		return cart.getSubTotal() + getTax(cart) + getShipping(cart);
	}
}
