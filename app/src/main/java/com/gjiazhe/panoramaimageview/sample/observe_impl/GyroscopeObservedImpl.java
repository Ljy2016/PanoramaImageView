package com.gjiazhe.panoramaimageview.sample.observe_impl;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.gjiazhe.panoramaimageview.sample.observe_interface.BaseObservedInterface;
import com.gjiazhe.panoramaimageview.sample.observe_interface.GyroscopeControllInterface;


/**
 * 作者：Ljy on 2016/12/28.
 * 邮箱：enjoy_azad@sina.com
 */

public class GyroscopeObservedImpl implements SensorEventListener, BaseObservedInterface<GyroscopeControllInterface> {
    private SensorManager mSensorManager;
    private double rotateRadianX;
    private double rotateRadianY;
    private double rotateRadianZ;
    private double rotateRadian;
    private long lastTimestamp;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private static final int rotateAroundX = 1;
    private static final int rotateAroundY = 2;
    private static final int rotateAroundZ = 3;
    private static int type;
    private GyroscopeControllInterface gyroscopeControllInterface;

    public GyroscopeObservedImpl(GyroscopeControllInterface gyroscopeControllInterface) {
        this.gyroscopeControllInterface = gyroscopeControllInterface;
    }

    public void register(Context context) {
        Log.e("TAG", "register: " + "注册了");
        if (mSensorManager == null) {
            mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        }
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void unregister() {
        Log.e("TAG", "register: " + "解除注册了");
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
            mSensorManager = null;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (lastTimestamp == 0) {
            lastTimestamp = event.timestamp;
            return;
        }
        float angularX = Math.abs(event.values[0]);
        float angularY = Math.abs(event.values[1]);
        float angularZ = Math.abs(event.values[2]);

        final float dT = (event.timestamp - lastTimestamp) * NS2S;
        if (angularX > angularY + angularZ) {  //围绕x轴运动，即手机上下摆动（脑补拿手机打手板的动作）
            rotateRadianX += event.values[0] * dT;
            rotateRadian = rotateRadianX;
            type = rotateAroundX;
        } else if (angularY > angularX + angularZ) {//围绕y轴运动，即手机左右摆动(脑补四驱兄弟里面的“旋风冲锋龙卷风”)
            rotateRadianY += event.values[1] * dT;
            type = rotateAroundY;
            rotateRadian = rotateRadianY;
        } else {//围绕Z轴运动，可以理解为手机放在桌子上转圈
            rotateRadianZ += event.values[2] * dT;
            type = rotateAroundZ;
            rotateRadian = rotateRadianZ;
        }
        updata(gyroscopeControllInterface);
        lastTimestamp = event.timestamp;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void updata(GyroscopeControllInterface gyroscopeControllInterface) {

        gyroscopeControllInterface.updata(rotateRadian, type);
    }
}
