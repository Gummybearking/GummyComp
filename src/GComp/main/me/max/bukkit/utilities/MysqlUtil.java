package GComp.main.me.max.bukkit.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import GComp.main.me.max.bukkit.CompMain;



public class MysqlUtil {
	private Connection con;
	private String ip;
	private String port;
	private String url;
	private String user;
	private String pass;
	private Logger log;
	private String prefix = "[GummyComp] ";

	private String driver = "com.mysql.jdbc.Driver"; 
	
	public static CompMain main;
	public static PermissionHandler permission;
	
	
	public MysqlUtil(CompMain cm){
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		log            = cm.getLogger();
		main           = cm;
		permission     = new PermissionHandler(cm);
		ip             = cm.getConfig().getString("MySQL-Database.ip");
		port           = cm.getConfig().getString("MySQL-Database.port");
		user           = cm.getConfig().getString("MySQL-Database.username");
		pass           = cm.getConfig().getString("MySQL-Database.password");

		url            = "jdbc:mysql://" 
							+ ip 
							+ ":" 
							+ port 
							+ "/";
		
	}
	
	public void executeUpdate(String update) {
		
		Statement stmt = null;
		log.log(Level.INFO, prefix + "Connecting to database:"
				+ "\nUsername: "   + user
				+ "\nssword: "     + pass
				+ "\nOn Url: "     + url );
		
		try {
			con = DriverManager.getConnection(url, user, pass);
			log.log(Level.INFO, prefix + "Connected Successfully");
			stmt = con.createStatement();
			stmt.executeUpdate(update);
			log.log(Level.INFO, prefix + "Executed update");
		} catch (SQLException e) {
			log.log(Level.SEVERE, prefix + "Failed to execute: " + update);
			e.printStackTrace();
		}finally{
			try {
				if(con!=null) {
					con.close();
				}
				if(stmt!=null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
	}
}
