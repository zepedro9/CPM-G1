import 'package:flutter/material.dart';
import 'package:weather_forecast/components/city_checkbox_tile.dart';
import 'package:weather_forecast/models/city.dart';
import '../databases/database.dart';

class ManageLocationsPage extends StatefulWidget {
  const ManageLocationsPage({Key? key}) : super(key: key);

  @override
  State<ManageLocationsPage> createState() => _ManageLocationsPageState();
}

class _ManageLocationsPageState extends State<ManageLocationsPage> {
  List<City> cities = [];
  DBHelper db = DBHelper.instance;

  Future fetchCities() async {
    List<City> response = await db.getCities();
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
        backgroundColor: const Color.fromARGB(255, 247, 247, 247),
        appBar: AppBar(
          title: const Text('Manage Locations'),
          backgroundColor: const Color.fromRGBO(32, 82, 209, 1),
        ),
        floatingActionButton: saveButton(),
        floatingActionButtonLocation: FloatingActionButtonLocation.centerFloat,
        body: citiesListView()
        );
  }

  Widget citiesListView() {
    return ListView.separated(
        itemCount: cities.length,
        separatorBuilder: (context, index) => const Divider(
        thickness: 1,
        color: Color.fromARGB(255, 192, 192, 192),
      ),
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

  Widget saveButton(){
    return FloatingActionButton.extended(
      elevation: 4.0,
      icon: const Icon(Icons.save),
      label: const Text('Save'),
      onPressed: () {saveCities();},
      backgroundColor: const Color.fromRGBO(32, 82, 209, 1),
    );
  }
  
  Future<void> saveCities() async {
    List<int> checked = cities.where(
      (element) => element.isOfInterest == true
    ).toList().map((e) => e.id!).toList();
    await db.updateCities(checked);
    Navigator.pop(context);
  }
}
