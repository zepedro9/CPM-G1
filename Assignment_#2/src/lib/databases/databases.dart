import 'dart:async';
import 'package:path/path.dart';
import 'package:sqflite/sqflite.dart';
import 'package:wheather_forecast/models/city.dart';
import 'package:flutter/widgets.dart'; 

Future<Database> database = openDB();

Future<Database> openDB() async {
  var databasesPath  = await getDatabasesPath();
  String path = join(databasesPath, 'city_databases.db'); 

  return await openDatabase(path,
    onCreate: (db, version) {
      return db.execute(
        'CREATE TABLE city(id INTEGER PRIMARY KEY, name TEXT); CREATE TABLE user_city(id INTEGER)', 
      );
    },
    version: 1,
  );
}

Future<void> addCity(City city) async {
  final db = await database;

  await db.insert(
    'city',
    city.toMap(),
    conflictAlgorithm: ConflictAlgorithm.replace,
  );
}

Future<void> updateCity(City city) async {
  final db = await database;

  await db.update(
    'city',
    city.toMap(),
    where: 'id = ?',
    whereArgs: [city.id],
  );
}