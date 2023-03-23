/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package com.github.hykes.codegen.parser;

import com.github.hykes.codegen.utils.StringUtils;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import io.entframework.med.model.*;
import io.entframework.med.util.TextUtil;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import org.apache.commons.lang.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc: 默认的解析器, 使用 JSqlParser 进行解析.
 * <p>
 * 不提供DB连接解析 !!!
 *
 * @author chk19940609@gmail.com
 * @date 2017/6/20
 */
public class DefaultParser extends AbstractParser {

    private static final Logger LOGGER = Logger.getInstance(DefaultParser.class);

    @Override
    public List<Entity> parseSQLs(String sqls) {
        if (StringUtils.isBlank(sqls)) {
            return null;
        }

        List<Entity> result = new ArrayList<>();
        // 解析sql语句
        try {
            List<Statement> statements = CCJSqlParserUtil.parseStatements(sqls).getStatements();
            if (statements == null || statements.isEmpty()) {
                throw new RuntimeException("Nothing in parse !!!");
            }
            List<CreateTable> createTables = new ArrayList<>();
            for (Statement statement : statements) {
                if (statement instanceof CreateTable) {
                    createTables.add((CreateTable) statement);
                }
            }
            if (createTables.isEmpty()) {
                throw new RuntimeException("Only support create table statement !!!");
            }

            for (CreateTable createTable : createTables) {
                List<Field> fields = new ArrayList<>();
                EntityImpl entity = new EntityImpl(fields);
                entity.setTable(removeQuotes(createTable.getTable().getName()));
                entity.setName(Name.of(TextUtil.underlineToCamel(entity.getTable(), true)));
                createTable.getColumnDefinitions().forEach(it -> {
                    FieldImpl field = new FieldImpl();
                    // 字段名称
                    String columnName = removeQuotes(it.getColumnName());
                    // 同时设置了 FieldName
                    field.setColumn(columnName);
                    field.setName(TextUtil.underlineToCamel(columnName));

                    // 字段类型
                    ColDataType colDataType = it.getColDataType();
                    // 同时设置了字段类型
                    field.setColumnType(colDataType.getDataType());
                    String length = firstOrNull(colDataType.getArgumentsStringList());
                    field.setLength(StringUtil.isEmptyOrSpaces(length) ? -1 : NumberUtils.toInt(length, -1));

                    // comment注释
                    field.setComment(getColumnComment(it.getColumnSpecs()));

                    fields.add(field);
                });

                if (entity.getFields() != null && !entity.getFields().isEmpty()) {
                    result.add(entity);
                }
            }
            return result;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取字段的注释
     */
    private String getColumnComment(List<String> specs) {
        if (specs == null || specs.isEmpty()) {
            return null;
        }
        for (int size = specs.size(), i = 0; i < size; i++) {
            String spec = specs.get(i);
            if ("COMMENT".equals(spec.toUpperCase())) {
                // 下一个为comment内容, 查看是否越界
                if (i + 1 >= size) {
                    return null;
                }
                return removeQuotes(specs.get(i + 1));
            }
        }
        return null;
    }

}
