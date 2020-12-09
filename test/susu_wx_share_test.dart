import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:susu_wx_share/susu_wx_share.dart';

void main() {
  const MethodChannel channel = MethodChannel('susu_wx_share');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await SusuWxShare.platformVersion, '42');
  });
}
