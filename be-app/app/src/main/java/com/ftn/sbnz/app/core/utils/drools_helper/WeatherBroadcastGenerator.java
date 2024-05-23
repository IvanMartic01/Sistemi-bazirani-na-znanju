package com.ftn.sbnz.app.core.utils.drools_helper;

import com.ftn.sbnz.model.drools_helper.PrecipitationType;
import com.ftn.sbnz.model.drools_helper.WeatherBroadcast;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;

@Component
public class WeatherBroadcastGenerator {

    private final String WINTER_SEASON = "Winter";
    private final String SPRING_SEASON = "Spring";
    private final String SUMMER_SEASON = "Summer";
    private final String AUTUMN_SEASON = "Autumn";

    public WeatherBroadcast generateWeather(LocalDate date) {
        int dateHash = date.getYear() * 1000000 + date.getMonthValue() * 100000 + date.getYear();
        int hashValue = (dateHash * 17) % 101;
        PrecipitationType precipitationType = PrecipitationType.NOTHING;
        String season = getSeason(date);
        switch (hashValue % 3) {
            case 1:
                precipitationType = PrecipitationType.RAIN;
                break;
            case 2:
                if (season.equals(WINTER_SEASON)) {
                    precipitationType = PrecipitationType.SNOW;
                }
                break;
        }

        return WeatherBroadcast.builder()
                .date(date)
                .temperature(getTemperatureFromChance(getSeason(date), hashValue))
                .precipitation(precipitationType)
                .build();
    }

    public String getSeason(LocalDate date) {
        Month month = date.getMonth();
        int day = date.getDayOfMonth();

        return switch (month) {
            case DECEMBER -> (day < 21) ? AUTUMN_SEASON : WINTER_SEASON;
            case JANUARY, FEBRUARY -> WINTER_SEASON;
            case MARCH -> (day < 21) ? WINTER_SEASON : SPRING_SEASON;
            case APRIL, MAY -> SPRING_SEASON;
            case JUNE -> (day < 21) ? SPRING_SEASON : SUMMER_SEASON;
            case JULY, AUGUST -> SUMMER_SEASON;
            case SEPTEMBER -> (day < 23) ? SUMMER_SEASON : AUTUMN_SEASON;
            case OCTOBER, NOVEMBER -> AUTUMN_SEASON;
        };
    }

    private double getTemperatureFromChance(String season, int chance) {
        double minTemperature, maxTemperature;
        switch (season) {
            case WINTER_SEASON:
                minTemperature = 0.0;
                maxTemperature = 10.0;
                break;
            case SPRING_SEASON:
                minTemperature = 15.0;
                maxTemperature = 30.0;
                break;
            case SUMMER_SEASON:
                minTemperature = 25.0;
                maxTemperature = 40.0;
                break;
            case AUTUMN_SEASON:
                minTemperature = 10.0;
                maxTemperature = 25.0;
                break;
            default:
                minTemperature = 0.0;
                maxTemperature = 0.0;
                break;
        }
        return calculateTemperature(minTemperature, maxTemperature, chance / 100.0);
    }

    private double calculateTemperature(double minTemperature, double maxTemperature, double chance) {
        return (maxTemperature - minTemperature) * chance + minTemperature;
    }
}
