import 'dart:async';
import 'dart:convert';
import 'package:flutter/services.dart';
import 'package:path/path.dart';
import 'package:sqflite/sqflite.dart';
import 'package:wheather_forecast/models/city.dart';

class DBHelper {
  static const _databaseName = 'cities_database.db';
  static const _databaseVersion = 2;

  DBHelper._privateConstructor();
  static final DBHelper instance = DBHelper._privateConstructor();
  static Database? _database;

  Future<Database> get database async {
    if (_database != null) {
      return _database!;
    }

    _database = await _initDatabase();
    return _database!;
  }

  _initDatabase() async {
    String path = join(await getDatabasesPath(), _databaseName);

    return await openDatabase(path,
        version: _databaseVersion, onCreate: _onCreate);
  }

  Future _onCreate(Database db, int version) async {
    await db.execute('''
          CREATE TABLE city (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            isOfInterest INTEGER NOT NULL
          )
          ''');
    await populate();
  }

  Future<void> populate() async {
    final String response =
        await rootBundle.loadString('assets/data/locations.json');
    final data = await json.decode(response);

    for (int i = 0; i < data.length; i++) {
      City city = City(id: null, name: data[i], isOfInterest: false);
      addCity(city);
    }
  }

  Future<List<City>> getCitiesOfInterest() async {
    Database db = await instance.database;
    const int isOfInterest = 1;

    final List<Map<String, dynamic>> maps = await db.query(
      'city',
      where: 'isOfInterest = ?',
      whereArgs: [isOfInterest],
    );

    // Convert the List<Map<String, dynamic> into a List<City>.
    return List.generate(maps.length, (i) {
      return City(
        id: maps[i]['id'],
        name: maps[i]['name'],
        isOfInterest: maps[i]['isOfInterest'] == 1 ? true : false,
      );
    });
  }

  Future<List<City>> getCities() async {
    Database db = await instance.database;
    final List<Map<String, dynamic>> maps = await db.query('city');

    // Convert the List<Map<String, dynamic> into a List<City>.
    return List.generate(maps.length, (i) {
      return City(
        id: maps[i]['id'],
        name: maps[i]['name'],
        isOfInterest: maps[i]['isOfInterest'] == 1 ? true : false,
      );
    });
  }

  Future<void> addCity(City city) async {
    Database db = await instance.database;
    Map<String, dynamic> data = city.toMap();
    data.remove('id');
    data.update('isOfInterest', (value) => city.isOfInterest ? 1 : 0);

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

  Future<void> updateCities(List<int> citiesOfInterest) async {
    Database db = await instance.database;
    int nArgs = citiesOfInterest.length;

    try {
      await db.transaction((txn) async {
        // Set cities of interest
        await txn.update(
          'city',
          {'isOfInterest': 1},
          where: 'id IN (${List.filled(nArgs, '?').join(',')})',
          whereArgs: citiesOfInterest,
        );

        // Remove cities that are not of interest
        await txn.update(
          'city',
          {'isOfInterest': 0},
          where: 'id NOT IN (${List.filled(nArgs, '?').join(',')})',
          whereArgs: citiesOfInterest,
        );
      });
    } on Exception catch (err) {
      print(err);
    }
  }
}
