package kr.ac.daelim.summer.vo;

import lombok.Data;

@Data
public class FileVO {
    private long file_num;
    private long product_num;
    private String original_name; // 사용자가 등록한 파일 이름
    private String file_name; // 서버에 저장된 파일 이름
    private long file_size; // 파일 크기
    private String local_path; // 서버에 저장된 파일 경로
    private String web_path; // HTTP로 호출 가능한 파일 경로
}
