import com.gym.master.*;
import org.junit.Assert;
import org.junit.Test;

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
        Assert.assertEquals(1, actualSizeForMonday);

        //Проверить, что за вторник не вернулось занятий
        int actualSizeForTuesday = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).size();
        Assert.assertEquals(0, actualSizeForTuesday);
    }

    @Test
    public void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = createTimetable();
        TimeOfDay expectedThirteenTimeOfDay = new TimeOfDay(13, 0);
        TimeOfDay expectedTwentyTimeOfDay = new TimeOfDay(20, 0);
        // Проверить, что за понедельник вернулось одно занятие
        int actualSizeForMonday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size();
        Assert.assertEquals(1, actualSizeForMonday);


        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        int actualCountForThursday = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY).size();
        Assert.assertEquals("За четверг должно быть два занятия", 2, actualCountForThursday);

        TimeOfDay firstTrainForThursday = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY)
                .keySet()
                .stream()
                .findFirst().orElseThrow(() -> new RuntimeException("Пустой объект 'firstTrainForThursday'"));

        TimeOfDay secondTrainForThursday = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY)
                .keySet()
                .stream()
                .skip(1)
                .findFirst().orElseThrow(() -> new RuntimeException("Пустой объект 'firstTrainForThursday'"));
        Assert.assertEquals(expectedThirteenTimeOfDay, firstTrainForThursday);

        Assert.assertEquals(expectedTwentyTimeOfDay, secondTrainForThursday);

        // Проверить, что за вторник не вернулось занятий
        int actualCountForTuesday = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).size();
        Assert.assertEquals(0, actualCountForTuesday);
    }

    private Timetable createTimetable() {
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
        return timetable;
    }

    @Test
    public void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = createTimetable();
        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        int actualSizeForMondayAtThirteen = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0)).size();
        Assert.assertEquals(1, actualSizeForMondayAtThirteen);

        //Проверить, что за понедельник в 14:00 не вернулось занятий
        int actualSizeForMondayAtFourteen = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0)).size();
        Assert.assertEquals(0, actualSizeForMondayAtFourteen);
    }

    @Test
    public void testGetCountByCoachesSingle() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);

        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));
        TrainingSession thursdayAdultTrainingSessionDuplicate = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));
        timetable.addNewTrainingSession(thursdayAdultTrainingSessionDuplicate);
        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        //Проверка, что за неделю есть 1 тренер с 1 тренировкой и ФИО "Васильев", "Николай", "Сергеевич"
        Map<Coach, Long> actualCoaches = timetable.getCountByCoaches();
        int actualCoachCount = actualCoaches.size();
        Map.Entry<Coach, Long> actualCoachEntry = actualCoaches.entrySet().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Пустой объект 'firstTrainForThursday'"));

        Assert.assertEquals(1, actualCoachCount);

        Coach actualCoach = actualCoachEntry.getKey();
        Assert.assertEquals(coach.getName(), actualCoach.getName());
        Assert.assertEquals(coach.getSurname(), actualCoach.getSurname());
        Assert.assertEquals(coach.getMiddleName(), actualCoach.getMiddleName());

        long actualTrainCount = actualCoachEntry.getValue();
        Assert.assertEquals(1, actualTrainCount);
    }

    @Test()
    public void testGetCountByCoachesUniqueCoachesAndTrain() {
        Timetable timetable = getDuplicateTimeTable();
        //Проверка, что за неделю не будет дубликатов тренеров и занятий на одно и тоже время
        Map<Coach, Long> actualCoaches = timetable.getCountByCoaches();
        int actualCount = actualCoaches.size();
        Assert.assertEquals(1, actualCount);

        long actualTrainCount = actualCoaches
                .entrySet().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Пустой объект 'Map<Coach,Long>'"))
                .getValue();
        Assert.assertEquals(1, actualTrainCount);
    }

    private Timetable getDuplicateTimeTable() {
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
        return timetable;
    }

    private Timetable initializeTimeTableWitchCoaches(Coach coach, Coach coachTwo, Coach coachThree) {
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
        TrainingSession fridayAdultTrainingSessionTree = new TrainingSession(groupAdult, coachThree,
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
        Coach coachThree = new Coach("Дмитриев", "Сергей", "Николаевич");
        Timetable timetable = initializeTimeTableWitchCoaches(coach, coachTwo, coachThree);

        Map<Coach, Long> actualCoaches = timetable.getCountByCoaches();
        // Проверка что вернулось 3 тренера
        long expectedCoachCount = 3;
        long actualCoachCount = actualCoaches.size();
        Assert.assertEquals(expectedCoachCount, actualCoachCount);
        //Проверка что вернулось отсортировонные значения

        long expectedCoachTrainCountFirst = 4;
        Set<Map.Entry<Coach, Long>> actualCoachEntry = actualCoaches.entrySet();

        Map.Entry<Coach, Long> actualFirstCoachEntry = actualCoachEntry.stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Объект не должен быть пустым 'Map<Coach,Long>'"));
        long actualCoachTrainFirst = actualFirstCoachEntry
                .getValue();
        Coach actualFirstCoach = actualFirstCoachEntry.getKey();
        //сравниваем ФИО первого тренера
        Assert.assertEquals(coach, actualFirstCoach);
        //сравниваем первое кол-во тренировок
        Assert.assertEquals(expectedCoachTrainCountFirst, actualCoachTrainFirst);

        Map.Entry<Coach, Long> actualMiddleCoachEntry = actualCoachEntry.stream().skip(1).findFirst()
                .orElseThrow(() -> new RuntimeException("Объект не должен быть пустым 'Map<Coach,Long>'"));
        long expectedCoachTrainMiddle = 2;

        long actualCoachTrainMiddle = actualMiddleCoachEntry.getValue();
        Coach actualMiddleCoach = actualMiddleCoachEntry.getKey();
        // сравниваем второго тренера и его кол-во тренировок
        Assert.assertEquals(expectedCoachTrainMiddle, actualCoachTrainMiddle);
        Assert.assertEquals(coachTwo, actualMiddleCoach);

        Map.Entry<Coach, Long> actualSecondCoachEntry = actualCoachEntry.stream().skip(2).findFirst()
                .orElseThrow(() -> new RuntimeException("Объект не должен быть пустым 'Map<Coach,Long>'"));
        long expectedCoachTrainSecond = 1;


        long actualCoachTrainSecond = actualSecondCoachEntry.getValue();
        Coach actualSecondCoach = actualSecondCoachEntry.getKey();
        //сравниваем крайнего тренера и его кол-во тренировок
        Assert.assertEquals(expectedCoachTrainSecond, actualCoachTrainSecond);
        Assert.assertEquals(coachThree, actualSecondCoach);

    }

    @Test
    public void testGetTrainingSessionMultiplySessionInTime() {
        Timetable timetable = new Timetable();
        Coach coachOne = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coachTwo = new Coach("Семёнов", "Виктор", "Константинович");
        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TimeOfDay time = new TimeOfDay(13, 0);

        TrainingSession first = new TrainingSession(groupChild, coachOne, DayOfWeek.MONDAY, time);
        TrainingSession second = new TrainingSession(groupAdult, coachTwo, DayOfWeek.MONDAY, time);

        timetable.addNewTrainingSession(first);
        timetable.addNewTrainingSession(second);

        Set<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, time);

        Assert.assertEquals(2, sessions.size());
        Assert.assertTrue(sessions.contains(first));
        Assert.assertTrue(sessions.contains(second));
    }

    @Test
    public void testGetTrainingSessionsForDayEmptyTimetable() {
        Timetable timetable = new Timetable();

        Map<TimeOfDay, Set<TrainingSession>> monday =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assert.assertNotNull(monday);
        Assert.assertTrue(monday.isEmpty());

        Set<TrainingSession> mondayAtThirteen =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        Assert.assertNotNull(mondayAtThirteen);
        Assert.assertTrue(mondayAtThirteen.isEmpty());

        Map<Coach, Long> counts = timetable.getCountByCoaches();
        Assert.assertNotNull(counts);
        Assert.assertTrue(counts.isEmpty());
    }

    @Test
    public void testGetCountByCoachesGroupsAndDays() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);

        timetable.addNewTrainingSession(new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(groupChild, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(groupAdult, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(20, 0)));

        Map<Coach, Long> counts = timetable.getCountByCoaches();

        Assert.assertEquals(1, counts.size());
        Assert.assertEquals(Long.valueOf(3), counts.get(coach));
    }
}


