package com.alex.meteodark;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SensorData {
    public static final int PARAMS = 5;
    public static final double mmHg = 0.75006375541921;

    private String _id;
    private int co2_mhz;
    private int temp_mhz;
    private double temp_bme;
    private double hum_bme;
    private double press_bme;
    private String date_time;

    public SensorData(String _id, int co2_mhz, int temp_mhz, double temp_bme, double hum_bme, double press_bme, String date_time) {
        this._id = _id;
        this.co2_mhz = co2_mhz;
        this.temp_mhz = temp_mhz;
        this.temp_bme = temp_bme;
        this.hum_bme = hum_bme;
        this.press_bme = press_bme;
        this.date_time = date_time;
    }

    public String get_id() {
        return _id;
    }

    public int getCo2_mhz() {
        return co2_mhz;
    }

    public int getTemp_mhz() {
        return temp_mhz;
    }

    public double getTemp_bme() {
        return temp_bme;
    }

    public double getHum_bme() {
        return hum_bme;
    }

    public double getPress_bme() {
        return press_bme;
    }

    public String getDate_time() {
        return date_time;
    }

    public int[] getPercentDataForProgressBar() {
        int[] preparedPercent = new int[PARAMS];
        preparedPercent[0] = getPercentReverse((int) this.temp_bme, 85, -40);
        preparedPercent[1] = getPercentReverse((int) this.hum_bme, 100, 0);
        preparedPercent[2] = getPercentReverse((int) (this.press_bme * mmHg), 1000, 0);
        preparedPercent[3] = getPercentReverse(this.temp_mhz, 85, -40);
        preparedPercent[4] = getPercentReverse(this.co2_mhz, 8000, 0);
        return preparedPercent;
    }

    private int getPercentReverse(int current, int max, int min) {
        return ((current - min) * 100 / (max - min));
    }

    public String[] getDataForTextView() {
        String[] preparedText = new String[PARAMS];
        preparedText[0] = "Temperature: " + this.temp_bme + " °C";
        preparedText[1] = "Humidity: " + (int) this.hum_bme + " %";
        preparedText[2] = "Pressure: " + (int) (this.press_bme * mmHg) + " mmHg";
        preparedText[3] = "Temperature: " + this.temp_mhz + " °C";
        preparedText[4] = "CO2: " + this.co2_mhz + " ppm";
        return preparedText;
    }

    public List<String> getDateTimeList() {
        ArrayList<String> dateTimeList = new ArrayList<>();
        Pattern pattern = Pattern.compile("[a-zA-Z]{3}|[0-9]{1,2}\\s|[0-9:]{8}\\s|[0-9]{4}");
        Matcher matcher = pattern.matcher(this.date_time);
        while (matcher.find()) {
            dateTimeList.add(this.date_time.substring(matcher.start(), matcher.end())
                    .replaceAll("(\\s+)", ""));
        }
        return dateTimeList;
    }

    public String getFormatDateTimeStr() {
        return getDateTimeList().get(2) + " " + getDateTimeList().get(1) + ", " + getDateTimeList().get(3);
    }

    @Override
    public String toString() {
        return "SensorData{" +
                "_id='" + _id + '\'' +
                ", co2_mhz=" + co2_mhz +
                ", temp_mhz=" + temp_mhz +
                ", temp_bme=" + temp_bme +
                ", hum_bme=" + hum_bme +
                ", press_bme=" + press_bme +
                ", dateTime='" + date_time + '\'' +
                '}';
    }
}
