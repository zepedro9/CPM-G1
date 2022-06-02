import 'package:http/http.dart' as http;

Future<String> getWeather(String country, String city) async {
  try {
    final response = await http.get(Uri.http(
      'api.openweathermap.org',
      '/data/2.5/weather',
      {
        'appid': '36d753d86fa1459a1a5977d63b11ab8c',
        'q': "$city,$country",
        'units': 'metric'
      },
    ));

    if (response.statusCode == 200) {
      return response.body;
    } else {
      return Future<String>.error('Error fetching API data');
    }
  } catch (error) {
    return Future<String>.error('Error fetching API data');
  }
}

Future<String> get5dayForecast(String country, String city) async {
  try {
    final response = await http.get(Uri.http(
      'api.openweathermap.org',
      '/data/2.5/forecast',
      {
        'appid': '36d753d86fa1459a1a5977d63b11ab8c',
        'q': "$city,$country",
        'units': 'metric'
      },
    ));

    if (response.statusCode == 200) {
      return response.body;
    } else {
      return Future<String>.error('Error fetching API data');
    }
  } catch (error) {
    return Future<String>.error('Error fetching API data');
  }
}
