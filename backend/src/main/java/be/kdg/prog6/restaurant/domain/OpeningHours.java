package be.kdg.prog6.restaurant.domain;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class OpeningHours {
    private final Map<DayOfWeek, DaySchedule> schedule;

    public OpeningHours(Map<DayOfWeek, DaySchedule> schedule) {
        if (schedule == null || schedule.isEmpty()) {
            throw new IllegalArgumentException("Schedule cannot be null or empty");
        }
        this.schedule = new HashMap<>(schedule);
    }


    public boolean isScheduledToBeOpen(LocalDateTime dateTime) {
        DayOfWeek day = dateTime.getDayOfWeek();
        DaySchedule daySchedule = schedule.get(day);

        if (daySchedule == null) {
            return false;
        }

        LocalTime time = dateTime.toLocalTime();
        return !time.isBefore(daySchedule.openTime()) && !time.isAfter(daySchedule.closeTime());
    }

    public boolean isOpenNow() {
        return isScheduledToBeOpen(LocalDateTime.now());
    }


    public static OpeningHours parse(String scheduleString) {
        if (scheduleString == null || scheduleString.trim().isEmpty()) {
            throw new IllegalArgumentException("Schedule string cannot be empty");
        }

        Map<DayOfWeek, DaySchedule> schedule = new HashMap<>();
        String[] dayEntries = scheduleString.split(",");

        for (String entry : dayEntries) {
            int colonIndex = entry.indexOf(':');
            if (colonIndex == -1) {
                throw new IllegalArgumentException("Invalid format. Expected DAY:HH:MM-HH:MM");
            }

            String dayPart = entry.substring(0, colonIndex).trim();
            String timePart = entry.substring(colonIndex + 1).trim();

            String[] times = timePart.split("-");
            if (times.length != 2) {
                throw new IllegalArgumentException("Invalid time range format. Expected HH:MM-HH:MM");
            }

            DayOfWeek day = DayOfWeek.valueOf(dayPart.toUpperCase());
            LocalTime openTime = LocalTime.parse(times[0].trim());
            LocalTime closeTime = LocalTime.parse(times[1].trim());

            schedule.put(day, new DaySchedule(openTime, closeTime));
        }

        return new OpeningHours(schedule);
    }


    public static OpeningHours createDefault() {
        Map<DayOfWeek, DaySchedule> schedule = new HashMap<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            schedule.put(day, new DaySchedule(LocalTime.of(10, 0), LocalTime.of(22, 0)));
        }
        return new OpeningHours(schedule);
    }

    public Map<DayOfWeek, DaySchedule> getSchedule() {
        return new HashMap<>(schedule);
    }

    @Override
    public String toString() {
        if (schedule.isEmpty()) return "No schedule";

        StringBuilder sb = new StringBuilder();
        schedule.forEach((day, daySchedule) ->
                sb.append(day.name().substring(0, 3))
                        .append(": ")
                        .append(daySchedule.openTime())
                        .append("-")
                        .append(daySchedule.closeTime())
                        .append(", ")
        );
        return sb.substring(0, sb.length() - 2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpeningHours that = (OpeningHours) o;
        return Objects.equals(schedule, that.schedule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schedule);
    }

    public record DaySchedule(LocalTime openTime, LocalTime closeTime) {
        public DaySchedule {
            if (openTime == null || closeTime == null) {
                throw new IllegalArgumentException("Times cannot be null");
            }
            if (openTime.isAfter(closeTime)) {
                throw new IllegalArgumentException("Open time must be before close time");
            }
        }
    }

    public String toStorageFormat() {
        return schedule.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey().name() + ":" +
                        entry.getValue().openTime() + "-" +
                        entry.getValue().closeTime())
                .collect(Collectors.joining(","));
    }
}