package org.country.italy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

	public static void main(String[] args) {
		
		String url = "jdbc:mysql://localhost:3306/db_nations";
		String username = "root";
		String password = "root";
		
		try(Connection con = DriverManager.getConnection(url, username, password)) {
			
			final String sql = "SELECT countries.name, countries.country_id, regions.name, continents.name "
					+ " FROM countries "
					+ "	JOIN regions "
					+ "		ON countries.region_id = regions.region_id "
					+ "	JOIN continents "
					+ "		ON regions.continent_id = continents.continent_id "
					+ " order by countries.name";
			
			PreparedStatement ps = con.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				
				final String countryName = rs.getString(1);
				final int countryId = rs.getInt(2);
				final String regionName = rs.getString(3);
				final String continentName = rs.getString(4);
				
				System.out.println(countryName + " | " + countryId + " - " + regionName + " - " + continentName);
			}
			
		} catch(SQLException ex) {
			System.err.println("Error: " + ex.getMessage());
		}
	}
}
