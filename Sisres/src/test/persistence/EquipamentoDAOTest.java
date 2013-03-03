package test.persistence;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import model.Equipament;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import persistence.EquipamentDAO;
import exception.PatrimonyException;


public class EquipamentoDAOTest {
	static EquipamentDAO instance;
	Equipament antigo, novo;
	Vector <Equipament> todos;
	
	@BeforeClass
	public static void setUpClass() throws PatrimonyException, SQLException {
		instance = EquipamentDAO.getInstance();
	}
	
	@AfterClass
	public static void tearDownClass() throws SQLException, PatrimonyException {
		instance = null;
	}
	
	@Before
	public void setUp() throws PatrimonyException, SQLException {
		 antigo = new Equipament("codigo", "descricao - antigo");
		 novo = new Equipament("codigo", "descricao - alterada");
		 instance.include(antigo);
		 todos = instance.searchAllEquipaments();
	}
	
	@After
	public void tearDown() throws SQLException, PatrimonyException {
		todos = instance.searchAllEquipaments();
		Iterator<Equipament> i = todos.iterator();
		while(i.hasNext()){
			Equipament e = i.next();
			instance.delete(e);
		}
		antigo = null;
		novo = null;
	}
	
	@Test
	public void testInstance() {
		assertTrue("Instanciando EquipamentoDAO", instance instanceof EquipamentDAO);
	}
	
	@Test
	public void testSingleton() {
		EquipamentDAO inst1 = EquipamentDAO.getInstance();
		EquipamentDAO inst2 = EquipamentDAO.getInstance();
		assertSame("Testando o Padrao Singleton", inst2, inst1);
	}
	
	@Test
	public void testIncluir() throws PatrimonyException, SQLException {
		assertNotNull("Equipamento nao foi incluido", procurarNoVetor(antigo));
	}
	@Test
	public void testBuscarTodos() throws SQLException, PatrimonyException {
		assertNotNull("Testando a busca de elementos no BD.", todos);
	}
	
	@Test
	public void testBuscarPorCodigo() throws SQLException, PatrimonyException {
		assertNotNull("Testando a busca por codigo de elementos no BD.", instance.searchCode(antigo.getCode()));
	}
	
	@Test
	public void testBuscarPorDescricao() throws SQLException, PatrimonyException {
		assertNotNull("Testando a busca por descricao de elementos no BD.", instance.searchDescription(antigo.getDescription()));
	}
	
	@Test
	public void testBuscarPorCodigoNull() throws SQLException, PatrimonyException {
		assertTrue("Testando a busca por codigo nulo de elementos no BD.", instance.searchCode(null).isEmpty());
	}
	
	@Test
	public void testBuscarPorDescricaoNull() throws SQLException, PatrimonyException {
		assertTrue("Testando a busca por descricao nula de elementos no BD.", instance.searchDescription(null).isEmpty());
	}
	
	@Test
	public void testAlterar() throws PatrimonyException, SQLException {
		instance.alterate(antigo, novo);
		Equipament e = procurarNoVetor(antigo);
		assertNull("Equipamento nao foi alterado", e);
		assertNotNull("Equipamento nao foi alterado", procurarNoVetor(novo));
	}
	
	@Test (expected= PatrimonyException.class)
	public void testIncluirComCodigoExistente() throws PatrimonyException, SQLException {
		instance.include(antigo);
	}
	
	@Test (expected= PatrimonyException.class)
	public void testIncluirNulo() throws PatrimonyException, SQLException {
		instance.include(null);
	}
	
	@Test (expected= PatrimonyException.class)
	public void testAlterarNull() throws PatrimonyException, SQLException {
		instance.alterate(null, null);
	}
	
	@Test (expected= PatrimonyException.class)
	public void testAlterarSegundoNull() throws PatrimonyException, SQLException {
		instance.alterate(antigo, null);
	}
	
	
	@Test (expected= PatrimonyException.class)
	public void testAlterarNaoExistente() throws PatrimonyException, SQLException {
		Equipament equip = new Equipament("codigo", "eqpt nao existente");
		Equipament equipAlter = new Equipament("codigo", "eqpt nao existente alteraddo");
		instance.alterate(equip, equipAlter);
	}
	
	@Test (expected= PatrimonyException.class)
	public void testAlterarIgual() throws PatrimonyException, SQLException {
		instance.alterate(novo, novo);
	}
	
	@Test (expected= PatrimonyException.class)
	public void testAlterarParaOutroEquipamento() throws PatrimonyException, SQLException {
		Equipament e = new Equipament("novo", "teste Alterar para outro");
		instance.include(e);
		instance.alterate(e, novo);
	}
	
	@Test (expected= PatrimonyException.class)
	public void testExcluirNull() throws PatrimonyException, SQLException {
		instance.delete(null);
	}
	
	@Test (expected= PatrimonyException.class)
	public void testExcluirNaoExistente() throws PatrimonyException, SQLException {
		Equipament eq = new Equipament("codigo"," nao existe descricao");
		instance.delete(eq);
	}
	
	@Test
	public void testExcluirExistente() throws PatrimonyException, SQLException {
		Equipament novoExclusao = new Equipament("cdg", "teste exclusao");
		instance.include(novoExclusao);
		instance.delete(novoExclusao);
		assertNull("Equipamento nao foi alterado", procurarNoVetor(novoExclusao));
	}
	
	public Equipament procurarNoVetor(Equipament teste) throws PatrimonyException, SQLException {
		todos = instance.searchAllEquipaments();
		Iterator<Equipament> i = todos.iterator();
		while(i.hasNext()){
			Equipament e = i.next();
			if(e.equals(teste))
				return e;			
		}
		return null;
	}
	
}
