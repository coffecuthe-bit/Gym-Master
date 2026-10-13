package com.gym.master;

import java.util.*;
import java.util.stream.Collectors;


public class Timetable {

    private HashMap<DayOfWeek, HashMap<TimeOfDay, Set<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        timetable.computeIfAbsent(trainingSession.getDayOfWeek(), k -> new HashMap<>())
                .computeIfAbsent(trainingSession.getTimeOfDay(), k -> new HashSet<>())
                .add(trainingSession);
    }

    //как реализовать, тоже непонятно, но сложность должна быть О(1)
    public HashMap<TimeOfDay, Set<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.getOrDefault(dayOfWeek, new HashMap<>());
    }

    //как реализовать, тоже непонятно, но сложность должна быть О(1)
    public Set<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        return timetable.getOrDefault(dayOfWeek, new HashMap<>())
                .getOrDefault(timeOfDay, new HashSet<>());
    }

    //лучше всего o log(n)
    //
    public Map<Coach, Long> getCountByCoaches() {
        Map<Coach, Long> dirtyCoach = getAllCoachByWeek();
        return sortCoach(dirtyCoach);
    }
    private Map<Coach, Long> sortCoach(Map<Coach, Long> dirtyCoach) {
        Map<Coach, Long> coachMap = new LinkedHashMap<>();
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
