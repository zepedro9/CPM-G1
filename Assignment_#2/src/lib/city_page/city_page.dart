import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:material_design_icons_flutter/material_design_icons_flutter.dart';
import 'package:wheather_forecast/httpRequests/weather_api.dart';
import 'package:wheather_forecast/utils/utils.dart';

import '../utils/future_builder.dart';
import '../utils/temperature_icons.dart';

class CityPage extends StatefulWidget {
  const CityPage({Key? key, required this.country, required this.city}) : super(key: key);

  final String country;
  final String city;

  @override
  State<CityPage> createState() => _CityPageState();
}

class _CityPageState extends State<CityPage> {
  int selected = 0;
  ScrollController scrollBarController = ScrollController();

  @override
  Widget build(BuildContext context) {
    Future<String> data = get5dayForecast(widget.country, widget.city);

    return Scaffold(
      body: Stack(
        children: [
          getWeatherIcon(data),
          Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              getLocation(data),
              Expanded(
                child: Column(
                  children: [
                    getForecast(data),
                    getConditions(data),
                  ],
                )
              )
            ],
          ),
        ]
      ),
    );
  }

  Positioned getWeatherIcon(Future<String> response) {
    Widget body(data) => Icon(
      MdiIcons.fromString(getTemperatureIcon(getWeatherStatus(data["list"][selected]["weather"][0]["icon"]))),
      size: 800,
      color: const Color.fromRGBO(255, 255, 255, 0.1)
    );

    return Positioned(
      bottom: 0,
      right: 0,
      child: getStringFutureBuilder(response, body),
    );
  }

  Expanded getLocation(Future<String> response) {
    TextStyle locationWeatherStyle = const TextStyle(color: Colors.white, fontSize: 20, fontWeight: FontWeight.w300);

    Widget body(data) => Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: <Widget>[
        getTemperature(data["list"][selected]["main"]["temp"].toString()),
        const Padding(padding: EdgeInsetsDirectional.only(bottom: 10)),
        Text(capitalize(data["list"][selected]["weather"][0]["description"]), style: locationWeatherStyle),
        Text(widget.city + ", " + widget.country, style: locationWeatherStyle)
      ],
    );

    return Expanded(
      child: Padding(
        padding: const EdgeInsets.only(top: 30),
        child: getStringFutureBuilder(response, body),
      ),
    );
  }

  Container getForecast(Future<String> response) {
    TextStyle timeStyle = const TextStyle(color: Colors.white70, fontSize: 15);
    TextStyle dateStyle = const TextStyle(color: Colors.white70, fontSize: 12);
    double screenWidth = MediaQuery.of(context).size.width;

    Widget body(data) => RawScrollbar(
      controller: scrollBarController,
      thumbVisibility: true,
      trackVisibility: true,
      interactive: false,
      thumbColor: Colors.lightBlueAccent,
      thickness: 2,
      child: ListView.builder(
        controller: scrollBarController,
        scrollDirection: Axis.horizontal,
        itemCount: data["list"].length,
        itemBuilder: (BuildContext context, int i) {
          return Column(
            children: [
              GestureDetector(
                onTap: () => setSelected(i),
                child: Container(
                  width: screenWidth / 4,
                  color: i == selected ? Colors.blue : Colors.transparent,
                  padding: const EdgeInsets.symmetric(vertical: 10),
                  child: Column(
                    children: [
                      Icon(
                        MdiIcons.fromString(getTemperatureIcon(getWeatherStatus(data["list"][i]["weather"][0]["icon"]))),
                        size: 40,
                        color: Colors.white70,
                      ),
                      getSmallTemperature(data["list"][i]["main"]["temp"].toString()),
                      Text(getTime(data["list"][i]["dt_txt"]), style: timeStyle),
                      Text(getWeekDay(data["list"][i]["dt_txt"]), style: dateStyle),
                    ],
                  ),
                ),
              ),
            ],
          );
        },
      ),
    );

    return Container(
      height: 121,
      color: const Color.fromRGBO(0, 0, 0, 0.75),
      child: getStringFutureBuilder(response, body)
    );
  }

  Expanded getConditions(Future<String> response) {
    TextStyle valueStyle = const TextStyle(color: Colors.white, fontSize: 28);
    TextStyle titleStyle = const TextStyle(color: Colors.white, fontSize: 15);

    Widget body(data) => Column(
      children: [
        Expanded(
          child: Row(
            children: [
              Expanded(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    Text(data["list"][selected]["main"]["humidity"].toString() + "%", style: valueStyle),
                    Text("Humidity", style: titleStyle),
                  ],
                ),
              ),
              Expanded(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    Text(data["list"][selected]["main"]["feels_like"].round().toString() + "º", style: valueStyle),
                    Text("Feels Like", style: titleStyle),
                  ],
                )
              ),
              Expanded(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    Text(data["list"][selected]["main"]["pressure"].toString() + " hPa", style: valueStyle),
                    Text("Pressure", style: titleStyle),
                  ],
                ),
              ),
            ],
          ),
        ),
        Expanded(
          child: Row(
            children: [
              Expanded(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    Text((double.parse(data["list"][selected]["pop"].toString()) * 100).round().toString() + "%", style: valueStyle),
                    Text("Precipitation", style: titleStyle),
                  ],
                ),
              ),
              Expanded(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    Text(data["list"][selected]["clouds"]["all"].toString() + "%", style: valueStyle),
                    Text("Cloudiness", style: titleStyle),
                  ],
                ),
              ),
              Expanded(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    Text(data["list"][selected]["wind"]["speed"].toString() + " m/s", style: valueStyle),
                    Text("Wind speed", style: titleStyle),
                  ],
                ),
              ),
            ],
          ),
        ),
      ],
    );

    return Expanded(
      child: Container(
          color: const Color.fromRGBO(0, 0, 0, 0.8),
          child: getStringFutureBuilder(response, body)
      ),
    );
  }

  Widget getTemperature(String temperature) {
    TextStyle grausStyle = const TextStyle(color: Colors.white, fontSize: 40, fontWeight: FontWeight.bold);
    TextStyle temperatureValueStyle = const TextStyle(
        color: Colors.white, fontWeight: FontWeight.w300, fontSize: 80);

    return Row(
        mainAxisAlignment: MainAxisAlignment.center, // Aligns in the horizontal center
        crossAxisAlignment: CrossAxisAlignment.start, // Aligns in the vertical center
        children: <Widget>[
          Text(double.parse(temperature).round().toString(), style: temperatureValueStyle),
          Text("º", style: grausStyle),
        ]);
  }

  Widget getSmallTemperature(String temperature) {
    TextStyle grausStyle = const TextStyle(color: Colors.white70, fontSize: 14, fontWeight: FontWeight.bold);
    TextStyle temperatureValueStyle = const TextStyle(
        color: Colors.white70, fontWeight: FontWeight.w300, fontSize: 25);

    return Row(
        mainAxisAlignment: MainAxisAlignment.center, // Aligns in the horizontal center
        crossAxisAlignment: CrossAxisAlignment.start, // Aligns in the vertical center
        children: <Widget>[
          Text(double.parse(temperature).round().toString(), style: temperatureValueStyle),
          Text("º", style: grausStyle),
        ]);
  }

  String getTime(String data) {
    String time = data.substring(11, 16);
    if(int.parse(time.substring(0, 2)) < 12) {
      if(time[0] == "0") {
        time = time.substring(1, null);
        if (time[0] == "0") {
          return "12 am";
        } else {
          return time.substring(0, 1) + " am";
        }
      }
      return time.substring(0, 2) + " am";
    } else {
      if(time[0] == "1" && time[1] == "2") return "12 pm";
      return (int.parse(time.substring(0, 2)) - 12).toString() + " pm";
    }
  }

  String getWeekDay(String data) {
    DateTime date = DateTime(
      int.parse(data.substring(0, 4)),
      int.parse(data.substring(5, 7)),
      int.parse(data.substring(8, 10)),
    );
    return DateFormat('EEEE').format(date);
  }

  setSelected(int i) {
    setState(() {
      selected = i;
    });
  }
}