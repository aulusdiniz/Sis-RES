package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.control.ReserveRoomProfesssorControllerTest;
import test.model.ReservaSalaProfessorTest;
import test.persistence.ReserveRoomProfessorDAOTest;

@RunWith(Suite.class)
@SuiteClasses({ReservaSalaProfessorTest.class, ReserveRoomProfessorDAOTest.class, ReserveRoomProfesssorControllerTest.class})
public class ReservaSalaProfessorSuite {

}
