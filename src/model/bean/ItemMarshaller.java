package model.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={"name", "price", "quantity", "extended"})
@XmlRootElement(name="item")
public class ItemMarshaller {
	
	String name;
	@XmlAttribute
	String number;
	double price;
	int quantity;
	double extended;
	
	public ItemMarshaller(){
		
	}
	
	public ItemMarshaller(String name, double price, int quantity, double extended, String number){
		this.number = number;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.extended = extended;
	}

	public double getExtended() {
		return extended;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setExtended(double extended) {
		this.extended = extended;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
