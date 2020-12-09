package com.susu.flwx.susu_wx_share_example;
import android.os.Bundle;
import com.susu.flwx.susu_wx_share.SusuWxSharePlugin;

import io.flutter.embedding.android.FlutterActivity;
public class MainActivity extends FlutterActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SusuWxSharePlugin.registerWith(getApplicationContext());
    }
}
