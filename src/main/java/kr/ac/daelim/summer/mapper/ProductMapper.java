package kr.ac.daelim.summer.mapper;

import kr.ac.daelim.summer.vo.FileVO;
import kr.ac.daelim.summer.vo.ProductVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ProductMapper {

    /**
     * 상품 정보 등록
     * @param productVO
     */
    void insertProduct(ProductVO productVO);

    /**
     * 상품 이미지 정보 등록
     * @param fileVO
     */
    void insertFile(FileVO fileVO);
}
