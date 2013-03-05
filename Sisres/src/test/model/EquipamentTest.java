package test.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import model.Equipament;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exception.PatrimonyException;

/**
 * Testa Patrimony, de modo indireto por causa da Heranca de Equipamento
 * */

public class EquipamentTest {

	Equipament eq;
	
	@Before
	public void setUp() throws PatrimonyException{
		eq = new Equipament("codigo", "descricao");
	}
	
	@After
	public void tearDown() throws PatrimonyException{
		eq = null;
	}
	
	@Test
	public void testInstance() throws PatrimonyException {
		assertTrue(eq instanceof Equipament);
	}
	
	@Test
	public void testNome() throws PatrimonyException {
		assertTrue("codigo diferente instanciado", "codigo" == eq.getCode());
	}
	
	@Test
	public void testDescricao() throws PatrimonyException {
		assertTrue("Descricao diferente instanciada", "descricao" == eq.getDescription());
	}
	
	@Test
	public void testEquals() throws PatrimonyException {
		Equipament eq2 = new Equipament("codigo", "descricao");
		assertTrue("Equipamentos deviam ser iguais", eq.equals(eq2));
	}
	
	@Test
	public void testEqualsCodigoDiferente() throws PatrimonyException {
		Equipament eq2 = new Equipament("codigo diferente", "descricao");
		assertFalse("Equipamentos deviam ser diferentes", eq.equals(eq2));
	}
	
	@Test
	public void testEqualsDescricaoDiferente() throws PatrimonyException {
		Equipament eq2 = new Equipament("codigo", "descricao diferente");
		assertFalse("Equipamentos deviam ser diferentes", eq.equals(eq2));
	}
	
	@Test(expected = PatrimonyException.class)
	public void testDescricaoVazia() throws PatrimonyException {
		new Equipament("abc", "");
	}
	
	@Test(expected = PatrimonyException.class)
	public void testCodigoVazio() throws PatrimonyException {
		new Equipament("", "abc");
	}
	
	@Test(expected = PatrimonyException.class)
	public void testCodigoNulo() throws PatrimonyException {
		new Equipament(null, "abc");
	}
	
	@Test(expected = PatrimonyException.class)
	public void testDescricaoNulo() throws PatrimonyException {
		new Equipament("abc", null);
	}

}
