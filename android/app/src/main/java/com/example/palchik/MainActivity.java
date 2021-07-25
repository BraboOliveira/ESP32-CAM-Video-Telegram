package com.example.palchik;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;
import com.digitalpersona.uareu.Fid;
import com.digitalpersona.uareu.Fmd;
import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.ReaderCollection;
import com.digitalpersona.uareu.UareUGlobal;
import com.digitalpersona.uareu.UareUException;
import com.digitalpersona.uareu.Reader.Priority;
import com.digitalpersona.uareu.dpfpddusbhost.DPFPDDUsbHost;
import com.digitalpersona.uareu.dpfpddusbhost.DPFPDDUsbException;
import androidx.annotation.NonNull;
import android.content.Intent;
import android.content.Context;
import android.app.PendingIntent;



public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "jsdaddy.dev/digitalpersona";
    private static final String ACTION_USB_PERMISSION = "com.digitalpersona.uareu.dpfpddusbhost.USB_PERMISSION";

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler(
                        (call, result) -> {
                            Context appContext = getApplicationContext();
                            Reader reader;
                            try {
                                ReaderCollection  readers  = Globals.getInstance().getReaders(appContext);
                                if (readers.isEmpty()) {
                                    result.error("0", "No readers",  "");
                                    return;
                                }
                                reader = readers.get(0);
                            } catch (UareUException e) {
                                result.error("1", "Can't get readers: " + e.getMessage(),  "");
                                return;
                            }

                            if (call.method.equals("checkOrRequestPermissions")) {
                                try {

                                    PendingIntent mPermissionIntent;
                                    mPermissionIntent = PendingIntent.getBroadcast(appContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
                                    boolean checkResult =  DPFPDDUsbHost.DPFPDDUsbCheckAndRequestPermissions(appContext, mPermissionIntent, reader.GetDescription().name);
                                    result.success(checkResult);
                                } catch (DPFPDDUsbException e) {
                                    result.error("2",  "USB exception: " + e.getMessage(),  "");
                                    return;
                                }
                            }

                            if (call.method.equals("openReader")) {
                                try {
                                   reader.Open(Priority.EXCLUSIVE);
                                } catch (UareUException e) {
                                    result.error("3",  "Can't  open reader : " + e.getMessage(),  "");
                                    return;
                                }
                            }

                            if (call.method.equals("closeReader")) {
                                try {
                                    reader.Close();
                                } catch (UareUException e) {
                                    result.error("4",  "Can't  close reader : " + e.getMessage(),  "");
                                    return;
                                }
                            }

                            if (call.method.equals("compareFingers")) {
                                try {
                                    byte[] first = call.argument("firstFinger");
                                    byte[] second = call.argument("secondFinger");
                                    Fmd firstFinger = UareUGlobal.GetImporter().ImportFmd(first, Fmd.Format.ANSI_378_2004, Fmd.Format.ANSI_378_2004);
                                    Fmd secondFinger = UareUGlobal.GetImporter().ImportFmd(second, Fmd.Format.ANSI_378_2004, Fmd.Format.ANSI_378_2004);
                                    int compareResult = UareUGlobal.GetEngine().Compare(firstFinger, 0, secondFinger, 0);
                                    result.success(compareResult);
                                } catch (Exception e) {
                                    result.error("5",  "Compare error : " + e.getMessage(),  "");
                                }

                            }
                            if (call.method.equals("getFingerData")) {
                                try {
                                       int m_DPI = Globals.GetFirstDPI(reader);
                                       Reader.CaptureResult  res = reader.Capture(Fid.Format.ANSI_381_2004, Globals.DefaultImageProcessing, m_DPI, 11111);
                                       Fmd fmd = UareUGlobal.GetEngine().CreateFmd(res.image, Fmd.Format.ANSI_378_2004);
                                       byte[] bytes = fmd.getData();
                                       result.success(bytes);
                                       reader.CancelCapture();

                                } catch (Exception e)
                                {
                                    result.error("6",  "Capture error : " + e.getMessage(),  "");
                                }


                            }
                                // TODO: NotImplemented
                                // result.notImplemented();

                        }
                );

    }
}
