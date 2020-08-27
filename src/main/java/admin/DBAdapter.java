package admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBAdapter {
	private static Connection connection;
	private final String dbpwd;
	
	public DBAdapter(String dbpwd) {
		this.dbpwd = dbpwd;
	}

	private Connection createConnection() {
		try { 
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection("jdbc:postgresql://tuffi.db.elephantsql.com:5432/qpqctchb", "qpqctchb", dbpwd);
        } catch (SQLException | ClassNotFoundException e) {
        	e.printStackTrace();
		}
		return null;
	}
	
	private Connection getConnection() {
		if(connection == null) {
			connection = createConnection();
		}
		return connection;
	}

	public void addServer(String discordId, int orgId, String ownerName, String ownerICName)
			throws SQLException {
		String query = "INSERT INTO serverlist VALUES ('" + discordId + "', " + orgId + ", '" + ownerName + "', '"
				+ ownerICName + "')";
		PreparedStatement statement = getConnection().prepareStatement(query);
		statement.execute();
	}

	public void removeServer(String discordId) throws SQLException {
		String query = "DELETE FROM serverlist WHERE discordId = '" + discordId + "'";
		PreparedStatement statement = getConnection().prepareStatement(query);
		statement.execute();
	}

	public boolean orgExists(int orgId) {
		String query = "SELECT * FROM serverlist WHERE orgId = '" + orgId + "'";
		try {
			PreparedStatement statement = getConnection().prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Hubo un error al obtener el servidor de la ORG " + orgId);
			e.printStackTrace();
		}
		return false;
	}

	public ResultSet getServer(String discordId) {
		String query = "SELECT * FROM serverlist WHERE discordId = '" + discordId + "'";
		try {
			PreparedStatement statement = getConnection().prepareStatement(query);
			return statement.executeQuery();
		} catch (SQLException e) {
			System.out.println("Hubo un error al obtener el servidor '" + discordId + "'");
			e.printStackTrace();
		}
		return null;
	}
}
