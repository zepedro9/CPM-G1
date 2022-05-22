import 'package:flutter/material.dart';
import 'package:wheather_forecast/utils/future_builder.dart';
import 'package:wheather_forecast/utils/temperature_icons.dart';
import 'package:wheather_forecast/utils/utils.dart';
import 'package:wheather_forecast/main_page/locations_list.dart';
import 'package:material_design_icons_flutter/material_design_icons_flutter.dart';
import 'package:wheather_forecast/httpRequests/weather_api.dart';

const String country = "Portugal";

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key}) : super(key: key);

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  String currentCity = "Porto";

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
          child: Stack(children: [
        /*Icon(
          MdiIcons.fromString(getTemperatureIcon(WeatherStatus.SUNNY)),
          size: 480,
          color: Color.fromRGBO(255,255,255,0.1)
        ),*/
        Image.asset(
          'assets/images/sunny.png',
          color: const Color.fromRGBO(255, 255, 255, 0.4),
          colorBlendMode: BlendMode.modulate,
        ),
        Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            mainTemperature(),
            const LocationsList(), // TODO: make possible adding information to the list.
          ],
        ),
      ])),
    );
  }

  /// Temperature main display
  Expanded mainTemperature() {
    Future<String> response = getWeather(country, currentCity);
    Widget body(data) => Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            getDate(currentCity),
            getTemperatureValue(
                getWeatherStatus(data["weather"][0]["icon"]), data["main"]["temp"].toString()),
            getTemperatureDescription(data["weather"][0]["description"])
          ],
        );

    return Expanded(
      child: Align(
          alignment: Alignment.center, child: getFutureBuilder(response, body)),
    );
  }

  Widget getDate(String city) {
    TextStyle dateStyle = const TextStyle(color: Colors.white, fontSize: 14);
    TextStyle locationStyle = const TextStyle(color: Colors.white, fontSize: 16);
    String today = getCurrentDay();

    return Padding(
        padding: const EdgeInsets.all(20),
        child: Column(children: <Widget>[
          Padding(
              padding: const EdgeInsets.all(4),
              child: Text("$city, $country", style: locationStyle)),
          Text(today, style: dateStyle),
        ]));
  }

  Widget getTemperatureValue(WeatherStatus weatherStatus, String temperature) {
    TextStyle grausStyle = const TextStyle(color: Colors.white, fontSize: 20);
    TextStyle temperatureValueStyle = const TextStyle(
        color: Colors.white, fontWeight: FontWeight.bold, fontSize: 55);

    return Row(
        mainAxisAlignment:
            MainAxisAlignment.center, // Aligns in the horizontal center
        crossAxisAlignment:
            CrossAxisAlignment.start, // Aligns in the vertical center
        children: <Widget>[
          Padding(
              padding: const EdgeInsets.fromLTRB(0, 0, 7, 0),
              child: Icon(
                  MdiIcons.fromString(getTemperatureIcon(weatherStatus)),
                  color: Colors.white,
                  size: 55)),
          Text(temperature, style: temperatureValueStyle),
          Text("ºC", style: grausStyle),
        ]);
  }

  Widget getTemperatureDescription(String weatherDescription) {
    TextStyle weatherDescriptionStyle = const TextStyle(
        color: Colors.white, fontSize: 16, fontWeight: FontWeight.w300);
    String temperatureDescription = "<empty>";

    return Text(
      weatherDescription.capitalize(),
      style: weatherDescriptionStyle,
    );
  }
}
