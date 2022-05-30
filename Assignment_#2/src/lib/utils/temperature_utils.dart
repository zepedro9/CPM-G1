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

String getWeatherImage(String iconId){
  String url = 'assets/images/';
  iconId = iconId.substring(0, 2);
  switch (iconId) {
    case "01":
      return "${url}sunny.jpg";
    case "02":
    case "04":
      return "${url}partly_cloudy.jpg";
    case "03":
      return "${url}cloudy.jpg";
    case "09":
    case "10":
      return "${url}rain.jpg";
    case "11":
      return "${url}storm.jpg";
    case "13":
      return "${url}snow.jpg";
    case "50":
      return "${url}fog.jpg";
    default:
      return "${url}default.png";
  }
}
