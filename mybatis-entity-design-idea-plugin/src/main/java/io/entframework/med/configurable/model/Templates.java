/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.configurable.model;

import com.intellij.openapi.util.text.StringUtil;
import io.entframework.med.model.CodeGroup;
import io.entframework.med.model.CodeTemplate;

import java.io.Serializable;
import java.util.*;

/**
 * 自定义模版 model
 *
 * @author hehaiyangwork@gmail.com
 * @date 2017/05/10
 */
public class Templates implements Serializable {

    private static final long serialVersionUID = 8790162098133834684L;

    /**
     * 模板的根root
     */
    private List<CodeGroup> groups = new ArrayList<>();

    public Templates() {
    }

    /**
     * 获取组和模板的映射map
     */
    public Map<String, List<CodeTemplate>> getTemplatesMap() {
        Map<String, List<CodeTemplate>> result = new HashMap<>();
        groups.forEach(it -> result.put(it.getId(), it.getTemplates()));
        return result;
    }

    public List<CodeGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<CodeGroup> groups) {
        this.groups = groups;
    }

    public CodeTemplate getTemplate(String templateId, String groupId) {
        Optional<CodeGroup> codeGroup = groups.stream().filter(group -> StringUtil.equals(groupId, group.getId())).findFirst();
        return codeGroup.map(group -> group.getTemplates()
                .stream().filter(template -> StringUtil.equals(templateId, template.getId()))
                .findFirst().orElse(null)).orElse(null);
    }
}
