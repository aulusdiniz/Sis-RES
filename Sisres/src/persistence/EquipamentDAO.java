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

    public void alterate(Equipament equipament_old, Equipament equipament_new) throws SQLException, PatrimonyException {
        if (equipament_old == null) {
            throw new PatrimonyException(EQUIPAMENT_NULL);
        }
        if (equipament_new == null) {
            throw new PatrimonyException(EQUIPAMENT_NULL);
        }

        Connection connection = FactoryConnection.getInstance().getConnection();
        PreparedStatement prepare_statement;

        if (!this.inDB(equipament_old)) { 
            throw new PatrimonyException(EQUIPAMENT_NOT_EXISTING);
        }
        else
        	if (this.inOtherDB(equipament_old)) {
        		throw new PatrimonyException(EQUIPAMENT_RESERVED);
        	}
        	else
        		if (!equipament_new.getCode().equals(equipament_old.getCode()) && this.inDBCode(equipament_new.getCode())) {
        			throw new PatrimonyException(CODE_EXISTING);
        		}
        		else
        			if (!this.inDB(equipament_new)) {
        				String msg = "UPDATE equipamento SET " + "codigo = \"" + equipament_new.getCode() + "\", " + "descricao = \""
        						+ equipament_new.getDescription() + "\"" + " WHERE " + "equipamento.codigo = \"" + equipament_old.getCode()
        						+ "\" and " + "equipamento.descricao = \"" + equipament_old.getDescription() + "\";";

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
        
    	Vector<Equipament> vector_equipament = new Vector<Equipament>();
        Connection connection = FactoryConnection.getInstance().getConnection();

        PreparedStatement prepare_statement = connection.prepareStatement(query);
        ResultSet result_set = prepare_statement.executeQuery();

        while (result_set.next()) {
            vector_equipament.add(this.fetchEquipament(result_set));
        }

        prepare_statement.close();
        result_set.close();
        connection.close();
        
        return vector_equipament;
    }

    private boolean inDBGeneric(String query) throws SQLException {
        Connection connection = FactoryConnection.getInstance().getConnection();
        PreparedStatement prepare_statement = connection.prepareStatement(query);
        ResultSet result_set = prepare_statement.executeQuery();

        if (!result_set.next()) {
            result_set.close();
            prepare_statement.close();
            connection.close();
            
            return false;
        }
        else {
            result_set.close();
            prepare_statement.close();
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

    private Equipament fetchEquipament(ResultSet result_set) throws PatrimonyException, SQLException {
        return new Equipament(result_set.getString("codigo"), result_set.getString("descricao"));
    }

    private void updateQuery(String msg) throws SQLException {
        Connection connection = FactoryConnection.getInstance().getConnection();
        PreparedStatement prepare_statement = connection.prepareStatement(msg);
        prepare_statement.executeUpdate();
        prepare_statement.close();
        connection.close();
    }
}
