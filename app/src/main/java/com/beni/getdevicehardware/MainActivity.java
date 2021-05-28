package com.beni.getdevicehardware;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActionBar ToastUtils;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView text1 = findViewById(R.id.txt_scroll);
                text1.setMovementMethod(ScrollingMovementMethod.getInstance());
                String aa = getTelephonyInfo();
                String bb = getBuildHardwareInfo();
                String cc = "getTelephonyInfo！！！！！\n" + aa + "getBuildHardwareInfo！！！！！\n" + bb;
                text1.setText(cc);
                System.out.println(cc);
            }
        });

        //
        requestPermission(Manifest.permission.READ_PHONE_STATE);
        requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }

    private void requestPermission(String permission) {
        //先判断这个权限有没有被授权
        int hasPermission = ActivityCompat.checkSelfPermission(this, permission);
        //如果没有被授权，那么就要请求用户授权
        //shouldShowRequestPermissionRationale()在第一次授权弹框之前或用户点击永不提醒之后 会返回false，其他情况都返回true
        boolean shouldShowUI = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        SharedPreferences sp = getSharedPreferences("LOCAL_DATAS", MODE_PRIVATE);
        boolean is_first_get_permission = sp.getBoolean("is_first_request_permission", true);
        //系统会自动弹框请求权限
        ActivityCompat.requestPermissions(this, new String[]{permission}, 1);
        sp.edit().putBoolean("is_first_request_permission",false).apply();
    }

    public String getBuildHardwareInfo(){
        Field[] fields = Build.class.getDeclaredFields();
        StringBuilder sbBuilder = new StringBuilder();
        for(Field field:fields){
            field.setAccessible(true);
            try {
                sbBuilder.append(field.getName()+":"+field.get(null).toString() + "\n");
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return sbBuilder.toString();
    }

    private String getTelephonyInfo() {
        StringBuilder sbBuilder = new StringBuilder();
        TelephonyManager tm =(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

        //手机串号:GSM手机的 IMEI 和 CDMA手机的 MEID.
        String deviceID =tm.getDeviceId();
        sbBuilder.append("getDeviceId:"+deviceID+"\n");

        //手机号(有些手机号无法获取，是因为运营商在SIM中没有写入手机号)
        String tel =tm.getLine1Number();
        sbBuilder.append("getLine1Number:"+tel+"\n");

        //获取手机SIM卡的序列号
        String simSerialNumber =tm.getSimSerialNumber();
        sbBuilder.append("getSimSerialNumber:"+simSerialNumber+"\n");

        //获取客户id，在gsm中是imsi号
        String subscriberId=tm.getSubscriberId();
        sbBuilder.append("getSubscriberId:"+subscriberId+"\n");

        //运营商名称,注意：仅当用户已在网络注册时有效,在CDMA网络中结果也许不可靠
        String networkoperatorName = tm.getNetworkOperatorName();
        sbBuilder.append("getNetworkOperatorName:"+networkoperatorName+"\n");

        //取得和语音邮件相关的标签，即为识别符
        String voiceMail =tm.getVoiceMailAlphaTag();
        sbBuilder.append("getVoiceMailAlphaTag:"+voiceMail+"\n");

        //获取语音邮件号码：
        String voiceMailNumber =tm.getVoiceMailNumber();
        sbBuilder.append("getVoiceMailNumber:"+voiceMailNumber+"\n");

        //获取ISO国家码，相当于提供SIM卡的国家码。
        String simCountryIso =tm.getSimCountryIso();
        sbBuilder.append("getSimCountryIso:"+simCountryIso+"\n");

        /**
         * 电话状态：
         * 1.tm.CALL_STATE_IDLE=0         无活动
         *2.tm.CALL_STATE_RINGING=1  响铃
         *3.tm.CALL_STATE_OFFHOOK=2  摘机
         */
        int callState =tm.getCallState();
        sbBuilder.append("getCallState:"+callState+"\n");

        /**
         * 设备的软件版本号：
         * 例如：the IMEI/SV(softwareversion) for GSM phones.
         * Return null if the software versionis not available.
         */
        String devicesoftware =tm.getDeviceSoftwareVersion();
        sbBuilder.append("getDeviceSoftwareVersion:"+devicesoftware+"\n");

        /**
         * 获取ISO标准的国家码，即国际长途区号。
         * 注意：仅当用户已在网络注册后有效。
         *      在CDMA网络中结果也许不可靠。
         */
        String networkCountry =tm.getNetworkCountryIso();
        sbBuilder.append("getNetworkCountryIso:"+networkCountry+"\n");

        /**
         * MCC+MNC(mobile country code +mobile network code)
         * 注意：仅当用户已在网络注册时有效。
         *    在CDMA网络中结果也许不可靠。
         */
        String networkoperator = tm.getNetworkOperator();
        sbBuilder.append("getNetworkOperator:"+networkoperator+"\n");

        /**
         * 当前使用的网络类型：
         * 例如：NETWORK_TYPE_UNKNOWN  网络类型未知  0
         NETWORK_TYPE_GPRS    GPRS网络  1
         NETWORK_TYPE_EDGE    EDGE网络  2
         NETWORK_TYPE_UMTS    UMTS网络  3
         NETWORK_TYPE_HSDPA    HSDPA网络  8
         NETWORK_TYPE_HSUPA    HSUPA网络  9
         NETWORK_TYPE_HSPA    HSPA网络  10
         NETWORK_TYPE_CDMA    CDMA网络,IS95A 或 IS95B.  4
         NETWORK_TYPE_EVDO_0   EVDO网络, revision 0.  5
         NETWORK_TYPE_EVDO_A   EVDO网络, revision A.  6
         NETWORK_TYPE_1xRTT   1xRTT网络  7
         */
        int netWorkType =tm.getNetworkType();
        sbBuilder.append("getNetworkType:"+netWorkType+"\n");

        /**
         * 手机类型：
         * 例如：PHONE_TYPE_NONE  无信号
         PHONE_TYPE_GSM   GSM信号
         PHONE_TYPE_CDMA  CDMA信号
         */
        int phoneType = tm.getPhoneType();
        sbBuilder.append("getPhoneType:"+phoneType+"\n");

        /**
         * 获取SIM卡提供的移动国家码和移动网络码.5或6位的十进制数字.
         * SIM卡的状态必须是 SIM_STATE_READY(使用getSimState()判断).
         */
        String simOperator =tm.getSimOperator();
        sbBuilder.append("getSimOperator:"+simOperator+"\n");

        /**
         * 服务商名称：
         * 例如：中国移动、联通
         * SIM卡的状态必须是 SIM_STATE_READY(使用getSimState()判断).
         */
        String simOperatorName =tm.getSimOperatorName();
        sbBuilder.append("getSimOperatorName:"+simOperatorName+"\n");

        /**
         * SIM的状态信息：
         * SIM_STATE_UNKNOWN         未知状态 0
         SIM_STATE_ABSENT          没插卡 1
         SIM_STATE_PIN_REQUIRED     锁定状态，需要用户的PIN码解锁 2
         SIM_STATE_PUK_REQUIRED    锁定状态，需要用户的PUK码解锁 3
         SIM_STATE_NETWORK_LOCKED   锁定状态，需要网络的PIN码解锁 4
         SIM_STATE_READY           就绪状态 5
         */
        int simState =tm.getSimState();
        sbBuilder.append("getSimState:"+simState+"\n");

        /**
         * ICC卡是否存在
         */
        boolean hasIccCard=tm.hasIccCard();
        sbBuilder.append("hasIccCard:"+hasIccCard+"\n");

        /**
         * 是否漫游:
         * (在GSM用途下)
         */
        boolean isRoaming =tm.isNetworkRoaming();
        sbBuilder.append("isNetworkRoaming:"+isRoaming+"\n");


        /**
         * 获取数据连接状态
         */
        int dataActivty =tm.getDataActivity();
        sbBuilder.append("getDataActivity:"+dataActivty+"\n");

        return sbBuilder.toString();
    }
}