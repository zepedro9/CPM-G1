import 'package:flutter/material.dart';

class CityCheckBoxTile extends StatelessWidget{
  final String title;
  final bool value;
  final ValueChanged<bool> onChanged;

  const CityCheckBoxTile({
    Key? key,
    required this.title,
    required this.value,
    required this.onChanged,
  }) : super(key: key);
  
  @override
  Widget build(BuildContext context) {
    return CheckboxListTile(
        dense: true,
        activeColor: const Color.fromRGBO(32, 82, 209, 1),
        title: Text(
          title,
          style: const TextStyle(
              fontSize: 15, fontWeight: FontWeight.w500),
        ),
        value: value,
        onChanged: (bool? val) {
          onChanged(val!);
        });
  }
  
}