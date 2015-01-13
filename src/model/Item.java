package model;

import java.sql.SQLException;
import java.util.List;

import model.bean.ItemBean;
import model.dao.ItemDAO;

public class Item {

	private ItemDAO dao;
	
	public Item() throws SQLException{
		dao = new ItemDAO();
	}
	
	public List<ItemBean> getItemsByCategoryName(int categoryID) throws SQLException {
		return dao.getItemsByCategory(categoryID);
	}

	public List<ItemBean> getAllItems() throws SQLException {
		return dao.getAllItems();
	}

	public model.bean.ItemBean getItem(String itemNumber) throws SQLException {
		return dao.getItembyID(itemNumber);
	}
	public List<ItemBean> getItembyName(String itemName) throws SQLException {
		return dao.getItemByName(itemName);
	}
}
