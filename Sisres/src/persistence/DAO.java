package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import exception.ClientException;
import exception.PatrimonyException;
import exception.ReserveException;

public abstract class DAO {
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Vector find(String query) throws SQLException, ClientException, 
											   PatrimonyException, ReserveException{
		Vector vector = new Vector();
		Connection connection =  FactoryConnection.getInstance().getConnection();
		PreparedStatement prepareStatement = connection.prepareStatement(query);
		ResultSet resultSet= prepareStatement.executeQuery();
		
		while(resultSet.next()) {
			vector.add(this.fetch(resultSet));
		}
		
		prepareStatement.close();
		resultSet.close();
		connection.close();
		return vector;
	}
	
	protected boolean inDBGeneric(String query) throws SQLException{
		Connection connection = FactoryConnection.getInstance().getConnection();
		PreparedStatement prepareStatement = connection.prepareStatement(query);
		ResultSet resultSet = prepareStatement.executeQuery();
		
		if(!resultSet.next()) {
			resultSet.close();
			prepareStatement.close();
			connection.close();
			return false;
		}
		else {
			resultSet.close();
			prepareStatement.close();
			connection.close();
			return true;
		}
	}

	protected abstract Object fetch(ResultSet resultSet) throws SQLException, ClientException,
														        PatrimonyException, ReserveException;
	
	protected void executeQuery(String query) throws SQLException{
		Connection connection =  FactoryConnection.getInstance().getConnection();
		PreparedStatement prepareStatement = connection.prepareStatement(query);
		prepareStatement.executeUpdate();		
		prepareStatement.close();
		connection.close();
	}
	
	protected void updateQuery(String query) throws SQLException{
		Connection connection =  FactoryConnection.getInstance().getConnection();
		connection.setAutoCommit(false);
		PreparedStatement prepareStatement = connection.prepareStatement(query);
		prepareStatement.executeUpdate();
		connection.commit();
		prepareStatement.close();
		connection.close();
	}
}
