package com.example.flutterchannel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import java.util.Locale;
import java.util.Map;
import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;



public class MainActivity extends FlutterActivity {
    public static final String CHANNEL = "com.example.flutterchannel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 0);

            }
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

            }

        }


        new MethodChannel(getFlutterView(), CHANNEL).setMethodCallHandler(new MethodChannel.MethodCallHandler() {
            @Override
            public void onMethodCall(MethodCall call, MethodChannel.Result result) {
                Map<String, Object> argument = call.arguments();
                if(call.method.equals("printer")){
                    String strTicket = (String) argument.get("ticket");
                    String textToPrint = String.format(Locale.ROOT, strTicket, 1);
                    String url = "rawbt:" + textToPrint;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    sendToPrint(intent);
                    result.success("Java printer...");
                }

                if(call.method.equals("sms")){
                    String number = (String) argument.get("number");
                    String message = (String) argument.get("message");
                    sendTosms(number, message);

                    result.success("Java sms...");
                }

            }
        });
    }

    protected void sendToPrint(Intent intent) {
        final String appPackageName = "ru.a402d.rawbtprinter";
        intent.setPackage(appPackageName);
        startActivity(intent);
    }

    protected void sendTosms(String number, String message){
        SmsManager smsmanager = SmsManager.getDefault();
        smsmanager.sendTextMessage(number, null, message, null, null);
    }
}