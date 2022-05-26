import 'package:flutter/material.dart';
import 'package:wheather_forecast/utils/temperature_icons.dart';
import 'package:wheather_forecast/components/city_card/weather_trailing_leading.dart';

class CityCard extends StatelessWidget {
  final String countryName;
  final int id;
  final String cityName;
  final String description;
  final WeatherStatus weatherStatus;
  final String temperature;
  final bool isFavorite;
  final void Function(int) setFavorite;

  String get cardTitle => "$cityName - $countryName";
  final TextStyle titleStyle = const TextStyle(fontWeight: FontWeight.w500);

  const CityCard({
    Key? key,
    required this.id,
    required this.countryName,
    required this.cityName,
    required this.description,
    required this.weatherStatus,
    required this.temperature,
    required this.isFavorite,
    required this.setFavorite,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Card(
        color: const Color.fromRGBO(233, 238, 250, 1),
        child: ListTile(
            leading: getLeading(id, isFavorite, setFavorite),
            title: Text(cardTitle, style: titleStyle),
            subtitle: Text(description),
            trailing: getTrailing(temperature, weatherStatus)));
  }
}
