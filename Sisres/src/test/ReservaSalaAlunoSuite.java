package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.control.ReserveStudentRoomControllerTest;
import test.model.ResSalaAlunoTest;
import test.persistence.ReserveRoomStudentDAOTest;

@RunWith(Suite.class)
@SuiteClasses({ResSalaAlunoTest.class, ReserveRoomStudentDAOTest.class, ReserveStudentRoomControllerTest.class})
public class ReservaSalaAlunoSuite {

}
