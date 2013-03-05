package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import model.Equipament;
import exception.PatrimonyException;

public class EquipamentDAO {

    private static final String EQUIPAMENT_EXISTING = "Equipamento ja cadastrado.";
    private static final String EQUIPAMENT_NOT_EXISTING = "Equipamento nao cadastrado.";
    private static final String EQUIPAMENT_NULL = "Equipamento esta nulo.";
    private static final String EQUIPAMENT_RESERVED = "Equipamento esta sendo utilizado em uma reserva.";
    private static final String CODE_EXISTING = "Equipamento com o mesmo codigo ja cadastrado.";

    
    private static EquipamentDAO instance;

    private EquipamentDAO() {
        //nothing
    }

    public static EquipamentDAO getInstance() {
        if (instance == null) {
            instance = new EquipamentDAO();
        }
        
        return instance;
    }

    
    public void include(Equipament equipament) throws SQLException, PatrimonyException {
        if (equipament == null) {
            throw new PatrimonyException(EQUIPAMENT_NULL);
        }
        else
        	if (this.inDBCode(equipament.getCode())) {
        		throw new PatrimonyException(CODE_EXISTING);
        	}
        	else
        		if (!this.inDB(equipament)) {
        			this.updateQuery("INSERT INTO " + "equipamento (codigo, descricao) VALUES (" +
        			"\"" + equipament.getCode() + "\", " + "\"" + equipament.getDescription() + "\");");
        		}
    }

    public void alterate(Equipament equipamentOld, Equipament equipamentNew) throws SQLException, PatrimonyException {
        if (equipamentOld == null) {
            throw new PatrimonyException(EQUIPAMENT_NULL);
        }
        else{
        	//nothing
        }
        
        if (equipamentNew == null) {
            throw new PatrimonyException(EQUIPAMENT_NULL);
        }
        else{
        	//nothing
        }

        Connection connection = FactoryConnection.getInstance().getConnection();
        PreparedStatement prepare_statement;

        if (!this.inDB(equipamentOld)) { 
            throw new PatrimonyException(EQUIPAMENT_NOT_EXISTING);
        }
        else
        	if (this.inOtherDB(equipamentOld)) {
        		throw new PatrimonyException(EQUIPAMENT_RESERVED);
        	}
        	else
        		if (!equipamentNew.getCode().equals(equipamentOld.getCode()) && this.inDBCode(equipamentNew.getCode())) {
        			throw new PatrimonyException(CODE_EXISTING);
        		}
        		else
        			if (!this.inDB(equipamentNew)) {
        				String msg = "UPDATE equipamento SET " + "codigo = \"" + equipamentNew.getCode() + "\", " + "descricao = \""
        						+ equipamentNew.getDescription() + "\"" + " WHERE " + "equipamento.codigo = \"" + equipamentOld.getCode()
        						+ "\" and " + "equipamento.descricao = \"" + equipamentOld.getDescription() + "\";";

            connection.setAutoCommit(false);
            prepare_statement = connection.prepareStatement(msg);
            prepare_statement.executeUpdate();
            connection.commit();

            prepare_statement.close();

        			}
        			else
        				throw new PatrimonyException(EQUIPAMENT_EXISTING);
        connection.close();
    }

    public void delete(Equipament equipament) throws SQLException, PatrimonyException {
        if (equipament == null) {
            throw new PatrimonyException(EQUIPAMENT_NULL);
        }
        else
        	if (this.inOtherDB(equipament)) {
        		throw new PatrimonyException(EQUIPAMENT_RESERVED);
        	}
        	
        if (this.inDB(equipament)) {
        		this.updateQuery("DELETE FROM equipamento WHERE " + "equipamento.codigo = \"" + equipament.getCode() + "\" and "
        				+ "equipamento.descricao = \"" + equipament.getDescription() + "\";");
        }
        else
            throw new PatrimonyException(EQUIPAMENT_NOT_EXISTING);
    }

    public Vector<Equipament> searchAllEquipaments() throws SQLException, PatrimonyException {
        return this.search("SELECT * FROM equipamento;");
    }

    public Vector<Equipament> searchCode(String value) throws SQLException, PatrimonyException {
        return this.search("SELECT * FROM equipamento WHERE codigo = " + "\"" + value + "\";");
    }

    public Vector<Equipament> searchDescription(String value) throws SQLException, PatrimonyException {
        return this.search("SELECT * FROM equipamento WHERE descricao = " + "\"" + value + "\";");
    }

    /**
     * Metodos Privados
     * */

    private Vector<Equipament> search(String query) throws SQLException, PatrimonyException {
        
    	Vector<Equipament> vectorEquipament = new Vector<Equipament>();
        Connection connection = FactoryConnection.getInstance().getConnection();

        PreparedStatement prepareStatement = connection.prepareStatement(query);
        ResultSet resultSet = prepareStatement.executeQuery();

        while (resultSet.next()) {
            vectorEquipament.add(this.fetchEquipament(resultSet));
        }

        prepareStatement.close();
        resultSet.close();
        connection.close();
        
        return vectorEquipament;
    }

    private boolean inDBGeneric(String query) throws SQLException {
        Connection connection = FactoryConnection.getInstance().getConnection();
        PreparedStatement prepareStatement = connection.prepareStatement(query);
        ResultSet resultSet = prepareStatement.executeQuery();

        if (!resultSet.next()) {
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

    private boolean inDB(Equipament equipament) throws SQLException, PatrimonyException {
        return this.inDBGeneric("SELECT * FROM equipamento WHERE " + "equipamento.codigo = \"" + equipament.getCode() + "\" and "
                + "equipamento.descricao = \"" + equipament.getDescription() + "\";");
    }

    private boolean inDBCode(String code) throws SQLException {
        return this.inDBGeneric("SELECT * FROM equipamento WHERE " + "codigo = \"" + code + "\";");
    }

    private boolean inOtherDB(Equipament equipament) throws SQLException {
        return this.inDBGeneric("SELECT * FROM reserva_equipamento WHERE "
                + "id_equipamento = (SELECT id_equipamento FROM equipamento WHERE " + "equipamento.codigo = \"" + equipament.getCode()
                + "\" and " + "equipamento.descricao = \"" + equipament.getDescription() + "\");");
    }

    private Equipament fetchEquipament(ResultSet resultSet) throws PatrimonyException, SQLException {
        return new Equipament(resultSet.getString("codigo"), resultSet.getString("descricao"));
    }

    private void updateQuery(String msg) throws SQLException {
        Connection connection = FactoryConnection.getInstance().getConnection();
        PreparedStatement prepareStatement = connection.prepareStatement(msg);
        prepareStatement.executeUpdate();
        prepareStatement.close();
        connection.close();
    }
}
