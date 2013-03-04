package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.control.StudentControllerTest;
import test.model.AlunoTest;
import test.persistence.AlunoDAOTest;


@RunWith(Suite.class)
@SuiteClasses({AlunoTest.class, AlunoDAOTest.class, StudentControllerTest.class })
public class AlunoSuite {

}
