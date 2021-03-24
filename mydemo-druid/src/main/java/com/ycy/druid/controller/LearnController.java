package com.ycy.druid.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ycy.druid.domain.LearnResource;
import com.ycy.druid.model.LeanQueryLeanListReq;
import com.ycy.druid.service.LearnService;
import com.ycy.druid.util.AjaxObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 教程页面
 * Created by tengj on 2017/3/13.
 */
@Controller
@RequestMapping("/learn")
public class LearnController extends AbstractController {
    @Autowired
    private LearnService learnService;

    @RequestMapping("")
    public String learn(Model model) {
        model.addAttribute("ctx", getContextPath() + "/");
        return "learn-resource";
    }

    /**
     * 查询教程列表
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "/queryLeanList", method = RequestMethod.POST)
    @ResponseBody
    public AjaxObject queryLearnList(Page<LeanQueryLeanListReq> page) {
        Page<LearnResource> learnResourcePage = learnService.queryLearnResoucePage(page);
        return AjaxObject.ok().put("page", learnResourcePage);
    }

    /**
     * 新添教程
     *
     * @param learn
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public AjaxObject addLearn(@RequestBody LearnResource learn) {
        learnService.save(learn);
        return AjaxObject.ok();
    }

    /**
     * 修改教程
     *
     * @param learn
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public AjaxObject updateLearn(@RequestBody LearnResource learn) {
        learnService.updateById(learn);
        return AjaxObject.ok();
    }

    /**
     * 删除教程
     *
     * @param ids
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxObject deleteLearn(@RequestBody Long[] ids) {
        learnService.deleteBatch(ids);
        return AjaxObject.ok();
    }

    /**
     * 获取教程
     *
     * @param id
     */
    @RequestMapping(value = "/resource/{id}", method = RequestMethod.GET)
    @ResponseBody
    public LearnResource qryLearn(@PathVariable(value = "id") Long id) {
        LearnResource learnResource = learnService.getById(id);
        return learnResource;
    }
}
