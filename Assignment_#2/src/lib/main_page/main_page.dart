import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:wheather_forecast/utils/temperature_icons.dart';
import 'package:wheather_forecast/main_page/locations_list.dart';
import 'package:material_design_icons_flutter/material_design_icons_flutter.dart';

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key}) : super(key: key);

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
          child: Stack(children: [
        Icon(
          MdiIcons.fromString(getTemperatureIcon(WeatherStatus.SUNNY)),
          size: 480,
          color: Color.fromRGBO(255,255,255,0.1)
        ),
        Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            mainTemperature("25", WeatherStatus.SUNNY),
            const LocationsList(), // TODO: make possible adding information to the list.
          ],
        ),
      ])),
    );
  }

  /// Temperature main display
  Expanded mainTemperature(String temperature, WeatherStatus weatherStatus) {
    return Expanded(
        child: Align(
      alignment: Alignment.center,
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          getDate(),
          getTemperatureValue(weatherStatus, temperature),
          getTemperatureDescription(weatherStatus)
        ],
      ),
    ));
  }

  Widget getDate() {
    TextStyle dateStyle = const TextStyle(color: Colors.white, fontSize: 14);
    return Padding(
        padding: const EdgeInsets.all(20),
        child: Text("Sat, May 20th 2022", style: dateStyle));
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

  Widget getTemperatureDescription(WeatherStatus weatherStatus) {
    TextStyle weatherDescriptionStyle = const TextStyle(
        color: Colors.white, fontSize: 16, fontWeight: FontWeight.w300);
    String temperatureDescription = "<empty>";

    switch (weatherStatus) {
      case WeatherStatus.CLOUDY:
        temperatureDescription = "Partly cloudy";
        break;
      case WeatherStatus.SUNNY:
        temperatureDescription = "A nice sunny day";
        break;
    }

    return Text(
      temperatureDescription,
      style: weatherDescriptionStyle,
    );
  }
}
