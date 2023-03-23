/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package com.github.hykes.codegen.parser;

import io.entframework.med.model.Entity;
import io.entframework.med.model.EntityImpl;

import java.util.List;

/**
 * Desc: 统一解析入口, 目前提供对sql语句的解析
 * <p>
 *
 * @author chk19940609@gmail.com Created by IceMimosa
 * @date 2017/6/20
 */
public interface Parser {

    /**
     * 将输入的sql语句解析成多个 Entity 对象
     *
     * @param sqls sql 语句, 分号分隔
     * @return {@link EntityImpl}
     */
    List<Entity> parseSQLs(String sqls);

}
