import 'package:flutter/material.dart';

class CityCard extends StatelessWidget {
  // Variables =====
  final String countryName;
  final String cityName;
  final String date;
  final String temperature;
  String get cardTitle => "$cityName - $countryName";

  // Style  =====
  final TextStyle trailingStyle = const TextStyle(fontWeight: FontWeight.w500, fontSize: 18);
  final TextStyle titleStyle = const TextStyle(fontWeight: FontWeight.w500);

  // =====
  const CityCard({
    Key? key,
    required this.countryName,
    required this.cityName,
    required this.date,
    required this.temperature,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Card(
      color: const Color.fromRGBO(233, 238, 250, 1),
      child: ListTile(
          title: Text(cardTitle, style: titleStyle),
          subtitle: Text(date),
          trailing: Text(temperature, style: trailingStyle)),
    );
  }
}
