package kr.ac.daelim.summer.controller;

import kr.ac.daelim.summer.service.ProductService;
import kr.ac.daelim.summer.vo.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/admin/product_regist")
    public String productRegistForm() {
        return "admin/product_regist";
    }

    @PostMapping("/admin/product_regist")
    public String productRegistFormExecute(Model model
            , @ModelAttribute ProductVO productVO) {
        /**
         * 상품이미지 전달받은 정보 debug
         */
        log.debug("등록한 파일 갯수: " + String.valueOf(productVO.getImage()!=null?0:productVO.getImage().length));
        for (MultipartFile file:productVO.getImage()){
            log.debug("업로드한 파일: " + file.getOriginalFilename());
        }
        /**
         * 상품 정보 등록 요청
         */
        this.productService.insertProduct(productVO);

        /**
         * 등록한 상품정보 VIEW에 전달
         */
        model.addAttribute(productVO);

        return "admin/product_regist_success";
    }
}
