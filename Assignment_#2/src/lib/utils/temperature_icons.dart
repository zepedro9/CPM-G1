enum WeatherStatus {
  SUNNY, 
  CLOUDY
}

/// Verifies what should be the
String getTemperatureIcon(WeatherStatus wheaterStatus) {
  switch (wheaterStatus) {
    case WeatherStatus.SUNNY:
      return "weather-sunny";
    case WeatherStatus.CLOUDY:
      return "weather-cloudy";
    default:
      return "weather-sunny";
  }
}