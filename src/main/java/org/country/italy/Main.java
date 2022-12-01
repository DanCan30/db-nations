package org.country.italy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		String url = "jdbc:mysql://localhost:3306/db_nations";
		String username = "root";
		String password = "root";
		
		System.out.println("Type a nation: ");
		String userInput = sc.nextLine();
		
		try(Connection con = DriverManager.getConnection(url, username, password)) {
			
			final String userQuery = "SELECT countries.country_id, countries.name "
								   + " FROM countries "
								   + "WHERE countries.name LIKE ? ";
			
			PreparedStatement psq = con.prepareStatement(userQuery);
			
			psq.setString(1, "%" + userInput + "%");
			
			ResultSet res = psq.executeQuery();
			
			while(res.next()) {
				final int countryId = res.getInt(1);
				final String countryName = res.getString(2);
				
				System.out.println("ID " + countryId + " - " + countryName);
			}
			
			System.out.println("Type and ID for an advanced search: ");
			int advSearchInput = sc.nextInt();
			
			final String languageSearchQuery = "select countries.name, languages.`language`"
										+ " from countries "
										+ "	join country_languages "
										+ "		on country_languages.country_id = countries.country_id "
										+ "	join languages "
										+ "		on country_languages.language_id = languages.language_id "
										+ " where countries.country_id = ? ";
			
			final String statsSearchQuery = "select country_stats.`year` , country_stats.population , country_stats.gdp  "
					+ "from countries "
					+ "	join country_languages "
					+ "		on country_languages.country_id = countries.country_id  "
					+ "	join languages "
					+ "		on country_languages.language_id = languages.language_id "
					+ "  join country_stats "
					+ " 		on country_stats.country_id = countries.country_id  "
					+ "where countries.country_id = ? "
					+ " order by country_stats.`year` desc "
					+ " limit 1";
			
			PreparedStatement psLang = con.prepareStatement(languageSearchQuery);
			PreparedStatement psStats = con.prepareStatement(statsSearchQuery);
			
			psLang.setInt(1, advSearchInput);
			psStats.setInt(1, advSearchInput);
			
			ResultSet languageSearchRes = psLang.executeQuery();
			
			ResultSet statsSearchRes = psStats.executeQuery();

			Set<String> languages = new HashSet<>(); 

			String countryName = "";
			
			while ( languageSearchRes.next() ) {

				countryName = languageSearchRes.getString(1);
				final String countryLanguage = languageSearchRes.getString(2);
				
				languages.add(countryLanguage);
			}
			int year = 0;
			int population = 0;
			String gdp = "";
			
			while ( statsSearchRes.next() ) {
				year = statsSearchRes.getInt(1);
				population = statsSearchRes.getInt(2);
				gdp = statsSearchRes.getString(3);
			}
			
			System.out.println("Country selected: " + countryName + ""
								+ "\nLanguages Spoken: ");
			
			for(String lang : languages) {
				System.out.println(lang);
			}
			
			System.out.println("Most recent stats from the year " + year + ": "
								+ "\nPopulation: " + population + ""
								+ "\nGDP: " + gdp);
			
			
			//			final String sql = "SELECT countries.name, countries.country_id, regions.name, continents.name "
//					+ " FROM countries "
//					+ "	JOIN regions "
//					+ "		ON countries.region_id = regions.region_id "
//					+ "	JOIN continents "
//					+ "		ON regions.continent_id = continents.continent_id "
//					+ " order by countries.name";
			
//			PreparedStatement ps = con.prepareStatement(sql);
			
//			ResultSet rs = ps.executeQuery();
			
//			while(rs.next()) {
//				
//				final String countryName = rs.getString(1);
//				final int countryId = rs.getInt(2);
//				final String regionName = rs.getString(3);
//				final String continentName = rs.getString(4);
//				
//				System.out.println(countryName + " | " + countryId + " - " + regionName + " - " + continentName);
//			}
			
		} catch(SQLException ex) {
			System.err.println("Error: " + ex.getMessage());
		}
	}
}
