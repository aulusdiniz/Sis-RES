package persistence;

import model.Sala;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import exception.PatrimonyException;

public class SalaDAO {

	//Mensagens
		private static final String SALA_JA_EXISTENTE = "Sala ja cadastrada.";
		private static final String SALA_NAO_EXISTENTE = "Sala nao cadastrada.";
		private static final String SALA_EM_USO = "Sala esta sendo utilizada em uma reserva.";
		private static final String SALA_NULA = "Sala esta nula.";
		private static final String CODIGO_JA_EXISTENTE = "Sala com o mesmo codigo ja cadastrada.";
	
	//Singleton
		private static SalaDAO instance;
		private SalaDAO(){
		}
		public static SalaDAO getInstance(){
			if(instance == null)
				instance = new SalaDAO();
			return instance;
		}
	//

		
	public void incluir(Sala sala) throws SQLException, PatrimonyException {	
		if(sala == null)
			throw new PatrimonyException(SALA_NULA);
		else if(this.inDBCodigo(sala.getCode()))
			throw new PatrimonyException(CODIGO_JA_EXISTENTE);
		this.updateQuery("INSERT INTO " +
					"sala (codigo, descricao, capacidade) VALUES (" +
					"\"" + sala.getCode() + "\", " +
					"\"" + sala.getDescription() + "\", " +
					sala.getCapacidade() + ");");
	}

	public void alterar(Sala old_sala, Sala new_sala) throws SQLException, PatrimonyException {
		if(new_sala == null)
			throw new PatrimonyException(SALA_NULA);
		if(old_sala == null)
			throw new PatrimonyException(SALA_NULA);
		
		Connection con = FactoryConnection.getInstance().getConnection();
		PreparedStatement pst;
		
		if(!this.inDB(old_sala))
			throw new PatrimonyException(SALA_NAO_EXISTENTE);
		else if(this.inOtherDB(old_sala))
			throw new PatrimonyException(SALA_EM_USO);
		else if(!old_sala.getCode().equals(new_sala.getCode()) && this.inDBCodigo(new_sala.getCode()))
			throw new PatrimonyException(CODIGO_JA_EXISTENTE);
		if(!this.inDB(new_sala)){
			String msg = "UPDATE sala SET " +				
				"codigo = \"" + new_sala.getCode() + "\", " +
				"descricao = \"" + new_sala.getDescription() + "\", " +
				"capacidade = " + new_sala.getCapacidade() +
				" WHERE " +
				"sala.codigo = \"" + old_sala.getCode() + "\" and " +
				"sala.descricao = \"" + old_sala.getDescription() +  "\" and " +
				"sala.capacidade = " + old_sala.getCapacidade() +";";
			con.setAutoCommit(false);
			pst = con.prepareStatement(msg);
			pst.executeUpdate();
			con.commit();
		}
		else
			throw new PatrimonyException(SALA_JA_EXISTENTE);
		
		pst.close();
		con.close();
	}

	public void excluir(Sala sala) throws SQLException, PatrimonyException {
		if(sala == null)
			throw new PatrimonyException(SALA_NULA);
		else if(this.inOtherDB(sala))
			throw new PatrimonyException(SALA_EM_USO);
		else if(this.inDB(sala)){
			this.updateQuery("DELETE FROM sala WHERE " +
				"sala.codigo = \"" + sala.getCode() + "\" and " +
				"sala.descricao = \"" + sala.getDescription() +  "\" and " +
				"sala.capacidade = " + sala.getCapacidade() + ";"				
				);
		}
		else
			throw new PatrimonyException(SALA_NAO_EXISTENTE);
	}

	
	
	public Vector<Sala> buscarTodos() throws SQLException, PatrimonyException {
		return this.buscar("SELECT * FROM sala;");
	}
	public Vector<Sala> buscarPorCodigo(String valor) throws SQLException, PatrimonyException {
		return this.buscar("SELECT * FROM sala WHERE codigo = " + "\"" + valor + "\";");
	}
	public Vector<Sala> buscarPorDescricao(String valor) throws SQLException, PatrimonyException {
		return this.buscar("SELECT * FROM sala WHERE descricao = " + "\"" + valor + "\";");
	}
	public Vector<Sala> buscarPorCapacidade(String valor) throws SQLException, PatrimonyException {
		return this.buscar("SELECT * FROM sala WHERE capacidade = " + valor + ";");
	}
	
	
	/**
	 * Metodos Privados
	 * */
	
	private Vector<Sala> buscar(String query) throws SQLException, PatrimonyException {
		Vector<Sala> vet = new Vector<Sala>();
		
		Connection con =  FactoryConnection.getInstance().getConnection();
		
		PreparedStatement pst = con.prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		
		while(rs.next())
			vet.add(this.fetchSala(rs));
		
		pst.close();
		rs.close();
		con.close();
		return vet;
	}
	
	
	private boolean inDBGeneric(String query) throws SQLException{
		Connection con = FactoryConnection.getInstance().getConnection();
		PreparedStatement pst = con.prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		
		if(!rs.next())
		{
			rs.close();
			pst.close();
			con.close();
			return false;
		}
		else {
			rs.close();
			pst.close();
			con.close();
			return true;
		}
	}
	private boolean inDB(Sala sala) throws SQLException{
		return this.inDBGeneric("SELECT * FROM sala WHERE " +
				"sala.codigo = \"" + sala.getCode() + "\" and " +
				"sala.descricao = \"" + sala.getDescription() + "\" and " +
				"sala.capacidade = " + sala.getCapacidade() +
				";");
	}
	private boolean inDBCodigo(String codigo) throws SQLException{
		return this.inDBGeneric("SELECT * FROM sala WHERE " +
				"sala.codigo = \"" + codigo + "\";");
	}
	private boolean inOtherDB(Sala sala) throws SQLException{
		if( this.inDBGeneric("SELECT * FROM reserva_sala_professor WHERE " +
				"id_sala = (SELECT id_sala FROM sala WHERE " +
				"sala.codigo = \"" + sala.getCode() + "\" and " +
				"sala.descricao = \"" + sala.getDescription() +  "\" and " +
				"sala.capacidade = " + sala.getCapacidade() +" );") == false)
		{
			if(this.inDBGeneric("SELECT * FROM reserva_sala_aluno WHERE " +
							"id_sala = (SELECT id_sala FROM sala WHERE " +
							"sala.codigo = \"" + sala.getCode() + "\" and " +
							"sala.descricao = \"" + sala.getDescription() +  "\" and " +
							"sala.capacidade = " + sala.getCapacidade() +" );") == false)
			{
				return false;
			}
		}
		
		return true;
	}
	
	
	private Sala fetchSala(ResultSet rs) throws PatrimonyException, SQLException{
		return new Sala(rs.getString("codigo"), rs.getString("descricao"), rs.getString("capacidade"));
	}
	
	private void updateQuery(String msg) throws SQLException{
		Connection con =  FactoryConnection.getInstance().getConnection();
		PreparedStatement pst = con.prepareStatement(msg);
		pst.executeUpdate();		
		pst.close();
		con.close();
	}

}
