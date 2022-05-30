import 'dart:async';
import 'dart:convert';
import 'package:flutter/services.dart';
import 'package:path/path.dart';
import 'package:sqflite/sqflite.dart';
import 'package:weather_forecast/models/city.dart';

class DBHelper {
  static const _databaseName = 'cities_database.db';
  static const _tableName = 'city';
  static const _databaseVersion = 3;

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
        version: _databaseVersion, 
        onCreate: _onCreate);
  }

  Future _onCreate(Database db, int version) async {
    await db.execute('''
          CREATE TABLE city (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            isOfInterest INTEGER NOT NULL,
            isFavorite INTEGER NOT NULL
          )
          ''');
    await populate(db);
  }

  Future<void> populate(Database db) async {
    //print("populate database");
    final String response =
        await rootBundle.loadString('assets/data/locations.json');
    final data = await json.decode(response);
    
    for (int i = 0; i < data.length; i++) {
      Map<String, dynamic> dataMap = {};
      dataMap.putIfAbsent('name', () => data[i]);
      dataMap.putIfAbsent('isOfInterest', () => 0);
      dataMap.putIfAbsent('isFavorite', () => 0);

      await db.insert(
        _tableName,
        dataMap,
        conflictAlgorithm: ConflictAlgorithm.replace,
      );
    }
  }

  Future<List<City>> getCitiesOfInterest() async {
    Database db = await instance.database;
    const int isOfInterest = 1;

    final List<Map<String, dynamic>> maps = await db.query(
      _tableName,
      where: 'isOfInterest = ?',
      whereArgs: [isOfInterest],
    );

    // Convert the List<Map<String, dynamic> into a List<City>.
    return List.generate(maps.length, (i) {
      return City(
        id: maps[i]['id'],
        name: maps[i]['name'],
        isOfInterest: maps[i]['isOfInterest'] == 1 ? true : false,
        isFavorite: maps[i]['isFavorite'] == 1 ? true : false,
      );
    });
  }

  Future<List<City>> getCities() async {
    Database db = await instance.database;
    final List<Map<String, dynamic>> maps = await db.query(_tableName);

    // Convert the List<Map<String, dynamic> into a List<City>.
    return List.generate(maps.length, (i) {
      return City(
        id: maps[i]['id'],
        name: maps[i]['name'],
        isOfInterest: maps[i]['isOfInterest'] == 1 ? true : false,
        isFavorite: maps[i]['isFavorite'] == 1 ? true : false,
      );
    });
  }

  Future<void> addCity(City city) async {
    Database db = await instance.database;
    Map<String, dynamic> data = city.toMap();
    data.remove('id');
    data.update('isOfInterest', (value) => city.isOfInterest ? 1 : 0);

    try {
      await db.transaction((txn) async {
        bool isFavorite = !await hasFavorite(txn);
        data.update('isFavorite', (value) => isFavorite ? 1 : 0);

        await db.insert(
          _tableName,
          data,
          conflictAlgorithm: ConflictAlgorithm.replace,
        );
      });
    } on Exception catch (err) {
      print(err);
    }
  }

  Future<void> updateFavoriteCity(int id) async {
    Database db = await instance.database;

    try {
      await db.transaction((txn) async {
        // Remove previous favorite
        await removeFavorites(txn);

        // Add new favorite
        await addFavorite(id, txn);
      });
    } on Exception catch (err) {
      print(err);
    }
  }

  Future<void> updateCities(List<int> citiesOfInterest) async {
    Database db = await instance.database;
    int nArgs = citiesOfInterest.length;

    try {
      await db.transaction((txn) async {
        // Set cities of interest
        await txn.update(
          _tableName,
          {'isOfInterest': 1},
          where: 'id IN (${List.filled(nArgs, '?').join(',')})',
          whereArgs: citiesOfInterest,
        );

        // Remove cities that are not of interest
        await txn.update(
          _tableName,
          {'isOfInterest': 0, 'isFavorite': 0},
          where: 'id NOT IN (${List.filled(nArgs, '?').join(',')})',
          whereArgs: citiesOfInterest,
        );

        // Set favorite
        bool missingFavorite = !await hasFavorite(txn);
        if (citiesOfInterest.isNotEmpty && missingFavorite) {
          citiesOfInterest.sort((a, b) => a.compareTo(b));
          await addFavorite(citiesOfInterest[0], txn);
        }
      });
    } on Exception catch (err) {
      print(err);
    }
  }

  Future<bool> hasFavorite(Transaction txn) async {
    const int isFavorite = 1;

    List<Map<String, Object?>> result = await txn.query(
      _tableName,
      where: 'isFavorite = ?',
      whereArgs: [isFavorite],
    );

    return result.isNotEmpty;
  }

  Future<void> addFavorite(int id, Transaction txn) async {
    await txn.update(
      _tableName,
      {'isFavorite': 1},
      where: 'id = ?',
      whereArgs: [id],
    );
  }

  Future<void> removeFavorites(Transaction txn) async {
    const isFavorite = 1;

    await txn.update(
      _tableName,
      {'isFavorite': 0},
      where: 'isFavorite = ?',
      whereArgs: [isFavorite],
    );
  }
}
