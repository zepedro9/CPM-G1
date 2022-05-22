import 'package:http/http.dart' as http;

Future<String> getWeather(String country, String city) async {
  final response = await http.get(Uri.http(
    'api.openweathermap.org',
    '/data/2.5/weather',
    {'appid': '36d753d86fa1459a1a5977d63b11ab8c', 
    'q': "$city,$country",
    'units': 'metric'},
  ));

  if (response.statusCode == 200) {
    return response.body;
  } else {
    throw Exception('HTTP failed');
  }
}
