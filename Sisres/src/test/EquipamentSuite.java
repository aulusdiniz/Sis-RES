package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.control.EquipamentControllerTest;
import test.model.EquipamentTest;
import test.persistence.EquipamentDAOTest;


@RunWith(Suite.class)
@SuiteClasses({EquipamentTest.class, EquipamentDAOTest.class, EquipamentControllerTest.class })
public class EquipamentSuite {

}
