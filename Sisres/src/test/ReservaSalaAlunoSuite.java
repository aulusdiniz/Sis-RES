package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.control.ReserveRoomStudentControllerTest;
import test.model.ResSalaAlunoTest;
import test.persistence.ResSalaAlunoDAOTest;

@RunWith(Suite.class)
@SuiteClasses({ResSalaAlunoTest.class, ResSalaAlunoDAOTest.class, ReserveRoomStudentControllerTest.class})
public class ReservaSalaAlunoSuite {

}
