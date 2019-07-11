package kr.ac.daelim.summer.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductVO {

    private long product_num;
    private String name;
    private String product_info;
    private long price;
    MultipartFile[] image;
}
