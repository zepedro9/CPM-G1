enum WeatherStatus {
  SUNNY,
  PARTLY_CLOUDS,
  CLOUDY,
  RAIN,
  THUNDERSTORM,
  SNOW,
  MIST,
  PARTLY_RAINING,
}

/// Verifies what should be the
String getTemperatureIcon(WeatherStatus wheaterStatus) {
  switch (wheaterStatus) {
    case WeatherStatus.SUNNY:
      return "weather-sunny";
    case WeatherStatus.PARTLY_CLOUDS:
      return "weather-partly-cloudy";
    case WeatherStatus.CLOUDY:
      return "weather-cloudy";
    case WeatherStatus.RAIN:
      return "weather-pouring";
    case WeatherStatus.THUNDERSTORM:
      return "weather-lightning";
    case WeatherStatus.SNOW:
      return "weather-snowy";
    case WeatherStatus.MIST:
      return "weather-fog";
    case WeatherStatus.PARTLY_RAINING: 
      return "weather-partly-raining";
    default:
      return "weather-sunny";
  }
}

WeatherStatus getWeatherStatus(String iconId) {
    iconId = iconId.substring(0, 2); 
    switch(iconId){
      case "01":
        return WeatherStatus.SUNNY;
      case "02": 
        return WeatherStatus.PARTLY_CLOUDS;
      case "03": 
        return WeatherStatus.CLOUDY; 
      case "04": 
        return WeatherStatus.PARTLY_CLOUDS;
      case "09": 
        return WeatherStatus.RAIN; 
      case "10":
        return WeatherStatus.PARTLY_RAINING; 
      case "11":
        return WeatherStatus.THUNDERSTORM; 
      case "13":
        return WeatherStatus.SNOW;
      case "50":
        return WeatherStatus.MIST; 
      default:
        return WeatherStatus.SUNNY; 
      
    }
}
