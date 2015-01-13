package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import model.bean.CategoryBean;

import org.apache.tomcat.jdbc.pool.DataSource;

public class CategoryDAO {
	
	private DataSource ds;
	
	public CategoryDAO() throws SQLException {
		try {
			ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/EECS");
		} catch (NamingException e) {
			throw new SQLException("Could not initialize database connection", e);
		}
	}

	public List<CategoryBean> getCategories() throws SQLException {
		Connection con = this.ds.getConnection();
		PreparedStatement p = con
				.prepareStatement("select name, id from roumani.category");
		ResultSet r = p.executeQuery();
		List<CategoryBean> l = new ArrayList<CategoryBean>();
		
		while (r.next()){
			String name = r.getString("name");
			int id = r.getInt("id");
			l.add(new CategoryBean(name, id));
		}
		
		r.close();
		p.close();
		con.close();
		return l;
	}
	public int getCategoryID(String categoryName) throws SQLException{
		Connection con = this.ds.getConnection();
		// do a exact match with ignore case comparison of the category name
		PreparedStatement p = con
				.prepareStatement("select id from roumani.category where upper(name) = ?");
		p.setString(1, categoryName.toUpperCase());
		ResultSet r = p.executeQuery();
		int id;
		
		r.next();
		id = r.getInt(1);
		
		r.close();
		p.close();
		con.close();
		return id;
	}
	
	public boolean hasCategory(String categoryName) throws SQLException{
		Connection con = this.ds.getConnection();
		// do a exact match with ignore case comparison of the category name
		PreparedStatement p = con
				.prepareStatement("select count(name) from roumani.category where upper(name) = ?");
		p.setString(1, categoryName.toUpperCase());
		ResultSet r = p.executeQuery();
		boolean hasCategory;
		
		r.next();
		hasCategory = r.getInt(1) == 1;
		
		r.close();
		p.close();
		con.close();
		return hasCategory;
	}

}
