import 'package:intl/intl.dart';

extension StringCasingExtension on String {
  String capitalize() => length > 0 ?'${this[0].toUpperCase()}${substring(1).toLowerCase()}':'';
}

String getCurrentDay(){
  var now  = DateTime.now(); 
  return DateFormat('yMMMEd').format(now);
}