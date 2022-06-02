import 'package:flutter/material.dart';
import 'package:material_design_icons_flutter/material_design_icons_flutter.dart';
import 'package:weather_forecast/utils/temperature_utils.dart';

/// This file contains the functions and methods used to generate the card trailing, that contains:
/// -- Temperature icon
/// -- Temperature value
/// -- Menu arrow icon

Row getTrailing(String temperature, WeatherStatus weatherStatus) {
  const TextStyle trailingStyle = TextStyle(fontWeight: FontWeight.w500, fontSize: 17);
  const EdgeInsets weatherIconPadding = EdgeInsets.fromLTRB(0, 0, 7, 0);

  return Row(mainAxisSize: MainAxisSize.min, children: <Widget>[
    Padding(
        padding: weatherIconPadding,
        child: Icon(MdiIcons.fromString(getTemperatureIcon(weatherStatus)))),
    Text("$temperature ºC", style: trailingStyle),
    Icon(MdiIcons.fromString("menu-right")),
  ]);
}

Row getLeading(int id, bool isFavorite, void Function(int) onPressed) {

  return Row(
    mainAxisSize: MainAxisSize.min,
    mainAxisAlignment: MainAxisAlignment.start, 
    crossAxisAlignment: CrossAxisAlignment.center,
    children: [
    IconButton(
      icon: isFavorite ? const Icon(Icons.favorite_rounded) : const Icon(Icons.favorite_border_rounded), 
      onPressed: () {
        if(!isFavorite){
          onPressed(id);
        }
      },
    )
  ]);
}
