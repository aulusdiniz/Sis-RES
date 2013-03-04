package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.control.ReserveStudentRoomControllerTest;
import test.model.ReserveStudentRoomTest;
import test.persistence.ReserveRoomStudentDAOTest;

@RunWith(Suite.class)
@SuiteClasses({ReserveStudentRoomTest.class, ReserveRoomStudentDAOTest.class, ReserveStudentRoomControllerTest.class})
public class ReserveStudentRoomSuite {

}
