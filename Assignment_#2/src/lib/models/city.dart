class City {
  int? id;
  final String name;
  bool isOfInterest;
  bool isFavorite;

  City({
    required this.id,
    required this.name,
    required this.isOfInterest,
    required this.isFavorite,
  });

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'name': name,
      'isOfInterest': isOfInterest,
      'isFavorite': isFavorite,
    };
  }

  @override
  String toString() {
    return 'City{id: $id, name: $name, isOfInterest: $isOfInterest, isFavorite: $isFavorite}';
  }
}
