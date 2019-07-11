package kr.ac.daelim.summer.service;

import kr.ac.daelim.summer.vo.ProductVO;

public interface ProductService {

    /**
     * 상품정보 등록 및 상품 이미지 처리를 담당하는 서비스
     * @param productVO
     * @return
     */
    ProductVO insertProduct(ProductVO productVO);
}
