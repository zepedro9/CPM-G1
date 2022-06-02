import 'package:flutter/material.dart';
import 'dart:convert';

import '../models/city.dart';

/// Renders a response for an http request.
FutureBuilder<String> getStringFutureBuilder(
    Future<String> response, Widget Function(dynamic) widget) {
  return FutureBuilder<String>(
      future: response,
      builder: (context, snapshot) {
        if (snapshot.hasData) {
          return widget(json.decode(snapshot.data!));
        } else if (snapshot.hasError) {
          return Text('${snapshot.error}');
        } else {
          return const CircularProgressIndicator();
        }
      });
}
