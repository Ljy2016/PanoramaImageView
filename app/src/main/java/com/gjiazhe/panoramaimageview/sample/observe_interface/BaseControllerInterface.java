package com.gjiazhe.panoramaimageview.sample.observe_interface;

/**
 * 作者：Ljy on 2016/12/29.
 * 邮箱：enjoy_azad@sina.com
 */

public interface BaseControllerInterface<T> {
    void attach(T t);

    void detach(T t);

    void updata();
}
