import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);
  final String title;
  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;
  String _androidstring;
  static final platform = const MethodChannel('com.example.flutterchannel');

  void _incrementCounter() {

    setState(() {
       _counter++;
    });

    String data = "Mensaje que se envia a la impresora";

    _sms("55512273", "mensaje pequeno");//  "Test %d completed.\n\n\n"
    _print(data);
  }

  @override
  Widget build(BuildContext context) {
       return Scaffold(
      appBar: AppBar(
         title: Text(widget.title),
      ),
      body: Center(
         child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(
              'Presiona el boton para enviar sms via channel java: ',
            ),


            Text(
              '$_counter',
              style: Theme.of(context).textTheme.display1,
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Increment',
        child: Icon(Icons.add),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }

  Future<void> _print(String data) async{

    var sendMap=<String, dynamic>{
      'ticket': data
    };
    try{
      _androidstring = await platform.invokeMethod('printer', sendMap);
      print(_androidstring);
    }catch(e){
      print(e);
    }
    //return value;
  }

  Future<void> _sms(String number, String message) async{

    var sendMap=<String, dynamic>{
      'number': number,
      'message': message
    };

    try{
      _androidstring = await platform.invokeMethod('sms', sendMap);
      print(_androidstring);

    }catch(e){
      print(e);
    }
    //return value;
  }

}
