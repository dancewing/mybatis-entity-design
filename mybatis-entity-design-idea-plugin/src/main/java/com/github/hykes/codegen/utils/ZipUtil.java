/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package com.github.hykes.codegen.utils;

import com.intellij.openapi.diagnostic.Logger;
import org.apache.commons.io.IOUtils;

import java.io.FileWriter;

/**
 * @author hehaiyangwork@gmail.com
 * @date 2017/12/30
 */
public class ZipUtil {

    private static final Logger LOGGER = Logger.getInstance(ZipUtil.class);

    public static void export(String content, String targetPath) {
        try {
            FileWriter writer = new FileWriter(targetPath);
            IOUtils.write(content, writer);
            writer.flush();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
