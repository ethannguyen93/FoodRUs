package model;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import model.bean.ItemBean;
import model.dao.ItemDAO;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

public class Admin {
	final ItemDAO dao;
	public Admin() throws SQLException{
		dao = new ItemDAO();
	}
	public List<String> getOrderList(String startDate, String folderPath) throws IOException, SAXException, ParserConfigurationException{
		File folder = new File(folderPath);
		File[] listofFiles = folder.listFiles();
		List<String> file = new ArrayList<String>();
		for(File f1: listofFiles){
			if (f1.isFile()){
				String nfile = f1.getName();
				nfile = nfile.substring(0, nfile.indexOf("."));
				if (!nfile.equals("PO")){
					DOMParser parser = new DOMParser();
					parser.parse(folderPath + "/" + f1.getName());
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					Document dom = db.parse(folderPath + "/" + f1.getName());
					NodeList list = dom.getElementsByTagName("order");
					Node node = list.item(0);
					Element e = (Element) node;
					String submitted = e.getAttribute("submitted");
					System.out.println(submitted);
					if (submitted.equals(startDate)){
						file.add(nfile+".xml");
					}
				}
			}
		}
		return file;
	}
	public ItemBean getItem(String itemNumber) throws Exception{
		return dao.getItembyID(itemNumber);
	}
}
