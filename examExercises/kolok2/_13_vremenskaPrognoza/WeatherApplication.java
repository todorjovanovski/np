package kolok2._13_vremenskaPrognoza;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class WeatherApplication {

    public static void main(String[] args) {
        WeatherDispatcher weatherDispatcher = new WeatherDispatcher();

        CurrentConditionsDisplay currentConditions = new CurrentConditionsDisplay(weatherDispatcher);
        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherDispatcher);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            weatherDispatcher.setMeasurements(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]), Float.parseFloat(parts[2]));
            if(parts.length > 3) {
                int operation = Integer.parseInt(parts[3]);
                if(operation==1) {
                    weatherDispatcher.remove(forecastDisplay);
                }
                if(operation==2) {
                    weatherDispatcher.remove(currentConditions);
                }
                if(operation==3) {
                    weatherDispatcher.register(forecastDisplay);
                }
                if(operation==4) {
                    weatherDispatcher.register(currentConditions);
                }

            }
        }
    }
}


interface Observer {
    void display();
    void update(float temperature, float humidity, float pressure);
}

interface Observable {
    void setMeasurements(float temperature, float humidity, float pressure);
    void register(Observer observer);
    void remove(Observer observer);
}

class WeatherDispatcher implements Observable {
    private Set<Observer> observers;

    public WeatherDispatcher() {
        observers = new HashSet<>();
    }

    @Override
    public void setMeasurements(float temperature, float humidity, float pressure) {
        observers.forEach(observer -> observer.update(temperature, humidity, pressure));
        System.out.println("");
    }

    @Override
    public void register(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void remove(Observer observer) {
        observers.remove(observer);
    }
}

class ForecastDisplay implements Observer {
    private float pressure;
    private String message;

    public ForecastDisplay(WeatherDispatcher weatherDispatcher) {
        weatherDispatcher.register(this);
        pressure = 0;
    }

    @Override
    public void display() {
        System.out.println("Forecast: " + message);
    }

    @Override
    public void update(float temperature, float humidity, float pressure) {
        if(pressure > this.pressure) {
            message = "Improving";
        } else if(pressure < this.pressure) {
            message = "Cooler";
        } else {
            message = "Same";
        }
        this.pressure = pressure;
        display();
    }
}

class CurrentConditionsDisplay implements Observer {
    private float temperature;
    private float humidity;

    public CurrentConditionsDisplay(WeatherDispatcher weatherDispatcher) {
        weatherDispatcher.register(this);
    }

    @Override
    public void display() {
        System.out.printf("Temperature: %.1fF\nHumidity: %.1f%%\n", temperature, humidity);
    }

    @Override
    public void update(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        display();
    }
}
