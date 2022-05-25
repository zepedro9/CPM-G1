
import 'package:flutter/material.dart';
import 'package:wheather_forecast/models/city.dart';

class ManageLocationsPage extends StatefulWidget {
  const ManageLocationsPage({Key? key}) : super(key: key);

  @override
  State<ManageLocationsPage> createState() => _ManageLocationsPageState();
}

class _ManageLocationsPageState extends State<ManageLocationsPage> {
  Future<City> response = getWeather(country, currentCity);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Manage Locations'),
        backgroundColor:  Color.fromRGBO(32, 82, 209, 1),
      ),
      body: Center(
          child: Stack(children: [
            ListView.builder(itemBuilder: itemBuilder)
      ])),
    );
  }

}