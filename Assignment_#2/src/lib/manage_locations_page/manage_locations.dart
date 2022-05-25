import 'package:flutter/material.dart';
import 'package:wheather_forecast/components/city_checkbox_tile.dart';
import 'package:wheather_forecast/models/city.dart';
import 'package:wheather_forecast/utils/future_builder.dart';
import '../databases/database.dart';

class ManageLocationsPage extends StatefulWidget {
  const ManageLocationsPage({Key? key}) : super(key: key);

  @override
  State<ManageLocationsPage> createState() => _ManageLocationsPageState();
}

class _ManageLocationsPageState extends State<ManageLocationsPage> {
  List<City> cities = [];

  Future fetchCities() async { 
    List<City> response = await DBHelper.instance.getCities();
    setState(() {
      cities = response;
    });
  }

  @override
  void initState() {
    super.initState();
    fetchCities();
  }

  @override
  Widget build(BuildContext context) {
    
    return Scaffold(
        backgroundColor: Color.fromARGB(255, 247, 247, 247),
        appBar: AppBar(
          title: const Text('Manage Locations'),
          backgroundColor: const Color.fromRGBO(32, 82, 209, 1),
        ),
        body: ListView.builder(
          itemCount: cities.length,
          itemBuilder: (BuildContext context, int index) =>
            CityCheckBoxTile(
              title: cities[index].name,
              value: cities[index].isOfInterest,
              onChanged: (bool newValue) {
                print(cities[index].isOfInterest);
                setState(() {
                  cities[index].isOfInterest = newValue;
                });
                print(cities[index].isOfInterest);
              },
            )
        )
    );
  }

  FutureBuilder<List<City>> citiesFutureBuilder() {
    Future<List<City>> citiesFuture = DBHelper.instance.getCities();
    Widget body(data) => citiesListView(data);

    return getCityListFutureBuilder(citiesFuture, body);
  }

  Widget citiesListView(List<City> data) {
    cities = data;
    return ListView.builder(
        itemCount: cities.length,
        itemBuilder: (BuildContext context, int index) => CityCheckBoxTile(
              title: cities[index].name,
              value: cities[index].isOfInterest,
              onChanged: (bool newValue) {
                setState(() {
                  cities[index].isOfInterest = newValue;
                });
              },
            ));
  }
}
