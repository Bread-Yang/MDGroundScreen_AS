package com.mdground.screen.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import com.snail.antifake.deviceid.macaddress.MacAddressUtils;
import com.snail.antifake.jni.EmulatorDetectUtil;

public class DeviceIDUtil {

    private static String fileName = "YiDeGuanScreen.txt";
    private static String xgpushTokenFileName = "YiDeGuanScreenToken.txt";

    public static String getToken(Context applicationContext) {

//        if ( EmulatorDetectUtil.isEmulator(applicationContext))
//            return getMacAddr();
//       else
//           return  MacAddressUtils.getMacAddress(applicationContext);

        return MacAddressUtils.getMacAddress(applicationContext);
    }

    public int getDeviceID(Context mContext) {
        File file = new File(getFile_path(mContext), fileName);

//        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            file = new File(Environment.getExternalStorageState(), fileName);
//        }

        if (!file.exists()) {
            L.e(DeviceIDUtil.this, "文件不存在");
            return -1;
        } else {
            try {

                FileInputStream is = new FileInputStream(file);
                byte[] b = new byte[is.available()];
                is.read(b);
                String result = new String(b);
                L.e(DeviceIDUtil.this, "读取dviceID成功 : deviceID是 : " + result);
                return Integer.valueOf(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return -1;
        }
    }

    public void saveDeviceIDToSDCard(Context mContext,int deviceID) {
        File file = new File(getFile_path(mContext), fileName);

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = new File(Environment.getExternalStorageState(), fileName);
        }

        try {
            OutputStream out;
            out = new FileOutputStream(file, false);
            String content = String.valueOf(deviceID);
            out.write(content.getBytes());
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveXgPushTokenToSDCard(Context mContext,String token) {
        File file = new File(getFile_path(mContext), xgpushTokenFileName);

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = new File(Environment.getExternalStorageState(), xgpushTokenFileName);
        }

        try {
            OutputStream out;
            out = new FileOutputStream(file, false);
            out.write(token.getBytes());
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void createFile(Context mContext) {

        File file = new File(getFile_path(mContext));
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }


    }

    public static String getFile_path(Context context) {

        return context.getFilesDir() + File.separator + "yideguan_files";
    }

    // 获取ip地址
    public static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            //列举
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface.getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {//是否还有元素
                NetworkInterface ni = (NetworkInterface) en_netInterface.nextElement();//得到下一个元素
                Enumeration<InetAddress> en_ip = ni.getInetAddresses();//得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1)
                        break;
                    else
                        ip = null;
                }

                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {

            e.printStackTrace();
        }
        return ip;
    }
    // 获取MAC地址
    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = nif.getHardwareAddress();
                //nif.getInetAddresses();
                if (macBytes == null) {
                    return "";
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    public static String getUniquePsuedoID()
    {
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
        String serial = null;
        try
        {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();

            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        }
        catch (Exception e)
        {
            serial = "serial";
        }
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }


}
