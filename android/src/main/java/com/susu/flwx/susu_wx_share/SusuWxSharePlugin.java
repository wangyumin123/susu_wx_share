package com.susu.flwx.susu_wx_share;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import io.flutter.Log;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * SusuWxSharePlugin
 */
public class SusuWxSharePlugin implements FlutterPlugin,  MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity

    //FlutterPlugin
    private MethodChannel channel;
    private IWXAPI wxApi;
//    private static Registrar registrars;
    private static  Context contexts;
    private static final int THUMB_SIZE = 150;
    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        Log.i("wyumerss",result.toString());
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        }else if (call.method.equals("getPlatformVersion2")) {
            registerToWX(call,result);
        }else if (call.method.equals("getPlatformVersion3")) {
            shareImage(call,result);
        }
        else {
            result.notImplemented();
        }
    }




    public static void registerWith(Context context) {
        contexts=context;
    }



//    public static void registerWith(PluginRegistry registry) {
//        String CHANNEL = "susu_wx_share";
//        PluginRegistry.Registrar registrar = registry.registrarFor(CHANNEL);
//        MethodChannel methodChannel = new MethodChannel(registrar.messenger(), CHANNEL);
//        registrars=registrar;
//        SusuWxSharePlugin myFlutterPlugin = new SusuWxSharePlugin();
//        methodChannel.setMethodCallHandler(myFlutterPlugin);
//    }
//    public static void registerWith(Registrar registrar) {
//        final MethodChannel channel = new MethodChannel(registrar.messenger(), "sy_flutter_wechat");
//        final SusuWxSharePlugin plugin = new SusuWxSharePlugin(registrar);
//        channel.setMethodCallHandler(plugin);
//    }

//    public SusuWxSharePlugin(Registrar registrar){
//        this.registrar = registrar;
//    }



    //注册微信app id
    private void registerToWX(MethodCall call, Result result){
        Log.i("wyumers","111111111111111");
        String appId ="wx32b6e99c4a80f000";
        wxApi = WXAPIFactory.createWXAPI(contexts,appId,true);
        boolean res =wxApi.registerApp(appId);
        StateManager.setApi(wxApi);
        result.success(appId);
    }
    private void shareImage(final MethodCall call, final Result result){

        Log.i("wyumers","222222222222222");
        final String imageUrl = call.argument("imageUrl");
        final String shareType ="timeline";
        new Thread(new Runnable() {
            WXMediaMessage msg = new WXMediaMessage();
            @Override
            public void run() {
                try {
                    Bitmap bmp = BitmapFactory.decodeStream(new URL("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1316161088,1431610930&fm=26&gp=0.jpg").openStream());
                    WXImageObject imageObject = new WXImageObject(bmp);
                    Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                    msg.mediaObject = imageObject;
                    bmp.recycle();
                    msg.thumbData = SusuWxSharePlugin.bmpToByteArray(thumbBmp, true);
                }catch (IOException e){
                    e.printStackTrace();
                }
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.scene = _convertShareType(shareType);
                req.message = msg;
                Log.i("wyumerss",wxApi+"--------sss");
                boolean res = wxApi.sendReq(req);
                result.success(res);
            }
        }
        ).start();
    }
    private static int _convertShareType(String shareType){
        switch (shareType){
            case "session":
                return SendMessageToWX.Req.WXSceneSession;
            case "timeline":
                return SendMessageToWX.Req.WXSceneTimeline;
            case "favorite":
                return SendMessageToWX.Req.WXSceneFavorite;
            default:
                return SendMessageToWX.Req.WXSceneSession;
        }
    }
    private static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "susu_wx_share");
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }
}
