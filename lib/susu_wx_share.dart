
import 'dart:async';

import 'package:flutter/services.dart';

class SusuWxShare {
  static const MethodChannel _channel =
      const MethodChannel('susu_wx_share');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
  static Future<String> get platformVersion2 async {
    final String version = await _channel.invokeMethod('getPlatformVersion2');
    return version;
  }

  static Future<String> get platformVersion3 async {
    final String version = await _channel.invokeMethod('getPlatformVersion3');
    return version;
  }

  // /// shareImage
  // static Future<bool> shareImage() async {
  //   return await _channel.invokeMethod('shareImage');
  // }
}
