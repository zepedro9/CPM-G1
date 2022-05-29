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
String getTemperatureIcon(WeatherStatus weatherStatus) {
  switch (weatherStatus) {
    case WeatherStatus.SUNNY:
      return "weatherSunny";
    case WeatherStatus.PARTLY_CLOUDS:
      return "weatherPartlyCloudy";
    case WeatherStatus.CLOUDY:
      return "weatherCloudy";
    case WeatherStatus.RAIN:
      return "weatherPouring";
    case WeatherStatus.THUNDERSTORM:
      return "weatherLightning";
    case WeatherStatus.SNOW:
      return "weatherSnowy";
    case WeatherStatus.MIST:
      return "weatherFog";
    case WeatherStatus.PARTLY_RAINING: 
      return "weatherPartlyRainy";
    default:
      return "weatherSunny";
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
