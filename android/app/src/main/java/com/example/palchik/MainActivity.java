package com.example.palchik;
import androidx.annotation.NonNull;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.ReaderCollection;
import com.digitalpersona.uareu.UareUGlobal;
import android.content.Context;
import com.digitalpersona.uareu.UareUException;
public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "samples.flutter.dev/digitalpersona";
    private String deviceName = "device not available.";
    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler(
                        (call, result) -> {
                            // Note: this method is invoked on the main thread.
                            if (call.method.equals("getDeviceName")) {
                                try {
                                    Context applContext = getApplicationContext();
                                    getReaders(applContext);
//                                    if (readers.isEmpty()) {
//                                    } else {
//                                        deviceName = getReaders().get(0).GetDescription().name;
//                                    }
                                } catch (Exception e)
                                {
//                                    result.success("device not available");
                                }
//
//                                if (deviceName == null) {
//                                    result.success("device not available");
//                                } else {
//                                    result.success(deviceName);
//                                }
                                result.success("daw");

                            } else {
                                result.notImplemented();
                            }
                        }
                );
    }
    public void getReaders(Context applContext) throws UareUException
    {
        try {
            ReaderCollection m_collection;
            m_collection = UareUGlobal.GetReaderCollection(applContext);
            m_collection.GetReaders();
        } catch (UareUException e1) {
            // TODO Auto-generated catch block
            return;
        }
//        ReaderCollection readers = UareUGlobal.GetReaderCollection(applContext);
//        readers.GetReaders();
//        return readers;
    }
}