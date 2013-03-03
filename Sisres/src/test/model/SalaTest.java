package test.model;

import static org.junit.Assert.*;
import model.Sala;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import exception.PatrimonyException;

public class SalaTest {
	
	Sala sala;
	
	@Before
	public void setUp() throws PatrimonyException{
		sala = new Sala("codigo", "descricao", "1");
	}
	
	@After
	public void tearDown() throws PatrimonyException{
		sala = null;
	}
	
	@Test
    public void testInstance() throws PatrimonyException {
		assertTrue(new Sala("codigo", "descricao","1") instanceof Sala);
	}
	
	@Test
	public void testEquals() throws PatrimonyException {
		setUp();
		Sala sala_new = new Sala("codigo", "descricao", "1");
		assertTrue("Falha no Equals.", sala_new.equals(sala));
		sala_new = null;
		tearDown();
	}
	
	@Test
	public void testNotEqualsCapacidade() throws PatrimonyException {
		Sala s = new Sala("codigo", "descricao", "1");
		Sala s2 = new Sala("codigo", "descricao", "2");
		assertFalse("Falha no Equals.", s.equals(s2));

	}
	
	@Test
	public void testNotEqualsDescricao() throws PatrimonyException {
		setUp();
		Sala sala_new = new Sala("codigo", "d", "1");
		assertFalse("Falha no Equals.", sala.equals(sala_new));
		sala_new = null;
		tearDown();
	}
	
	@Test
	public void testNotEqualsCodigo() throws PatrimonyException {
		setUp();
		Sala sala_new = new Sala("c", "descricao", "1");
		assertFalse("Falha no Equals.", sala.equals(sala_new));
		sala_new = null;
		tearDown();
	}
	
	@Test
	public void testCodigo() throws PatrimonyException {
		setUp();
		assertEquals("codigo diferente instanciado", "codigo", sala.getCode());
		tearDown();
	}
	
	@Test
	public void testDescricao() throws PatrimonyException {
		setUp();
		assertEquals("Descricao diferente instanciada", "descricao", sala.getDescription());
		tearDown();
	}	
	
	@Test
	public void testCapacidade() throws PatrimonyException {
		setUp();
		assertEquals("Capacidade diferente instanciada", "1", sala.getCapacidade());
		tearDown();
	}
	
	@Test(expected = exception.PatrimonyException.class)
	public void testCapacidadeNegativo() throws PatrimonyException {
		setUp();
		sala.setCapacidade("-1");
		assertEquals("Capacidade diferente instanciada", "1", sala.getCapacidade());
		tearDown();
	}
	
	@Test(expected = exception.PatrimonyException.class)
	public void testCapacidadeLetra() throws PatrimonyException {
		setUp();
		sala.setCapacidade("a");
		assertEquals("Capacidade diferente instanciada", "1", sala.getCapacidade());
		tearDown();
	}
	
	@Test(expected = exception.PatrimonyException.class)
	public void testDescricaoVazia() throws PatrimonyException {
		new Sala("codigo", "", "1");
	}
	
	@Test(expected = exception.PatrimonyException.class)
	public void testCapacidadeVazia() throws PatrimonyException {
		new Sala("codigo", "descricao", "");
	}
	
	@Test(expected = exception.PatrimonyException.class)
	public void testCodigoVazio() throws PatrimonyException {
		new Sala("", "descricao","1");
	}
	
	@Test(expected = exception.PatrimonyException.class)
	public void testCodigoNulo() throws PatrimonyException {
		new Sala(null, "descricao", "1");
	}
	
	@Test(expected = exception.PatrimonyException.class)
	public void testDescricaoNulo() throws PatrimonyException {
		new Sala("codigo", null,"1");
	}
	
	@Test(expected = exception.PatrimonyException.class)
	public void testCapacidadeNulo() throws PatrimonyException {
		new Sala("codigo", "descricao", null);
	}
}
