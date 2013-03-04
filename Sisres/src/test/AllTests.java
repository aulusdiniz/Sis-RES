package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({EquipamentSuite.class, ProfessorSuite.class, StudentSuite.class, SalaSuite.class,
				ReserveStudentRoomSuite.class, ReservaSalaProfessorSuite.class})
public class AllTests {

}
