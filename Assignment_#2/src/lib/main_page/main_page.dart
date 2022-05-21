import 'package:flutter/material.dart';
import 'package:wheather_forecast/main_page/locations_list.dart';

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key}) : super(key: key);

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: const <Widget>[
            Expanded(child: Text("It's raining man, aleluja",
                style: TextStyle(color: Colors.white))),
               LocationsList(), // TODO: make possible adding information to the list.
          ],
        ),
      ),
    );
  }
}
