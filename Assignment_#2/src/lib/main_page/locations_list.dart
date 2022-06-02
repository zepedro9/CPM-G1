import 'package:flutter/material.dart';
import 'package:weather_forecast/components/city_card/city_card.dart';
import 'package:weather_forecast/utils/future_builder.dart';
import 'package:weather_forecast/utils/temperature_utils.dart';
import 'package:weather_forecast/utils/utils.dart';
import '../httpRequests/weather_api.dart';
import '../models/city.dart';

class LocationsList extends StatelessWidget {
  final List<City> cities;
  final void Function(int) setFavorite;
  static const String country = "Portugal";

  const LocationsList({
    Key? key,
    required this.cities,
    required this.setFavorite
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    double screenHeight = MediaQuery.of(context).size.height;

    return Center(
        child: Card(
            margin: EdgeInsets.zero, // Removes margin from card.
            shape: const RoundedRectangleBorder(
              borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
            ),
            child: SizedBox(
              height: screenHeight * 0.4,
              width: double.infinity,
              // The builder only loads the visible elements. It increases the performance.
              child: citiesOfInterestListView(),
            )));
  }

  Widget citiesOfInterestListView() {
    return ListView.separated(
      separatorBuilder: (context, index) => const SizedBox(
        height: 10,
      ),
      padding: const EdgeInsets.fromLTRB(15, 25, 15, 2),
      itemCount: cities.length,
      itemBuilder: (BuildContext context, int index) {
        City city = cities[index];
        return cityFutureBuilder(city);
      },
    );
  }

  /// Temperature main display
  FutureBuilder<String> cityFutureBuilder(City city) {
    Future<String> response = getWeather(country, city.name);
    Widget body(data) => CityCard(
        id: city.id!,
        cityName: city.name,
        countryName: country,
        isFavorite: city.isFavorite,
        setFavorite: setFavorite,
        weatherStatus: getWeatherStatus(data["weather"][0]["icon"]),
        description: data["weather"][0]["description"].toString().capitalize(),
        temperature: data["main"]["temp"].toString());

    return getStringFutureBuilder(response, body);
  }

}
