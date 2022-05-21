import 'package:flutter/material.dart';
import 'package:wheather_forecast/components/city_card.dart';

class LocationsList extends StatefulWidget {
  const LocationsList({Key? key}) : super(key: key);

  @override
  State<LocationsList> createState() => _LocationsListState();
}

class _LocationsListState extends State<LocationsList> {
  int _counter = 0;

  void _incrementCounter() {
    setState(() {
      // This call to setState tells the Flutter framework that something has
      // changed in this State, which causes it to rerun the build method below
      // so that the display can reflect the updated values. If we changed
      // _counter without calling setState(), then the build method would not be
      // called again, and so nothing would appear to happen.
      _counter++;
    });
  }

  @override
  Widget build(BuildContext context) {
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
                        date: "May 3th 2022",
                        temperature: "28ºC");
                  }),
            )));
  }
}
