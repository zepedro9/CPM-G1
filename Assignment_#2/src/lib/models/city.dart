class City {
  final int? id;
  final String name;
  final bool isOfInterest;

  const City({
    required this.id,
    required this.name,
    required this.isOfInterest
  });

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'name': name,
      'isOfInterest': isOfInterest,
    };
  }

  // Implement toString to make it easier to see information about
  // each dog when using the print statement.
  @override
  String toString() {
    return 'City{id: $id, name: $name, isOfInterest: $isOfInterest}';
  }
}
