package test.control;

import control.EquipamentController;
import exception.PatrimonyException;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;
import model.Equipament;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EquipamentControllerTest {

	static EquipamentController instance;
	Vector<Equipament> todos;
	Equipament e;
 
	public EquipamentControllerTest() {
	}

	@BeforeClass
	public static void setUpClass() throws PatrimonyException {
		instance = EquipamentController.getInstance();
	}

	@AfterClass
	public static void tearDownClass() {
		instance = null;
	}

	@Before
	public void setUp() throws Exception {
		e = new Equipament("codigo", "descricao");
		instance.insert("codigo","descricao");
		todos = instance.getEquipamentVector();
	}

	@After
	public void tearDown() throws SQLException, PatrimonyException {
		todos = instance.getEquipamentVector();
		Iterator<Equipament> i = todos.iterator();
		while(i.hasNext()){
			e = i.next();
			instance.delete(e);
		}
		e = null;
	}
	
	@Test
	public void testGetEquipamento_vet() throws Exception {
		assertNotNull(todos);
	}
	
	@Test
	public void testGetInstance() {
		assertNotNull("Get Instance falhou",instance);
	}
	
	@Test
	public void testSingleton(){
		EquipamentController me = EquipamentController.getInstance();
		assertSame("Instancias diferentes", me, instance);
		
	}

	@Test
	public void testIncluirVet() throws SQLException, PatrimonyException {
		assertNotNull("Teste de Inclusao no Equipamento Vet.", procurarNoVetor(e));
	}
	
	@Test
	public void testAlterarVet() throws SQLException, PatrimonyException {
		instance.alterate("codigo alterado", "descricao alterarda", e);
		Equipament e2 = new Equipament("codigo alterado", "descricao alterarda");
		assertNotNull("Teste de Inclusao no Equipamento Vet.", procurarNoVetor(e2));
	}
	
	@Test(expected = PatrimonyException.class)
	public void testAlterarNaoExistente() throws SQLException, PatrimonyException {
		Equipament eq = new Equipament("codigo", "nao existe");
		instance.alterate("codigo alterado", "descricao alterarda", eq);
	}
	
	@Test(expected = PatrimonyException.class)
	public void testAlterarNull() throws SQLException, PatrimonyException {
		instance.alterate("codigo alterado", "descricao alterarda", null);
	}
	
	@Test (expected = PatrimonyException.class)
	public void testExcluirNull() throws SQLException, PatrimonyException {
		e = null;
		instance.delete(e);
	}
	
	public Equipament procurarNoVetor(Equipament teste) throws PatrimonyException, SQLException {
		todos = instance.getEquipamentVector();
		Iterator<Equipament> i = todos.iterator();
		while(i.hasNext()){
			Equipament e = i.next();
			if(e.equals(teste))
				return e;			
		}
		return null;
	}
	
}
