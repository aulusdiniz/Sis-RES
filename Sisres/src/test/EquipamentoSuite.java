package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.control.EquipamentControllerTest;
import test.model.EquipamentoTest;
import test.persistence.EquipamentDAOTest;


@RunWith(Suite.class)
@SuiteClasses({EquipamentoTest.class, EquipamentDAOTest.class, EquipamentControllerTest.class })
public class EquipamentoSuite {

}
