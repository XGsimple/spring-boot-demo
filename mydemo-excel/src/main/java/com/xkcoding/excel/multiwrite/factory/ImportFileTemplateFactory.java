package com.xkcoding.excel.multiwrite.factory;

import com.xkcoding.excel.multiwrite.AbstractImportFileTemplate;
import com.xkcoding.excel.multiwrite.dto.ImportFileMetaDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class ImportFileTemplateFactory {

    @Resource
    private List<AbstractImportFileTemplate> templateList;

    /**
     * 获取导入文件的执行模板
     **/
    public Optional<AbstractImportFileTemplate> findTemplate(ImportFileMetaDTO importFileMetaDTO) {
        return templateList.stream().filter(template -> template.checkMatched(importFileMetaDTO)).findFirst();
    }
}
