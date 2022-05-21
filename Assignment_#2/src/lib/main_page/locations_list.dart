import 'package:flutter/material.dart';
import 'package:wheather_forecast/components/city_card/city_card.dart';
import 'package:wheather_forecast/utils/temperature_icons.dart';

class LocationsList extends StatefulWidget {
  const LocationsList({Key? key}) : super(key: key);

  @override
  State<LocationsList> createState() => _LocationsListState();
}

// TODO: Put a different state here.
class _LocationsListState extends State<LocationsList> {

  @override
  Widget build(BuildContext context) {
    // TODO: get information using requests.
    final List<String> countryNames = <String>[
      "Portugal",
      "Portugal",
      "Portugal",
      "Portugal",
      "Portugal"
    ];
    final List<String> cityNames = <String>[
      "Porto",
      "Braga",
      "Aveiro",
      "Viseu",
      "Lisboa"
    ];
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
              child: ListView.separated(
                  // Compacts the item in this element.
                  separatorBuilder: (context, index) => const SizedBox(
                        height: 10,
                      ),
                  padding: const EdgeInsets.fromLTRB(15, 25, 15, 2),
                  itemCount: countryNames.length,
                  itemBuilder: (BuildContext context, int index) {
                    return CityCard(
                        cityName: cityNames.elementAt(index),
                        countryName: countryNames.elementAt(index),
                        weatherStatus: WeatherStatus.SUNNY,
                        date: "May 3th 2022",
                        temperature: "28ºC");
                  }),
            )));
  }
}
