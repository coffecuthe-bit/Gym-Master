package com.gym.master;

import java.util.Objects;

public class TrainingSession {

    //группа
    private Group group;
    //тренер
    private Coach coach;
    //день недели
    private DayOfWeek dayOfWeek;
    //время начала занятия
    private TimeOfDay timeOfDay;

    public TrainingSession(Group group, Coach coach, DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        this.group = group;
        this.coach = coach;
        this.dayOfWeek = dayOfWeek;
        this.timeOfDay = timeOfDay;
    }

    public Group getGroup() {
        return group;
    }

    public Coach getCoach() {
        return coach;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public TimeOfDay getTimeOfDay() {
        return timeOfDay;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TrainingSession that = (TrainingSession) o;
        return Objects.equals(group, that.group) && Objects.equals(coach, that.coach) && dayOfWeek == that.dayOfWeek && Objects.equals(timeOfDay, that.timeOfDay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, coach, dayOfWeek, timeOfDay);
    }
}