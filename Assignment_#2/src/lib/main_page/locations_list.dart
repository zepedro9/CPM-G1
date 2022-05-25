import 'package:flutter/material.dart';
import 'package:wheather_forecast/components/city_card/city_card.dart';
import 'package:wheather_forecast/utils/future_builder.dart';
import 'package:wheather_forecast/utils/temperature_icons.dart';

import '../databases/database.dart';
import '../models/city.dart';

class LocationsList extends StatefulWidget {
  const LocationsList({
    Key? key,
    required this.cities,
    }) : super(key: key);

  final List<City> cities;

  @override
  State<LocationsList> createState() => _LocationsListState();
}

class _LocationsListState extends State<LocationsList> {
  static const String country = "Portugal";

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
      itemCount: widget.cities.length,
      itemBuilder: (BuildContext context, int index) {
        City city = widget.cities[index];
        return CityCard(
          cityName: city.name,
          countryName: country,
          weatherStatus: WeatherStatus.SUNNY, // TODO: fetch temperature from API
          date: "May 3th 2022",
          temperature: "28ºC");
      },
    );
  }
}

