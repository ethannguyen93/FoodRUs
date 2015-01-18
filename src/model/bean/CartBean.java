package model.bean;

import java.util.HashMap;
import java.util.Map;

public class CartBean {
	Map<ItemBean, Integer> c;
	
	public CartBean(){
		this.c = new HashMap<ItemBean, Integer>();
	}
	
	public void addItem(ItemBean i, int quantity){
		if (c.containsKey(i)){
			c.put(i, c.get(i) + quantity);
		}else
			this.c.put(i, quantity);
	}
	public void updateItem(ItemBean i, int quantity){
		if (quantity != 0){
			c.put(i, quantity);
		} else {
			c.remove(i);
		}
	}
	public void removeItem(ItemBean i){
		this.c.remove(i);
	}
	public Map<ItemBean, Integer> getMap(){
		return c;
	}
	/**
	 * Convenience method to get the total price for an item multiplied by its quantity
	 * @param i the item
	 * @return the item total
	 */
	public double getItemTotal(ItemBean i){
		return c.get(i) * i.getPrice(); 
	}
	/**
	 * Convenience method to get the subtotal of the cart
	 * @return the subtotal
	 */
	public double getSubTotal(){
		double subTotal = 0;
		for (ItemBean i : c.keySet()){
			double itemTotal = i.price * c.get(i);
			subTotal += itemTotal;
		}
		return subTotal;
	}
	public boolean isContains(String itemId){
		for (ItemBean i: c.keySet()){
			if (i.id.equals(itemId)){
				return true;
			}
		}
		return false;
	}
	public ItemBean getItemByID(String itemId){
		for (ItemBean i: c.keySet()){
			if (i.id.equals(itemId)){
				return i;
			}
		}
		return null;
	}
}
