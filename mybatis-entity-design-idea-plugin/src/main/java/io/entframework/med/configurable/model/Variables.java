/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.configurable.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义参数 model
 *
 * @author hehaiyangwork@gmail.com
 * @date 2017/05/10
 */
public class Variables implements Serializable {

    private static final long serialVersionUID = -2336608097560710669L;

    /**
     * 自定义参数
     */
    private Map<String, String> params = new HashMap<>();

    public Variables() {
        params.put("author", "[ your name ]");
        params.put("email", "[ your email ]");
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

}
