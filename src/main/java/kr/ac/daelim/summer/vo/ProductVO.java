package kr.ac.daelim.summer.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProductVO {

    private long product_num;
    private String name;
    private String product_info;
    private long price;
    MultipartFile[] image; // 상품 이미지 파일 - uploads

    List<FileVO> fileVOList; // 상품 이미지 파일 정보 - 조회용
}
