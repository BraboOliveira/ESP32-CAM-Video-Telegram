import 'dart:developer';
import 'dart:typed_data';

import 'package:flutter/services.dart';

MethodChannel platform = const MethodChannel('jsdaddy.dev/digitalpersona');

class DigitalPersona {
  static Future<bool> checkOrRequestPermissions() async {
    return await platform
        .invokeMethod('checkOrRequestPermissions')
        .catchError(printEr);
  }

  static Future<Uint8List> getFingerData() async {
    return await platform.invokeMethod('getFingerData').catchError(printEr);
  }

  static Future<int> compareFingers(
      Uint8List firstFinger, Uint8List secondFinger) async {
    return await platform.invokeMethod('compareFingers', {
      'firstFinger': firstFinger,
      'secondFinger': secondFinger
    }).catchError(printEr);
  }

  static Future<void> openReader() async {
    await platform.invokeMethod('openReader').catchError(printEr);
  }

  static Future<void> closeReader() async {
    await platform.invokeMethod('closeReader').catchError(printEr);
  }
}

void printEr(dynamic e) {
  log(e?.toString());
}
