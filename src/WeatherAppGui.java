import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WeatherAppGui extends JFrame {
    private JSONObject weatherData;
    public WeatherAppGui(){

        super("Weather App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450,650);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        addGuiComponents();

    }
    private void addGuiComponents() {
        // TextBox
        JTextField searchTextField = new JTextField();
        searchTextField.setBounds(15,15,351,45);
        searchTextField.setFont(new Font("Dialog", Font.PLAIN, 24));
        add(searchTextField);



        // weather image
        JLabel weatherConditionImage = new JLabel(loadImage("src/assets/cloudy.png"));
        weatherConditionImage.setBounds(0,125,450,217);
        add(weatherConditionImage);

        // temperature text
        JLabel temperatureText = new JLabel("70 F");
        temperatureText.setBounds(0,350,450,54);
        temperatureText.setFont(new Font("Dialog", Font.BOLD, 48));
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureText);

        // weather description
        JLabel weatherConditionDesc = new JLabel("Cloudy");
        weatherConditionDesc.setBounds(0,405,450,36);
        weatherConditionDesc.setFont(new Font("Dialog", Font.PLAIN, 32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionDesc);

        //humidity Icon
        JLabel humidityIcon = new JLabel(loadImage("src/assets/humidity.png"));
        humidityIcon.setBounds(15,500,74,66);
        add(humidityIcon);

        //humidity text
        JLabel humidityDesc = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityDesc.setBounds(90,500,85,55);
        humidityDesc.setFont(new Font("Dialog", Font.PLAIN,16));
        add(humidityDesc);

        // wind speed icon
        JLabel windSpeedIcon = new JLabel(loadImage("src/assets/windspeed.png"));
        windSpeedIcon.setBounds(220,500,74,66);
        add(windSpeedIcon);
        // WindSpeed Text
        JLabel windSpeedDesc = new JLabel("<html><b>Windspeed</b> 15mph</html>");
        windSpeedDesc.setBounds(310,500,85,55);
        windSpeedDesc.setFont(new Font("Dialog", Font.PLAIN,16));
        add(windSpeedDesc);

        // search button
        JButton searchButton = new JButton(loadImage("src/assets/search.png"));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375,13,47,45);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = searchTextField.getText();
                if(userInput.replaceAll("\\s", "").length() <= 0) {
                    return;
                }

                weatherData = WeatherApp.getWeatherData(userInput);

                // weather image
                String weatherCondition = (String) weatherData.get("weather_condition");
                switch (weatherCondition) {
                    case "Clear":
                        weatherConditionImage.setIcon(loadImage("src/assets/clear.png"));
                        break;
                    case "Cloudy":
                        weatherConditionImage.setIcon(loadImage("src/assets/cloudy.png"));
                        break;
                    case "Rain":
                        weatherConditionImage.setIcon(loadImage("src/assets/rain.png"));
                        break;
                    case "Snow":
                        weatherConditionImage.setIcon(loadImage("src/assets/snow.png"));
                        break;
                }

                // update temperature
                double temperature = (double) weatherData.get("temperature");
                temperatureText.setText(temperature +" F");

                //update Weather Condition
                weatherConditionDesc.setText(weatherCondition);

                //update humidity
                long humidity = (long) weatherData.get("humidity");
                humidityDesc.setText("<html><b>Humidity</b> " + humidity + "%</html>");

                //update Wind Speed
                double windspeed = (double) weatherData.get("windspeed");
                windSpeedDesc.setText("<html><b>Windspeed</b> " + windspeed + "mph</html>");
            }
        });
        add(searchButton);

    }
    // method for making the images in the Gui
    private ImageIcon loadImage(String path) {
        try {
            BufferedImage image = ImageIO.read(new File(path));
            return new ImageIcon(image);
        } catch(IOException e) {
            e.printStackTrace();
        }
        System.out.println("Could not find resource");
        return null;

    }
}
