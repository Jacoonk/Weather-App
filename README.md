<h1 align="center">Weather App</h1>

The Weather App is a Java-based application that delivers real-time weather details for the location specified by the user. This application fetches data from the Weather Forecast API and Geolocation API to display accurate weather data in the graphical user interface. The user is presented with the location's temperature, weather conditions, humidity, and wind speed. This document outlines the project's architecture, technologies used, and the overall functionality of each class in the app.

## Technologies Used
The Weather App utilizes these technologies and libraries
* Java 18
* JSON Simple - Used to parse and analyze through JSON data
* HTTPURLConnection - Java's built-in library for making HTTP requests to fetch data from external APIs

## Class Functionalities
### AppLauncher
The AppLauncher class initializes the GUI and displays the application window
### WeatherAppGui
The WeatherAppGui class is the graphical interface of the app and is used to display the weather information for a user's specified location. This class controls the layout and display of the GUI components such as text fields, buttons, labels, and images. It also updates the GUI and weather information according to the user's specified location.
### WeatherApp
This class is responsible for the main functionality of the Weather App. It contains methods to fetch weather data and location coordinates from external APIs, convert weather codes into readable weather conditions, and manage API requests. This class connects the GUI and the external weather data from the external APIs and ensures that the data is retrieved and displayed properly.
