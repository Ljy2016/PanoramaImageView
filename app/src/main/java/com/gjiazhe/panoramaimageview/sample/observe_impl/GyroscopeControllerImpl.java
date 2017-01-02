package com.gjiazhe.panoramaimageview.sample.observe_impl;

import com.gjiazhe.panoramaimageview.sample.observe_interface.GyroscopeControllInterface;
import com.gjiazhe.panoramaimageview.sample.observe_interface.ImgvObserve;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Ljy on 2016/12/29.
 * 邮箱：enjoy_azad@sina.com
 */

public class GyroscopeControllerImpl implements GyroscopeControllInterface {
    private List<ImgvObserve> observaeList;

    public GyroscopeControllerImpl() {
        observaeList = new ArrayList<>();
    }

    @Override
    public void updata(double rotateRadian, int type) {
        for (ImgvObserve imgvObserve : observaeList) {
            imgvObserve.updata(rotateRadian, type);
        }
    }


    @Override
    public void attach(ImgvObserve observer) {
        if (!observaeList.contains(observer)) {
            observaeList.add(observer);
        }
    }

    @Override
    public void detach(ImgvObserve observer) {
        if (observaeList.contains(observer)) {
            observaeList.remove(observer);
        }
    }

    @Override
    public void updata() {

    }
}
