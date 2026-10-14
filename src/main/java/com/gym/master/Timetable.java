package com.gym.master;

import java.util.*;
import java.util.stream.Collectors;


public class Timetable {

    private final HashMap<DayOfWeek, TreeMap<TimeOfDay, Set<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        timetable.computeIfAbsent(trainingSession.getDayOfWeek(), k -> new TreeMap<>())
                .computeIfAbsent(trainingSession.getTimeOfDay(), k -> new HashSet<>())
                .add(trainingSession);
    }

    //как реализовать, тоже непонятно, но сложность должна быть О(1)
    public Map<TimeOfDay, Set<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.getOrDefault(dayOfWeek, new TreeMap<>());
    }

    //как реализовать, тоже непонятно, но сложность должна быть О(1)
    public Set<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        return timetable.getOrDefault(dayOfWeek, new TreeMap<>())
                .getOrDefault(timeOfDay, new HashSet<>());
    }

    //лучше всего o log(n)
    //
    public LinkedHashMap<Coach, Long> getCountByCoaches() {
        Map<Coach, Long> dirtyCoach = getAllCoachByWeek();
        return sortCoach(dirtyCoach);
    }
    private LinkedHashMap<Coach, Long> sortCoach(Map<Coach, Long> dirtyCoach) {
        LinkedHashMap<Coach, Long> coachMap = new LinkedHashMap<>();
        dirtyCoach.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .forEach(entry -> coachMap.put(entry.getKey(), entry.getValue()));
        return coachMap;
    }

    private Map<Coach, Long> getAllCoachByWeek() {
        return timetable
                .values()
                .stream()
                .flatMap(timeOfDayListHashMap -> timeOfDayListHashMap.values().stream())
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(TrainingSession::getCoach, Collectors.counting()));
    }
}
