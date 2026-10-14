import com.gym.master.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


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
        Assert.assertSame("За четверг должно быть два занятия", 2, actualCountForThursday);

        String firstTrainForThursday = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY)
                .keySet()
                .stream()
                .findFirst().orElseThrow(() -> new RuntimeException("Пустой объект 'firstTrainForThursday'"))
                .toString();

        String secondTrainForThursday = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY)
                .keySet()
                .stream()
                .skip(1)
                .findFirst().orElseThrow(() -> new RuntimeException("Пустой объект 'firstTrainForThursday'"))
                .toString();
        Assert.assertEquals("Первая тренировка должна быть в 13:00", "13:00", firstTrainForThursday);

        Assert.assertEquals("Крайняя тренировка должна быть в 20:00", "20:00", secondTrainForThursday);

        // Проверить, что за вторник не вернулось занятий
        int actualCountForTuesday = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).size();
        Assert.assertEquals(0, actualCountForTuesday);
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
        int actualSizeForMondayAtThirteen = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0)).size();
        Assert.assertEquals("за понедельник в 13:00 должно вернутся одно занятие", 1, actualSizeForMondayAtThirteen);


        //Проверить, что за понедельник в 14:00 не вернулось занятий
        int actualSizeForMondayAtFourteen = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0)).size();
        Assert.assertEquals("за понедельник в 14:00 не должно вернутся ни одно занятие", 0, actualSizeForMondayAtFourteen);
    }

    @Test
    public void testGetCountByCoachesSingle() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);

        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.MONDAY, new TimeOfDay(20, 0));
        TrainingSession thursdayAdultTrainingSessionDuplicate = new TrainingSession(groupAdult, coach,
                DayOfWeek.MONDAY, new TimeOfDay(20, 0));
        timetable.addNewTrainingSession(thursdayAdultTrainingSessionDuplicate);
        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        //Проверка, что за неделю есть 1 тренер с 1 тренировкой и ФИО "Васильев", "Николай", "Сергеевич"
        Map<Coach, Long> actualCoaches = timetable.getCountByCoaches();
        int actualCouchCount = actualCoaches.size();
        Map.Entry<Coach, Long> actualCouchEntry = actualCoaches.entrySet().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Пустой объект 'firstTrainForThursday'"));

        Assert.assertEquals(1, actualCouchCount);

        String actualFullName = actualCouchEntry.getKey().toString();
        Assert.assertEquals(coach.toString(), actualFullName);


        long actualTrainCount = actualCouchEntry.getValue();
        Assert.assertEquals(1, actualTrainCount);
    }

    @Test()
    public void testGetCountByCoachesUniqueCoachesAndTrain() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        Coach coachDuplicate = new Coach("Васильев", "Николай", "Сергеевич");

        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.MONDAY, new TimeOfDay(20, 0));

        TrainingSession thursdayAdultTrainingSessionDuplicate = new TrainingSession(groupAdult, coachDuplicate,
                DayOfWeek.MONDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);
        timetable.addNewTrainingSession(thursdayAdultTrainingSessionDuplicate);
        //Проверка, что за неделю не будет дубликатов тренеров и занятий на одно и тоже время
        Map<Coach, Long> actualCoaches = timetable.getCountByCoaches();
        int actualCount = actualCoaches.size();
        Assert.assertEquals(1, actualCount);

        long actualTrainCount = actualCoaches
                .entrySet().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Пустой объект 'Map<Couch,Long>'"))
                .getValue();
        Assert.assertEquals(1, actualTrainCount);
    }

    private Timetable initializeTimeTableWitchCoaches(Coach coach, Coach coachTwo, Coach coachTree) {
        Timetable timetable = new Timetable();
        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        TrainingSession wednesdayAdultTrainingSessionTwo = new TrainingSession(groupAdult, coachTwo,
                DayOfWeek.WEDNESDAY, new TimeOfDay(20, 0));
        TrainingSession fridayAdultTrainingSessionTwo = new TrainingSession(groupAdult, coachTwo,
                DayOfWeek.FRIDAY, new TimeOfDay(20, 0));
        TrainingSession fridayAdultTrainingSessionTree = new TrainingSession(groupAdult, coachTree,
                DayOfWeek.FRIDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(fridayAdultTrainingSessionTree);

        timetable.addNewTrainingSession(fridayAdultTrainingSessionTwo);
        timetable.addNewTrainingSession(wednesdayAdultTrainingSessionTwo);
        timetable.addNewTrainingSession(thursdayAdultTrainingSession);
        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);
        return timetable;
    }

    @Test
    public void testGetCountByCoachesSorted() {
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coachTwo = new Coach("Дмитриев", "Николай", "Васильевич");
        Coach coachTree = new Coach("Дмитриев", "Сергей", "Николаевич");
        Timetable timetable = initializeTimeTableWitchCoaches(coach, coachTwo, coachTree);

        Map<Coach, Long> actualCoaches = timetable.getCountByCoaches();
        // Проверка что вернулось 3 тренера
        long expectedCoachCount = 3;
        long actualCoachCount = actualCoaches.size();
        Assert.assertEquals(expectedCoachCount, actualCoachCount);
        //Проверка что вернулось отсортировонные значения

        long expectedCouchTrainCountFirst = 4;
        Set<Map.Entry<Coach, Long>> actualCouchEntry = actualCoaches.entrySet();

        String expectedCouchFullName = coach.toString();

        Map.Entry<Coach, Long> actualFirstCouchEntry = actualCouchEntry.stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Объект не должен быть пустым 'Map<Couch,Long>'"));
        long actualCouchTrainFirst = actualFirstCouchEntry
                .getValue();
        String actualExpectedFullName = actualFirstCouchEntry.getKey().toString();
        //сравниваем ФИО первого тренера
        Assert.assertEquals(expectedCouchFullName, actualExpectedFullName);
        //сравниваем первое кол-во тренировок
        Assert.assertEquals(expectedCouchTrainCountFirst, actualCouchTrainFirst);

        Map.Entry<Coach, Long> actualMiddleCouchEntry = actualCouchEntry.stream().skip(1).findFirst()
                .orElseThrow(() -> new RuntimeException("Объект не должен быть пустым 'Map<Couch,Long>'"));
        long expectedCoachTrainMiddle = 2;
        String expectedCoachFullNameMiddle = coachTwo.toString();
        long actualCouchTrainMiddle = actualMiddleCouchEntry.getValue();
        String actualCoachFullnameMiddle = actualMiddleCouchEntry.getKey().toString();
        // сравниваем второго тренера и его кол-во тренировок
        Assert.assertEquals(expectedCoachTrainMiddle, actualCouchTrainMiddle);
        Assert.assertEquals(expectedCoachFullNameMiddle, actualCoachFullnameMiddle);

        Map.Entry<Coach, Long> actualSecondCouchEntry = actualCouchEntry.stream().skip(2).findFirst()
                .orElseThrow(() -> new RuntimeException("Объект не должен быть пустым 'Map<Couch,Long>'"));
        long expectedCoachTrainSecond = 1;
        String expectedCoachFullnameSecond = coachTree.toString();

        long actualCoachTrainSecond = actualSecondCouchEntry.getValue();
        String actualCoachFullnameSecond = actualSecondCouchEntry.getKey().toString();
        //сравниваем крайнего тренера и его кол-во тренировок
        Assert.assertEquals(String.format("Ожидается %s кол-во тренировок", expectedCoachTrainSecond), expectedCoachTrainSecond, actualCoachTrainSecond);

        Assert.assertEquals(expectedCoachFullnameSecond, actualCoachFullnameSecond);

    }

}


