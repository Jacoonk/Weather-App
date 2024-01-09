// retrieve data from api, then the backend logic will fetch the latest weather data
// from the external api  and return it, The Gui will update and display this data


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class WeatherApp {
    // fetching data based off the location
    public static JSONObject getWeatherData(String locationName) {
        // gets the location coordinates using geolocation API
        JSONArray locationData = getLocationData(locationName);
        // retrieving latitude and longitude data
        JSONObject location =  (JSONObject) locationData.get(0);
        double latitude = (double)location.get("latitude");
        double longitude = (double)location.get("longitude");

        // build api request with the coords
        String urlString = "https://api.open-meteo.com/v1/forecast?" +
                "latitude=" + latitude + "&longitude=" + longitude +
                "&hourly=temperature_2m,relativehumidity_2m,weathercode,windspeed_10m&temperature_unit=fahrenheit&wind_speed_unit=mph&timezone=America%2FNew_York";


        try {
            // Call the API
            HttpURLConnection conn = fetchApiResponse(urlString);
            if (conn.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to API");
                return null;
            }
            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());
            // reading the data and storing it
            while(scanner.hasNext()) {
                resultJson.append(scanner.nextLine());
            }
            scanner.close();
            conn.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

            // retrieve hourly weather data
            JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");
            // Look at current hourly data
            JSONArray time = (JSONArray) hourly.get("time");
            int index = findCurrentIndex(time);

            // get Temperature
            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
            double temperature = (double)temperatureData.get(index);

            // get Weather code
            JSONArray weathercode = (JSONArray) hourly.get("weathercode");
            String weatherCondition = convertWeatherCode((long) weathercode.get(index));

            //get Humidity
            JSONArray humidityData = (JSONArray) hourly.get("relativehumidity_2m");
            long humidity = (long) humidityData.get(index);

            // Wind Speed
            JSONArray windData = (JSONArray) hourly.get("windspeed_10m");
            double windspeed = (double) windData.get(index);

            // Json data object for front end to access
            JSONObject weatherData = new JSONObject();
            weatherData.put("temperature", temperature);
            weatherData.put("weather_condition", weatherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("windspeed", windspeed);

            return weatherData;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // this method retrieves the latitude and longitude coords for the given location name
    public static JSONArray getLocationData(String locationName){
        locationName = locationName.replaceAll(" ", "+");

        // build API url with the location parameter
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                locationName + "&count=10&language=en&format=json";
        try {
            // Call the API
            HttpURLConnection conn = fetchApiResponse(urlString);

            // check response 200- Succcess, 400 - bad, 500 - error
            if (conn.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to API");
                return null;
            } else {
                // store the results from the API
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());
                // reading the data and storing it
                while(scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }
                scanner.close();
                conn.disconnect(); // closing the url connection
                // JSON String --> JSON Object
                JSONParser parser = new JSONParser();
                JSONObject resultsJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

                // list of location data from the API generated from the location name
                JSONArray locationData = (JSONArray) resultsJsonObj.get("results");
                return locationData;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        // location not found
        return null;
    }

    private static HttpURLConnection fetchApiResponse(String urlString) {
        try {
            // try to create connection
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //set request method to get
            conn.setRequestMethod("GET");
            conn.connect();
            return conn;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int findCurrentIndex(JSONArray timeArray){
        String currentTime = getCurrentTime();
        for(int i = 0; i<timeArray.size(); i++) {
            String time = (String) timeArray.get(i);
            if (time.equalsIgnoreCase(currentTime)){
                return i;
            }
        }
        return 0;
    }

    public static String getCurrentTime(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");
        String formattedDateTime = currentDateTime.format(formatter);
        return formattedDateTime;
    }

    private static String convertWeatherCode(long weathercode) {
        String weatherCondition = "";

        if (weathercode == 0L) {
            weatherCondition = "Clear";
        } else if (weathercode > 0L && weathercode <= 3L)  {
            weatherCondition ="Cloudy";
        } else if ((weathercode >= 51L && weathercode <= 67L) || (weathercode >= 80L  && weathercode <= 99L)) {
            weatherCondition = "Rain";
        } else if (weathercode >= 71L  && weathercode <= 77L) {
            weatherCondition = "Snow";
        }

        return weatherCondition;
    }

}
