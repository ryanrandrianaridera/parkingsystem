package com.parkit.parkingsystem.config;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class connect to database and close them.
 *
 * @author Ryan RANDRIA
 * @version 1.0
 */

public class DataBaseConfig {

	/**
	 * Databaseconfig logger.
	 */
	private static final Logger logger = LogManager.getLogger("DataBaseConfig");
	/**
	 * Url of DB prod with Timezone UTC.
	 */
	private static String urlprod = "jdbc:mysql://localhost:3306/prod?useSSL=false&serverTimezone=UTC";
	/**
	 * Username prod of DB.
	 */
	private static String usrprod = "root";
	/**
	 * Password prod od DB prod of DB.
	 */
	private static String pwdprod = "rootroot";

	/**
	 * Connect to sql as root.
	 *
	 * @return connection to database
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */

	public Connection getConnection() throws ClassNotFoundException, SQLException, IOException {
		logger.info("Create DB connection");
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection(urlprod, usrprod, pwdprod);
	}

	/**
	 * Close database connection.
	 *
	 * @param con of DB connection
	 */

	public void closeConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
				logger.info("Closing DB connection");
			} catch (SQLException e) {
				logger.error("Error while closing connection", e);
			}
		}
	}

	/**
	 * Close prepared statement.
	 *
	 * @param ps as an instance of PreparedStatement to be closed
	 */

	public void closePreparedStatement(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
				logger.info("Closing Prepared Statement");
			} catch (SQLException e) {
				logger.error("Error while closing prepared statement", e);
			}
		}
	}

	/**
	 * Close resultset.
	 *
	 * @param rs as an instance of resultset to be closed
	 */

	public void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
				logger.info("Closing Result Set");
			} catch (SQLException e) {
				logger.error("Error while closing result set", e);
			}
		}
	}
}