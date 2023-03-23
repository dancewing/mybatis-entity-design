/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.model;

import java.util.List;

public interface Entity extends GeneratorObject {
    Name getName();

    void setName(Name name);

    String getTable();

    void setTable(String table);

    List<Field> getFields();

    void setFields(List<Field> fields);

    String getComment();

    void setComment(String comment);
}
