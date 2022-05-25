import 'dart:async';
import 'dart:convert';
import 'package:flutter/services.dart';
import 'package:path/path.dart';
import 'package:sqflite/sqflite.dart';
import 'package:wheather_forecast/models/city.dart';


class DBHelper {
  static const _databaseName = 'city_databases.db';
  static const _databaseVersion = 1;

  DBHelper._privateConstructor();
  static final DBHelper instance = DBHelper._privateConstructor();
  static Database? _database;

  Future<Database> get database async {
    if (_database != null) return _database!;
    // lazily instantiate the db the first time it is accessed
    _database = await _initDatabase();
    return _database!;
  }

  _initDatabase() async {
    String path = join(await getDatabasesPath(), _databaseName);

    return await openDatabase(path,
        version: _databaseVersion,
        onCreate: _onCreate);
  }

  Future _onCreate(Database db, int version) async {
    await db.execute('''
          CREATE TABLE city (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            isOfInterest BOOLEAN NOT NULL
          )
          ''');
    populate();
  }

  Future<void> populate() async {
    final String response = await rootBundle.loadString('assets/data/locations.json');
    final data = await json.decode(response);

    for(int i = 0; i < data.locations.length; i++){
      City city = City(id: null, name: data.locations[i].name, isOfInterest: false);
      addCity(city);
    }
  }

  Future<List<Map<String, dynamic>>> getCities() async {
    Database db = await instance.database;
    return await db.query('city');
  }

  Future<void> addCity(City city) async {
    Database db = await instance.database;
    Map<String, dynamic> data = city.toMap();
    data.remove('id');

    await db.insert(
      'city',
      data,
      conflictAlgorithm: ConflictAlgorithm.replace,
    );
  }

  Future<void> updateCity(City city) async {
    Database db = await instance.database;

    await db.update(
      'city',
      city.toMap(),
      where: 'id = ?',
      whereArgs: [city.id],
    );
  }

}
