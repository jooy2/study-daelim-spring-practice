package kr.ac.daelim.summer.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserVO {
    private long user_num;
    private String email;
    private String password;
    private String name;
    private Date reg_date;
}
