package com.xkcoding.bf.dto;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.xkcoding.bf.enums.ImportFileSourceEnum;
import com.xkcoding.bf.enums.ImportFileTemplateEnum;
import com.xkcoding.bf.utils.Conditions;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 导入文件信息 DTO
 **/
@Setter
@Getter
@Builder
@ToString(callSuper = true)
public class ImportFileMetaDTO {
    /**
     * 导入文件记录ID
     */
    private Long importRecordId;
    /**
     * 导入模板类型
     */
    private ImportFileTemplateEnum importFileTemplateEnum;

    /**
     * 默认是华为OBS
     */
    private ImportFileSourceEnum importFileSourceEnum = ImportFileSourceEnum.OBS;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * ImportFileSourceEnum.OBS 对象存储路径，即上传后在桶中的文件路径，包含文件名
     * ImportFileSourceEnum.LOCAL_FILE 本地文件路径
     */
    private String filePath;

    /**
     * 统计时间DP
     */
    private StatisticsDateDP statisticsDate;

    @Getter
    public static class StatisticsDateDP {
        private String statisticsDate;
        private Integer year;
        private Integer month;
        /**
         * 日期: 202403
         */
        private Long date;

        private StatisticsDateDP() {}

        public StatisticsDateDP(String statisticsDate) {
            Conditions.assertTrue(StrUtil.isNotBlank(statisticsDate), "统计日期statisticsDate不能为空");
            LocalDateTime statisticsLocalDateTime = LocalDateTimeUtil.parse(statisticsDate,
                                                                            DateTimeFormatter.ofPattern("yyyy-MM"));
            this.year = statisticsLocalDateTime.getYear();
            this.month = statisticsLocalDateTime.getMonth().getValue();
            this.date = (long)(year * 100 + month);
            this.statisticsDate = statisticsDate;
        }
    }

}
