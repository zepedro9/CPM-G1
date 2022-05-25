import 'package:flutter/material.dart';
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
  List<bool> checkedState = [];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Manage Locations'),
        backgroundColor: const Color.fromRGBO(32, 82, 209, 1),
      ),
      body: citiesFutureBuilder(),
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
      itemCount: data.length,
      itemBuilder: (BuildContext context, int index) {
        City city = data[index];
        return CheckboxListTile(
            title: Text(city.name), 
						value: city.isOfInterest,
						onChanged: (bool? val) {
							setState(() { city.isOfInterest = val!;});
						});
      },
    );
  }

}
