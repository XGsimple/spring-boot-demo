package com.xkcoding.excel.multiwrite.utils;

import cn.hutool.core.date.DateUtil;
import com.apifan.common.random.RandomSource;
import com.apifan.common.random.constant.Province;
import com.apifan.common.random.source.PersonInfoSource;
import com.xkcoding.excel.multiwrite.excel.dto.UserExportDTO;
import com.xkcoding.excel.multiwrite.dto.WaterMarkDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@MockitoSettings(strictness = Strictness.WARN)
@SpringBootTest
class ExcelUtilTest {

    @DisplayName("用户导出测试-文件导出")
    @Test
    void testExportUsers() throws Exception {
        List<UserExportDTO> testUsers = IntStream.range(0, 100).boxed().map(i -> {
            PersonInfoSource personInfoSource = RandomSource.personInfoSource();
            return new UserExportDTO().setIdCard(personInfoSource.randomMaleIdCard(Province.SC, 20, 60))
                                      .setName(personInfoSource.randomChineseName())
                                      .setPhone(personInfoSource.randomChineseMobile());
        }).collect(Collectors.toList());
        File file = new File("D:/testExportUsers" + DateUtil.format(new Date(), "yyyyMMddHHmmss") + ".xlsx");
        WaterMarkDTO watermark = new WaterMarkDTO();
        watermark.setContent("测试水印");
        ExcelUtil.exportExcel(testUsers,
                              UserExportDTO.class,
                              watermark,
                              new FileOutputStream(file),
                              file.getName(),
                              false);

    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme