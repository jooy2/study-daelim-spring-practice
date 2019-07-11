package kr.ac.daelim.summer.service.impl;

import kr.ac.daelim.summer.mapper.ProductMapper;
import kr.ac.daelim.summer.service.ProductService;
import kr.ac.daelim.summer.vo.FileVO;
import kr.ac.daelim.summer.vo.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Value("${file.localpath")
    private String localpath;

    @Value("${file.webpath}")
    private String webpath;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ProductVO insertProduct(ProductVO productVO) {

        /**
         * 상품 정보 등록
         */
        this.productMapper.insertProduct(productVO);

        List<FileVO> fileVOList = new ArrayList<>();
        FileVO fileVO = null;
        String fileExt = null;
        String newFileName = null;

        // 1. 파일 이름 변경해서 저장
        for (MultipartFile image:productVO.getImage()) {
            if (!image.isEmpty()) {

                fileVO = new FileVO();
                fileExt = FilenameUtils.getExtension(image.getOriginalFilename());
                fileExt = fileExt.toLowerCase();

                newFileName = UUID.randomUUID().toString();

                // 신규 파일명 정의
                fileVO.setFile_name(newFileName + "." + fileExt);
                fileVO.setOriginal_name(image.getOriginalFilename());
                fileVO.setFile_size(image.getSize());

                fileVO.setLocal_path(localpath + File.pathSeparator + newFileName);
                fileVO.setWeb_path(webpath + "/" + newFileName);

                fileVOList.add(fileVO);

                try {
                    image.transferTo(new File(fileVO.getLocal_path()));
                } catch (IOException ioe) {
                    log.error("파일 등록 중 오류가 발생했습니다.", ioe);
                }
            }
        }

        return productVO;
    }
}
