package com.xkcoding.bf.excel;

import com.yxt.bigdata.decision.server.SpringBootApplicationTests;
import com.yxt.bigdata.decision.server.excel.utils.MyExcelUtils;
import com.yxt.bigdata.decision.server.monthly.data.infra.excel.model.five.merchants.*;
import com.yxt.bigdata.decision.server.monthly.data.infra.excel.model.five.star.commodity.StarSalesDifferenceRateExcelModel;
import com.yxt.bigdata.decision.server.monthly.data.infra.excel.model.five.star.commodity.SubTaskAndSalesExcelModel;
import com.yxt.bigdata.decision.server.monthly.data.infra.excel.model.five.stores.develop.MAStoreCompletionRateExcelModel;
import com.yxt.bigdata.decision.server.monthly.data.infra.excel.model.five.stores.develop.NewStoreCompletionRateExcelModel;
import com.yxt.bigdata.decision.server.monthly.data.infra.excel.model.five.stores.develop.SubMAStoreNetGrowthExcelModel;
import com.yxt.bigdata.decision.server.monthly.data.infra.excel.model.five.stores.develop.SubStoreNetGrowthCompletionRateExcelModel;
import com.yxt.bigdata.decision.server.monthly.data.infra.excel.model.five.yx.convenience.*;
import com.yxt.bigdata.decision.server.monthly.data.infra.excel.model.four.activity.*;
import com.yxt.bigdata.decision.server.monthly.data.infra.excel.model.four.arrival.SubArrivalRateExcelModel;
import com.yxt.bigdata.decision.server.monthly.report.domain.service.template.dto.ExcelReadTableVO;
import com.yxt.bigdata.decision.server.monthly.report.domain.service.template.dto.ExcelSheetReadContext;
import com.yxt.bigdata.decision.spec.monthly.report.enums.Module;
import com.yxt.bigdata.decision.spec.monthly.report.enums.SubModule;
import com.yxt.bigdata.decision.spec.monthly.report.enums.Unit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.RoundingMode;

/**
 * 集团综述读取test
 *
 * @Author WuHuaQiang
 * @Date 2024/03/14 10:18
 **/
@Slf4j
public class GroupOverviewReadTest extends SpringBootApplicationTests {

    /**
     * 多sheet页多table，从文件中读取
     *
     * @throws IOException
     */
    @Test
    @Ignore
    public void multiSheetMultiTableReadFromFile() throws IOException {
        String fileName = MultiSheetsReadTest.class.getResource("/").getPath() + "monthly.xlsx";
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(fileName));
        byte[] byteArray = IOUtils.toByteArray(inputStream);
        ExcelSheetReadContext sheet0ReadContext = configExcelSheetReadContext(fileName);
        ExcelSheetReadContext sheet1ReadContext = configExcelSheetReadContextFive(fileName);
        sheet0ReadContext.setSourceByteArray(byteArray);
        sheet1ReadContext.setSourceByteArray(byteArray);
        //解析sheet页所有子表
        MyExcelUtils.readSheets(sheet0ReadContext, sheet1ReadContext);
        sheet0ReadContext.getExcelReadTables().forEach(excelReadTable -> {
        });
        log.info("解析完成");
    }

    public Integer getSheetIndex1() {
        return 3;
    }

    ;

    /**
     * 集团综述四
     *
     * @param fileName
     * @return
     */
    public ExcelSheetReadContext configExcelSheetReadContext(String fileName) {
        ExcelSheetReadContext sheetReadContext = new ExcelSheetReadContext();
        sheetReadContext.setSheetIndex(getSheetIndex1());
        sheetReadContext.setFileName(fileName);
        //子表顺序
        int order = 0;

        // 到货率 > 到货率-子公司
        ExcelReadTableVO<SubArrivalRateExcelModel> subArrivalRateTable = new ExcelReadTableVO<SubArrivalRateExcelModel>(
            getSheetIndex1(),
            Module.ARRIVAL_RATE,
            order++,
            SubModule.SUB_ARRIVAL_RATE,
            SubArrivalRateExcelModel.class,
            subArrivalRateExcelModel -> {
                subArrivalRateExcelModel.setArrivalRate(subArrivalRateExcelModel.getArrivalRate()
                                                                                .setScale(1, RoundingMode.DOWN));
                subArrivalRateExcelModel.setArrivalRateUnit(Unit.PER_CENT.getMsg());
                subArrivalRateExcelModel.setPopValue(subArrivalRateExcelModel.getPopValue()
                                                                             .setScale(1, RoundingMode.DOWN));
                subArrivalRateExcelModel.setPopUnit(Unit.PER_CENT.getMsg());
                subArrivalRateExcelModel.setGroupArrivalRate(subArrivalRateExcelModel.getGroupArrivalRate()
                                                                                     .setScale(1, RoundingMode.DOWN));
                subArrivalRateExcelModel.setGroupArrivalRateUnit(Unit.PER_CENT.getMsg());
            });

        // 大型活动 > 任务达成率，活动门店数
        ExcelReadTableVO<TaskCompletionRateExcelModel> taskCompletionRateTable = new ExcelReadTableVO<TaskCompletionRateExcelModel>(
            getSheetIndex1(),
            Module.LARGE_SCALE_ACTIVITY,
            order++,
            SubModule.ACTIVITY_TASK_COMPLETION_RATE,
            TaskCompletionRateExcelModel.class,
            taskCompletionRateDTO -> {
                taskCompletionRateDTO.setTaskCompletionRate(taskCompletionRateDTO.getTaskCompletionRate()
                                                                                 .setScale(1, RoundingMode.DOWN));
                taskCompletionRateDTO.setTaskCompletionRateUnit(Unit.PER_CENT.getMsg());
                taskCompletionRateDTO.setActivityStoreCount(taskCompletionRateDTO.getActivityStoreCount());
            });

        // 大型活动 > 实际提升率
        ExcelReadTableVO<PromoteRateExcelModel> promoteRateTable = new ExcelReadTableVO<PromoteRateExcelModel>(
            getSheetIndex1(),
            Module.LARGE_SCALE_ACTIVITY,
            order++,
            SubModule.PROMOTE_RATE,
            PromoteRateExcelModel.class,
            promoteRateDTO -> {
                promoteRateDTO.setValue(promoteRateDTO.getValue().setScale(1, RoundingMode.DOWN));
                promoteRateDTO.setUnit(Unit.PER_CENT.getMsg());
            });

        // 大型活动 > 折扣率
        ExcelReadTableVO<DiscountRateExcelModel> discountRateTable = new ExcelReadTableVO<DiscountRateExcelModel>(
            getSheetIndex1(),
            Module.LARGE_SCALE_ACTIVITY,
            order++,
            SubModule.DISCOUNT_RATE,
            DiscountRateExcelModel.class,
            discountRateDTO -> {
                discountRateDTO.setValue(discountRateDTO.getValue().setScale(1, RoundingMode.DOWN));
                discountRateDTO.setUnit(Unit.PER_CENT.getMsg());
            });

        // 大型活动 > 经销差率
        ExcelReadTableVO<SalesDifferenceRateExcelModel> sellDifferenceRateTable = new ExcelReadTableVO<SalesDifferenceRateExcelModel>(
            getSheetIndex1(),
            Module.LARGE_SCALE_ACTIVITY,
            order++,
            SubModule.ACTIVITY_SALES_DIFFERENCE_RATE,
            SalesDifferenceRateExcelModel.class,
            salesDifferenceRateDTO -> {
                salesDifferenceRateDTO.setValue(salesDifferenceRateDTO.getValue().setScale(1, RoundingMode.DOWN));
                salesDifferenceRateDTO.setUnit(Unit.PER_CENT.getMsg());
            });

        // 大型活动 > 任务达成-子公司
        ExcelReadTableVO<SubTaskCompletionExcelModel> subTaskCompletionTable = new ExcelReadTableVO<SubTaskCompletionExcelModel>(
            getSheetIndex1(),
            Module.LARGE_SCALE_ACTIVITY,
            order++,
            SubModule.SUB_TASK_COMPLETION,
            SubTaskCompletionExcelModel.class,
            subTaskCompletionDTO -> {
                subTaskCompletionDTO.setTaskCompletionRate(subTaskCompletionDTO.getTaskCompletionRate()
                                                                               .setScale(1, RoundingMode.DOWN));
                subTaskCompletionDTO.setTaskCompletionRateUnit(Unit.PER_CENT.getMsg());
                subTaskCompletionDTO.setTargetValue(subTaskCompletionDTO.getTargetValue()
                                                                        .setScale(0, RoundingMode.DOWN));
                subTaskCompletionDTO.setTargetUnit(Unit.PER_CENT.getMsg());
            });

        // 大型活动 > 任务达成红榜-分部
        ExcelReadTableVO<TaskCompletionRedExcelModel> taskCompletionRedListTable = new ExcelReadTableVO<TaskCompletionRedExcelModel>(
            getSheetIndex1(),
            Module.LARGE_SCALE_ACTIVITY,
            order++,
            SubModule.TASK_COMPLETION_RED_LIST,
            TaskCompletionRedExcelModel.class,
            taskCompletionRedExcelModel -> {
                taskCompletionRedExcelModel.setTaskCompletionRate(taskCompletionRedExcelModel.getTaskCompletionRate()
                                                                                             .setScale(1,
                                                                                                       RoundingMode.DOWN));
                taskCompletionRedExcelModel.setTaskCompletionRateUnit(Unit.PER_CENT.getMsg());

            });

        // 大型活动 > 任务达成黑榜-分部
        ExcelReadTableVO<TaskCompletionBlackExcelModel> taskCompletionBlackListTable = new ExcelReadTableVO<TaskCompletionBlackExcelModel>(
            getSheetIndex1(),
            Module.LARGE_SCALE_ACTIVITY,
            order++,
            SubModule.TASK_COMPLETION_BLACK_LIST,
            TaskCompletionBlackExcelModel.class,
            taskCompletionBlackDTO -> {
                taskCompletionBlackDTO.setTaskCompletionRate(taskCompletionBlackDTO.getTaskCompletionRate()
                                                                                   .setScale(1, RoundingMode.DOWN));
                taskCompletionBlackDTO.setTaskCompletionRateUnit(Unit.PER_CENT.getMsg());

            });

        sheetReadContext.addExcelReadTable(subArrivalRateTable);
        sheetReadContext.addExcelReadTable(taskCompletionRateTable);
        sheetReadContext.addExcelReadTable(promoteRateTable);
        sheetReadContext.addExcelReadTable(discountRateTable);
        sheetReadContext.addExcelReadTable(sellDifferenceRateTable);
        sheetReadContext.addExcelReadTable(subTaskCompletionTable);
        sheetReadContext.addExcelReadTable(taskCompletionRedListTable);
        sheetReadContext.addExcelReadTable(taskCompletionBlackListTable);
        return sheetReadContext;
    }

    public Integer getSheetIndex() {
        return 4;
    }

    /**
     * 集团综述5
     *
     * @param fileName
     * @return
     */
    public ExcelSheetReadContext configExcelSheetReadContextFive(String fileName) {
        ExcelSheetReadContext sheetReadContext = new ExcelSheetReadContext();
        sheetReadContext.setSheetIndex(4);
        sheetReadContext.setFileName(fileName);
        //子表顺序
        int order = 0;

        // 心耀商品>任务达成率
        ExcelReadTableVO<com.yxt.bigdata.decision.server.monthly.data.infra.excel.model.five.star.commodity.TaskCompletionRateExcelModel> taskCompletionRateTable = new ExcelReadTableVO<com.yxt.bigdata.decision.server.monthly.data.infra.excel.model.five.star.commodity.TaskCompletionRateExcelModel>(
            getSheetIndex(),
            Module.STAR_COMMODITY,
            order++,
            SubModule.STAR_TASK_COMPLETION_RATE,
            com.yxt.bigdata.decision.server.monthly.data.infra.excel.model.five.star.commodity.TaskCompletionRateExcelModel.class,
            taskCompletionRateDTO -> {
                taskCompletionRateDTO.setValue(taskCompletionRateDTO.getValue().setScale(1, RoundingMode.DOWN));
                taskCompletionRateDTO.setUnit(Unit.PER_CENT.getMsg());
            });

        // 心耀商品>经销差率
        ExcelReadTableVO<StarSalesDifferenceRateExcelModel> salesDifferenceRateTable = new ExcelReadTableVO<StarSalesDifferenceRateExcelModel>(
            getSheetIndex(),
            Module.STAR_COMMODITY,
            order++,
            SubModule.STAR_SALES_DIFFERENCE_RATE,
            StarSalesDifferenceRateExcelModel.class,
            salesDifferenceRateDTO -> {
                salesDifferenceRateDTO.setValue(salesDifferenceRateDTO.getValue().setScale(1, RoundingMode.DOWN));
                salesDifferenceRateDTO.setUnit(Unit.PER_CENT.getMsg());
            });

        // 心耀商品>任务达成&进销差率-子公司
        ExcelReadTableVO<SubTaskAndSalesExcelModel> subTaskAndSalesTable = new ExcelReadTableVO<SubTaskAndSalesExcelModel>(
            getSheetIndex(),
            Module.STAR_COMMODITY,
            order++,
            SubModule.SUB_TASK_SALES,
            SubTaskAndSalesExcelModel.class,
            subTaskAndSalesDTO -> {
                subTaskAndSalesDTO.setTaskCompletionRate(subTaskAndSalesDTO.getTaskCompletionRate()
                                                                           .setScale(1, RoundingMode.DOWN));
                subTaskAndSalesDTO.setTaskCompletionRateUnit(Unit.PER_CENT.getMsg());
                subTaskAndSalesDTO.setSalesDifferenceRate(subTaskAndSalesDTO.getSalesDifferenceRate()
                                                                            .setScale(1, RoundingMode.DOWN));
                subTaskAndSalesDTO.setSalesDifferenceRateUnit(Unit.PER_CENT.getMsg());
            });

        // 门店发展>新开店-累计开店达成率
        ExcelReadTableVO<NewStoreCompletionRateExcelModel> newStoreCompletionRateTable = new ExcelReadTableVO<NewStoreCompletionRateExcelModel>(
            getSheetIndex(),
            Module.STORES_DEVELOP,
            order++,
            SubModule.NEW_STORE_COMPLETION_RATE,
            NewStoreCompletionRateExcelModel.class,
            newStoreCompletionRateDTO -> {
                newStoreCompletionRateDTO.setBeginCompletionRate(newStoreCompletionRateDTO.getBeginCompletionRate()
                                                                                          .setScale(1,
                                                                                                    RoundingMode.DOWN));
                newStoreCompletionRateDTO.setBeginCompletionRateUnit(Unit.PER_CENT.getMsg());
            });

        // 门店发展>并购店-累计开店达成率
        ExcelReadTableVO<MAStoreCompletionRateExcelModel> mAStoreCompletionRateTable = new ExcelReadTableVO<MAStoreCompletionRateExcelModel>(
            getSheetIndex(),
            Module.STORES_DEVELOP,
            order++,
            SubModule.MA_STORE_COMPLETION_RATE,
            MAStoreCompletionRateExcelModel.class,
            mAStoreCompletionRateDTO -> {
                mAStoreCompletionRateDTO.setBeginStoreCompletionRate(mAStoreCompletionRateDTO.getBeginStoreCompletionRate()
                                                                                             .setScale(1,
                                                                                                       RoundingMode.DOWN));
                mAStoreCompletionRateDTO.setBeginStoreCompletionRateUnit(Unit.PER_CENT.getMsg());
            });

        // 门店发展>新开净增长门店及开店达成率-子公司
        ExcelReadTableVO<SubStoreNetGrowthCompletionRateExcelModel> subStoreNetGrowthCompletionRateTable = new ExcelReadTableVO<SubStoreNetGrowthCompletionRateExcelModel>(
            getSheetIndex(),
            Module.STORES_DEVELOP,
            order++,
            SubModule.SUB_STORE_NET_GROWTH_COMPLETION_RATE,
            SubStoreNetGrowthCompletionRateExcelModel.class,
            netGrowthCompletionRateDTO -> {
                netGrowthCompletionRateDTO.setBeginStoreCompletionRate(netGrowthCompletionRateDTO.getBeginStoreCompletionRate()
                                                                                                 .setScale(0,
                                                                                                           RoundingMode.DOWN));
                netGrowthCompletionRateDTO.setBeginStoreCompletionRateUnit(Unit.PER_CENT.getMsg());
            });

        // 门店发展>并购净增长门店-子公司
        ExcelReadTableVO<SubMAStoreNetGrowthExcelModel> subMAStoreNetGrowthTable = new ExcelReadTableVO<SubMAStoreNetGrowthExcelModel>(
            getSheetIndex(),
            Module.STORES_DEVELOP,
            order++,
            SubModule.SUB_MA_STORE_NET_GROWTH,
            SubMAStoreNetGrowthExcelModel.class,
            subMAStoreNetGrowthDTO -> {
            });

        // 加盟店>开业门店数
        ExcelReadTableVO<StoreStartCountExcelModel> storeStartCountTable = new ExcelReadTableVO<StoreStartCountExcelModel>(
            getSheetIndex(),
            Module.MERCHANTS,
            order++,
            SubModule.STORE_START_COUNT,
            StoreStartCountExcelModel.class,
            storeStartCountDTO -> {
            });

        // 加盟店>签约门店数
        ExcelReadTableVO<StoreAgencyCountExcelModel> storeAgencyCountTable = new ExcelReadTableVO<StoreAgencyCountExcelModel>(
            getSheetIndex(),
            Module.MERCHANTS,
            order++,
            SubModule.STORE_AGENCY_COUNT,
            StoreAgencyCountExcelModel.class,
            storeAgencyCount -> {
            });

        // 加盟店>开业完成率
        ExcelReadTableVO<StartCompletionRateExcelModel> startCompletionRateTable = new ExcelReadTableVO<StartCompletionRateExcelModel>(
            getSheetIndex(),
            Module.MERCHANTS,
            order++,
            SubModule.START_COMPLETION_RATE,
            StartCompletionRateExcelModel.class,
            startCompletionRateDTO -> {
                startCompletionRateDTO.setValue(startCompletionRateDTO.getValue().setScale(0, RoundingMode.DOWN));
                startCompletionRateDTO.setUnit(Unit.PER_CENT.getMsg());
            });

        // 加盟店>签约完成率
        ExcelReadTableVO<AgencyCompletionRateExcelModel> agencyCompletionRateTable = new ExcelReadTableVO<AgencyCompletionRateExcelModel>(
            getSheetIndex(),
            Module.MERCHANTS,
            order++,
            SubModule.AGENCY_COMPLETION_RATE,
            AgencyCompletionRateExcelModel.class,
            agencyCompletionRateDTO -> {
                agencyCompletionRateDTO.setValue(agencyCompletionRateDTO.getValue().setScale(0, RoundingMode.DOWN));
                agencyCompletionRateDTO.setUnit(Unit.PER_CENT.getMsg());
            });

        // 加盟店>累计开业门店数
        ExcelReadTableVO<StartCumulativeCountExcelModel> startCumulativeCountTable = new ExcelReadTableVO<StartCumulativeCountExcelModel>(
            getSheetIndex(),
            Module.MERCHANTS,
            order++,
            SubModule.START_CUMULATIVE_COUNT,
            StartCumulativeCountExcelModel.class,
            startCumulativeCountDTO -> {
                startCumulativeCountDTO.setValue(startCumulativeCountDTO.getValue().setScale(0, RoundingMode.DOWN));
            });

        // 加盟店>筹建门店数
        ExcelReadTableVO<StorePrepareCountExcelModel> storePrepareCountTable = new ExcelReadTableVO<StorePrepareCountExcelModel>(
            getSheetIndex(),
            Module.MERCHANTS,
            order++,
            SubModule.STORE_PREPARE_COUNT,
            StorePrepareCountExcelModel.class,
            storePrepareCountDTO -> {
                storePrepareCountDTO.setValue(storePrepareCountDTO.getValue().setScale(0, RoundingMode.DOWN));
            });

        // 加盟店>各阶段门店统计-子公司
        ExcelReadTableVO<SubStageStoreCountExcelModel> subStageStoreCountTable = new ExcelReadTableVO<SubStageStoreCountExcelModel>(
            getSheetIndex(),
            Module.MERCHANTS,
            order++,
            SubModule.SUB_STAGE_STORE_COUNT,
            SubStageStoreCountExcelModel.class,
            subStageStoreCountDTO -> {
            });

        // 加盟店>累计开业门店-子公司
        ExcelReadTableVO<SubStartCumulativeCountExcelModel> subStartCumulativeCountTable = new ExcelReadTableVO<SubStartCumulativeCountExcelModel>(
            getSheetIndex(),
            Module.MERCHANTS,
            order++,
            SubModule.SUB_START_CUMULATIVE_COUNT,
            SubStartCumulativeCountExcelModel.class,
            subStartCumulativeCountDTO -> {
            });

        // 一心便利>零售销售完成率
        ExcelReadTableVO<RetailSaleCompletionRateExcelModel> retailSaleCompletionRateTable = new ExcelReadTableVO<RetailSaleCompletionRateExcelModel>(
            getSheetIndex(),
            Module.YX_CONVENIENCE,
            order++,
            SubModule.RETAIL_SALE_COMPLETION_RATE,
            RetailSaleCompletionRateExcelModel.class,
            retailSaleCompletionRateDTO -> {
                retailSaleCompletionRateDTO.setValue(retailSaleCompletionRateDTO.getValue()
                                                                                .setScale(1, RoundingMode.DOWN));
                retailSaleCompletionRateDTO.setUnit(Unit.PER_CENT.getMsg());
                retailSaleCompletionRateDTO.setYoyValue(retailSaleCompletionRateDTO.getYoyValue()
                                                                                   .setScale(1, RoundingMode.DOWN));
                retailSaleCompletionRateDTO.setYoyUnit(Unit.PER_CENT.getMsg());
                retailSaleCompletionRateDTO.setPopValue(retailSaleCompletionRateDTO.getPopValue()
                                                                                   .setScale(1, RoundingMode.DOWN));
                retailSaleCompletionRateDTO.setPopUnit(Unit.PER_CENT.getMsg());
            });

        // 一心便利>批发销售完成率
        ExcelReadTableVO<WholeSaleCompletionRateExcelExcelModel> wholeSaleCompletionRateTable = new ExcelReadTableVO<WholeSaleCompletionRateExcelExcelModel>(
            getSheetIndex(),
            Module.YX_CONVENIENCE,
            order++,
            SubModule.WHOLE_SALE_COMPLETION_RATE,
            WholeSaleCompletionRateExcelExcelModel.class,
            wholeSaleCompletionRateDTO -> {
                wholeSaleCompletionRateDTO.setValue(wholeSaleCompletionRateDTO.getValue()
                                                                              .setScale(1, RoundingMode.DOWN));
                wholeSaleCompletionRateDTO.setUnit(Unit.PER_CENT.getMsg());
                wholeSaleCompletionRateDTO.setYoyValue(wholeSaleCompletionRateDTO.getYoyValue()
                                                                                 .setScale(1, RoundingMode.DOWN));
                wholeSaleCompletionRateDTO.setYoyUnit(Unit.PER_CENT.getMsg());
                wholeSaleCompletionRateDTO.setPopValue(wholeSaleCompletionRateDTO.getPopValue()
                                                                                 .setScale(1, RoundingMode.DOWN));
                wholeSaleCompletionRateDTO.setPopUnit(Unit.PER_CENT.getMsg());
            });

        // 一心便利>零售经销差完成率
        ExcelReadTableVO<RetailSaleDifferenceCompletionRateExcelModel> retailSaleDifferenceCompletionRateTable = new ExcelReadTableVO<RetailSaleDifferenceCompletionRateExcelModel>(
            getSheetIndex(),
            Module.YX_CONVENIENCE,
            order++,
            SubModule.RETAIL_SALE_DIFFERENCE_COMPLETION_RATE,
            RetailSaleDifferenceCompletionRateExcelModel.class,
            retailSaleDifferenceCompletionRateDTO -> {
                retailSaleDifferenceCompletionRateDTO.setValue(retailSaleDifferenceCompletionRateDTO.getValue()
                                                                                                    .setScale(1,
                                                                                                              RoundingMode.DOWN));
                retailSaleDifferenceCompletionRateDTO.setUnit(Unit.PER_CENT.getMsg());
                retailSaleDifferenceCompletionRateDTO.setYoyValue(retailSaleDifferenceCompletionRateDTO.getYoyValue()
                                                                                                       .setScale(1,
                                                                                                                 RoundingMode.DOWN));
                retailSaleDifferenceCompletionRateDTO.setYoyUnit(Unit.PER_CENT.getMsg());
                retailSaleDifferenceCompletionRateDTO.setPopValue(retailSaleDifferenceCompletionRateDTO.getPopValue()
                                                                                                       .setScale(1,
                                                                                                                 RoundingMode.DOWN));
                retailSaleDifferenceCompletionRateDTO.setPopUnit(Unit.PER_CENT.getMsg());
            });

        // 一心便利>零售经销差率
        ExcelReadTableVO<RetailSaleDifferenceRateExcelModel> retailSaleDifferenceRateTable = new ExcelReadTableVO<RetailSaleDifferenceRateExcelModel>(
            getSheetIndex(),
            Module.YX_CONVENIENCE,
            order++,
            SubModule.RETAIL_SALE_DIFFERENCE_RATE,
            RetailSaleDifferenceRateExcelModel.class,
            retailSaleDifferenceRateDTO -> {
                retailSaleDifferenceRateDTO.setValue(retailSaleDifferenceRateDTO.getValue()
                                                                                .setScale(1, RoundingMode.DOWN));
                retailSaleDifferenceRateDTO.setUnit(Unit.PER_CENT.getMsg());
                retailSaleDifferenceRateDTO.setYoyValue(retailSaleDifferenceRateDTO.getYoyValue()
                                                                                   .setScale(1, RoundingMode.DOWN));
                retailSaleDifferenceRateDTO.setYoyUnit(Unit.PER_CENT.getMsg());
                retailSaleDifferenceRateDTO.setPopValue(retailSaleDifferenceRateDTO.getPopValue()
                                                                                   .setScale(1, RoundingMode.DOWN));
                retailSaleDifferenceRateDTO.setPopUnit(Unit.PER_CENT.getMsg());
            });

        // 一心便利>店日均交易次数趋势
        ExcelReadTableVO<StoreAverageDailyDealTrendExcelModel> storeAverageDailyDealTrendTable = new ExcelReadTableVO<StoreAverageDailyDealTrendExcelModel>(
            getSheetIndex(),
            Module.YX_CONVENIENCE,
            order++,
            SubModule.STORE_AVERAGE_DAILY_DEAL_TREND,
            StoreAverageDailyDealTrendExcelModel.class,
            storeAverageDailyDealTrendDTO -> {
            });

        // 一心便利>客单价趋势
        ExcelReadTableVO<PerCustomerTrendExcelModel> perCustomerTrendTable = new ExcelReadTableVO<PerCustomerTrendExcelModel>(
            getSheetIndex(),
            Module.YX_CONVENIENCE,
            order++,
            SubModule.PER_CUSTOMER_TREND,
            PerCustomerTrendExcelModel.class,
            perCustomerTrendDTO -> {
                perCustomerTrendDTO.setValue(perCustomerTrendDTO.getValue().setScale(2, RoundingMode.DOWN));
            });

        sheetReadContext.addExcelReadTable(taskCompletionRateTable);
        sheetReadContext.addExcelReadTable(salesDifferenceRateTable);
        sheetReadContext.addExcelReadTable(subTaskAndSalesTable);
        sheetReadContext.addExcelReadTable(newStoreCompletionRateTable);
        sheetReadContext.addExcelReadTable(mAStoreCompletionRateTable);
        sheetReadContext.addExcelReadTable(subStoreNetGrowthCompletionRateTable);
        sheetReadContext.addExcelReadTable(subMAStoreNetGrowthTable);
        sheetReadContext.addExcelReadTable(storeStartCountTable);
        sheetReadContext.addExcelReadTable(storeAgencyCountTable);
        sheetReadContext.addExcelReadTable(startCompletionRateTable);
        sheetReadContext.addExcelReadTable(agencyCompletionRateTable);
        sheetReadContext.addExcelReadTable(startCumulativeCountTable);
        sheetReadContext.addExcelReadTable(storePrepareCountTable);
        sheetReadContext.addExcelReadTable(subStageStoreCountTable);
        sheetReadContext.addExcelReadTable(subStartCumulativeCountTable);
        sheetReadContext.addExcelReadTable(retailSaleCompletionRateTable);
        sheetReadContext.addExcelReadTable(wholeSaleCompletionRateTable);
        sheetReadContext.addExcelReadTable(retailSaleDifferenceCompletionRateTable);
        sheetReadContext.addExcelReadTable(retailSaleDifferenceRateTable);
        sheetReadContext.addExcelReadTable(storeAverageDailyDealTrendTable);
        sheetReadContext.addExcelReadTable(perCustomerTrendTable);
        return sheetReadContext;
    }

}
