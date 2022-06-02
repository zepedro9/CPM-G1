import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'dart:convert';

/// Renders a response for an http request.
FutureBuilder<String> getStringFutureBuilder(
    Future<String> response, Widget Function(dynamic) widget) {
  return FutureBuilder<String>(
      future: response,
      builder: (context, snapshot) {
        if (snapshot.hasData) {
          return widget(json.decode(snapshot.data!));
        } else if (snapshot.hasError) {
          Fluttertoast.showToast(
            msg: snapshot.error.toString(),
            toastLength: Toast.LENGTH_SHORT,
            gravity: ToastGravity.CENTER,
            timeInSecForIosWeb: 1,
            backgroundColor: const Color.fromARGB(255, 213, 128, 104),
            textColor: Colors.white,
            fontSize: 16.0
          );
          return const Text("");
        } else {
          return const CircularProgressIndicator();
        }
      });
}
