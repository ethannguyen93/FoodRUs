package model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import model.bean.ItemMarshaller;
@XmlRootElement
public class Marshaller {
	@XmlElement(name="item")
	private List<ItemMarshaller> list;
	@XmlAttribute
	private String number;
	public Marshaller(){
		
	}
	public Marshaller(List<ItemMarshaller> list){
		this.list = list;
	}
}
