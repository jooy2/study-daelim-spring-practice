package kr.ac.daelim.summer.service.impl;

import kr.ac.daelim.summer.mapper.UserMapper;
import kr.ac.daelim.summer.service.UserService;
import kr.ac.daelim.summer.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void insertUser(UserVO userVO) {
        this.userMapper.insertUser(userVO);
    }
    @Override
    public UserVO selectUser(UserVO userVO) {
        return this.userMapper.selectUser(userVO);
    }
}