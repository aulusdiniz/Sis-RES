package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.control.ReserveRoomStudentControllerTest;
import test.model.ResSalaAlunoTest;
import test.persistence.ReserveRoomStudentDAOTest;

@RunWith(Suite.class)
@SuiteClasses({ResSalaAlunoTest.class, ReserveRoomStudentDAOTest.class, ReserveRoomStudentControllerTest.class})
public class ReservaSalaAlunoSuite {

}
