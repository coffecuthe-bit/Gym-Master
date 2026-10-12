package com.gym.master;

import java.util.*;


public class Timetable {

    private HashMap<DayOfWeek, HashMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        HashMap<TimeOfDay, List<TrainingSession>> dateTime = timetable
                .computeIfAbsent(trainingSession.getDayOfWeek(), k -> new HashMap<>());

        dateTime.computeIfAbsent(trainingSession.getTimeOfDay(), k -> new ArrayList<>()).add(trainingSession);
    }

    //как реализовать, тоже непонятно, но сложность должна быть О(1)
    public HashMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.getOrDefault(dayOfWeek, new HashMap<>());
    }

    //как реализовать, тоже непонятно, но сложность должна быть О(1)
    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        return timetable.getOrDefault(dayOfWeek, new HashMap<>())
                .getOrDefault(timeOfDay, new ArrayList<>());
    }

    //лучше всего o log(n)
    //TODO
    public TreeMap<Coach, Integer> getCountByCoaches() {
        TreeMap<Coach, Integer> countByCoaches = new TreeMap<>();


        return null;
    }


}
