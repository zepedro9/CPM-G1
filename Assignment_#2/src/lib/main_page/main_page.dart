import 'package:flutter/material.dart';
import 'package:weather_forecast/manage_locations_page/manage_locations.dart';
import 'package:weather_forecast/utils/future_builder.dart';
import 'package:weather_forecast/utils/temperature_utils.dart';
import 'package:weather_forecast/utils/utils.dart';
import 'package:weather_forecast/main_page/locations_list.dart';
import 'package:material_design_icons_flutter/material_design_icons_flutter.dart';
import 'package:weather_forecast/httpRequests/weather_api.dart';

import '../databases/database.dart';
import '../models/city.dart';
class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key}) : super(key: key);

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const String country = "Portugal";
  String currentCity = "Lisboa";
  List<City> cities = [];
  DBHelper db  = DBHelper.instance; 
 

  Future fetchCities() async {
    List<City> response = await db.getCitiesOfInterest();
    setState(() {
      cities = response;
    });

    if(cities.isNotEmpty){
      setState(() {
        currentCity = cities.where((element) => element.isFavorite == true).first.name;
      });
    } else {
      currentCity = "Lisboa";
    }
  }

  @override
  void initState() { 
    super.initState();
    fetchCities();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: mainView(),
    );
  }

  // Button to manage the cities
  Align optionsButton() {
    return Align(
        alignment: Alignment.topRight,
        child: Padding(
            padding: const EdgeInsets.fromLTRB(0, 30, 8, 0),
            child: IconButton(
              icon: const Icon(Icons.add_rounded, color: Colors.white, size: 35),
              tooltip: 'Manage Locations',
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                      builder: (context) => const ManageLocationsPage()),
                ).then((_) => fetchCities());
              },
            )));
  }

  /// Temperature main display
  Center mainView() {
    Future<String> response = getWeather(country, currentCity);

    Widget body(data) => Stack(children: [
        Positioned.fill(
          child: Image.asset(
            getWeatherImage(data["weather"][0]["icon"]),
            color: const Color.fromRGBO(240, 240, 240, 0.4),
            colorBlendMode: BlendMode.modulate,
            fit: cities.isNotEmpty ? BoxFit.fill : BoxFit.cover,
          ),
        ),
        Stack(
          children: [
            optionsButton(),
            Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                mainTemperature(data),
                if (cities.isNotEmpty) LocationsList(setFavorite: setFavorite, cities: cities),
              ],
            ),
          ],
        )
    ]);

    return Center(
      child: getStringFutureBuilder(response, body),
    );
  }

  Widget mainTemperature(data){
    return Expanded(
      child: Align(
          alignment: Alignment.center,
          child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            getDate(currentCity),
            getTemperatureValue(getWeatherStatus(data["weather"][0]["icon"]),
                data["main"]["temp"].toString()),
            getTemperatureDescription(data["weather"][0]["description"])
          ],
        ),
    ));
  }

  Widget getDate(String city) {
    TextStyle dateStyle = const TextStyle(color: Colors.white, fontSize: 14);
    TextStyle locationStyle =
        const TextStyle(color: Colors.white, fontSize: 16);
    String today = getCurrentDay();

    return Padding(
        padding: const EdgeInsets.fromLTRB(0, 0, 0, 20),
        child: Column(children: <Widget>[
          Padding(
              padding: const EdgeInsets.fromLTRB(0, 0, 0, 4),
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

    return Text(
      weatherDescription.capitalize(),
      style: weatherDescriptionStyle,
    );
  }

  void setFavorite(int id) async {
    await db.updateFavoriteCity(id);
    await fetchCities();
  }
}
