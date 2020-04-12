package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FoodDao {

	public List<Food> listAllFood(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_id"),
							res.getInt("food_code"),
							res.getString("display_name"), 
							res.getInt("portion_default"), 
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"),
							res.getDouble("calories")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

	}
	
	public Map<Integer, Condiment> listAllCondiment(Double calorie, Map <Integer, Condiment> idMapC){
		String sql = "SELECT * FROM condiment WHERE condiment.condiment_calories<?" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			Map <Integer ,Condiment> mappa = new HashMap<Integer, Condiment>() ;
			st.setDouble(1, calorie);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					if (!idMapC.containsKey(res.getInt("condiment_id"))) {
					mappa.put(res.getInt("condiment_id"),new Condiment(res.getInt("condiment_id"),
							res.getInt("food_code"),
							res.getString("display_name"), 
							res.getString("condiment_portion_size"), 
							res.getDouble("condiment_calories")
							));}
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return mappa ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

	}

	public int getFC(Integer condiment_id, Integer condiment_id2, Map <Integer, Condiment> idMapC) {
		String sql = "SELECT DISTINCT  COUNT(*) AS c " + 
				"FROM food_condiment AS fc1 , food_condiment AS fc2 " + 
				"WHERE  fc1.food_code=fc2.food_code AND fc1.condiment_food_code=? AND  fc2.condiment_food_code=? " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			Map <Integer ,Condiment> mappa = new HashMap<Integer, Condiment>() ;
			st.setInt(1, condiment_id);
			st.setInt(2, condiment_id2);
			ResultSet res = st.executeQuery() ;
			
			if(res.next()) {
				try {
	              int c	= res.getInt("c");
	              conn.close();
			      return c ;
					}
				 catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0 ;
		}
		return 0;
	}

	public int getC (int id) {
		String sql = "SELECT  COUNT(*) AS c " + 
				"FROM  food_condiment " + 
				"WHERE condiment_food_code =?" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, id);
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			if(res.next()) {
				try {
					return res.getInt("c");
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return 0 ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0 ;
		}
	}
	
	
}

