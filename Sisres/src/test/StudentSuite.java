package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.control.StudentControllerTest;
import test.model.StudentTest;
import test.persistence.StudentDAOTest;


@RunWith(Suite.class)
@SuiteClasses({StudentTest.class, StudentDAOTest.class, StudentControllerTest.class })
public class StudentSuite {

}
