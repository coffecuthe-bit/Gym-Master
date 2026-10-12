import com.gym.master.*;
import org.junit.Assert;
import org.junit.Test;


public class TimetableTest {


    @Test
    public void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        int actualSizeForMonday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size();
        Assert.assertSame("За понедельник должно вернуться одно занятие",
                1, actualSizeForMonday);

        //Проверить, что за вторник не вернулось занятий
        int actualSizeForTuesday = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).size();
        Assert.assertSame("За вторник не должно вернуться занятий",
                0, actualSizeForTuesday);
    }

    @Test
    public void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        int actualSizeForMonday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size();
        Assert.assertSame("За понедельник должно вернуться одно занятие",
                1, actualSizeForMonday);


        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        int actualCountForThursday = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY).size();
        Assert.assertSame("За четверг должно быть два занятия",2, actualCountForThursday);

        String firstTrainForThursday = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY)
                .keySet()
                .stream()
                .findFirst().orElseThrow(() -> new RuntimeException("Пустой объект 'firstTrainForThursday'"))
                .toString();


        String secondTrainForThursday =  timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY)
                .keySet()
                .stream()
                .skip(1)
                .findFirst().orElseThrow(() -> new RuntimeException("Пустой объект 'firstTrainForThursday'"))
                .toString();
        Assert.assertEquals("Первая тренировка должна быть в 13:00","13:00", firstTrainForThursday);

        Assert.assertEquals("Крайняя тренировка должна быть в 20:00","20:00", secondTrainForThursday);

        // Проверить, что за вторник не вернулось занятий
        int actualCountForTuesday =   timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).size();
        Assert.assertEquals(0,actualCountForTuesday);
    }

    @Test
    public void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        int actualSizeForMondayAtThirteen =timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0)).size();
        Assert.assertEquals("за понедельник в 13:00 должно вернутся одно занятие",1,actualSizeForMondayAtThirteen);


        //Проверить, что за понедельник в 14:00 не вернулось занятий
        int actualSizeForMondayAtFourteen =timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0)).size();
        Assert.assertEquals("за понедельник в 14:00 не должно вернутся ни одно занятие",0,actualSizeForMondayAtFourteen);
    }
}


