package kr.ac.daelim.summer.controller;

import kr.ac.daelim.summer.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

@Controller
public class MainController {

    @Autowired
    private CommonUtil commonUtil;

    @GetMapping(value="/")
    public String main(Model model) {
        model.addAttribute("datetime", new Date());
        model.addAttribute("myname", this.commonUtil.getMyName());

        return "main/main";
    }
}
