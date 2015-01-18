package model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import model.bean.CustomerBean;
import model.bean.ItemBean;
import model.bean.ItemMarshaller;

@XmlRootElement(name="order")
public class Order
{
	@XmlAttribute
	private int id;
	@XmlAttribute
	private String submitted;
	@XmlElement
	private CustomerBean customer;
	@XmlElement(name="items")
	private Marshaller m;
	@XmlElement
	private double total;
	@XmlElement
	private double shipping;
	@XmlElement
	private double HST;
	@XmlElement
	private double grandTotal;
	public Order() {
		
	}
   
	public Order(Cart c, CustomerBean cb, int id) {
		this.id = id;
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		this.submitted = sdf.format(d);
		Map<ItemBean, Integer> order = c.getCart();
		List<ItemMarshaller> list = new ArrayList<ItemMarshaller>();
		for (ItemBean i:order.keySet()){
			list.add(new ItemMarshaller(i.getName(), i.getPrice(), order.get(i), i.getPrice()*order.get(i),i.getId()));
		}
		this.customer = cb;
		this.m = new Marshaller(list);
		this.total = c.cart.getSubTotal();
		this.shipping = c.getShipping(c.cart);
		this.HST = c.getTax(c.cart);
		this.grandTotal = c.getGrandTotal(c.cart);
	}
}