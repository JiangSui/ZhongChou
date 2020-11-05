package com.offcn.user.service.impl;

import com.offcn.user.enums.UserExceptionEnum;
import com.offcn.user.exception.UserException;
import com.offcn.user.mapper.TMemberAddressMapper;
import com.offcn.user.mapper.TMemberMapper;
import com.offcn.user.po.TMember;
import com.offcn.user.po.TMemberAddress;
import com.offcn.user.po.TMemberAddressExample;
import com.offcn.user.po.TMemberExample;
import com.offcn.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TMemberMapper tMemberMapper;

    @Autowired
    private TMemberAddressMapper addressMapper;
    @Override
    //注册
    public void registerUser(TMember member) {
        // 1.判断用户名是否存在
        TMemberExample example = new TMemberExample();
        example.createCriteria().andLoginacctEqualTo(member.getLoginacct());
        Long count  = tMemberMapper.countByExample(example);
        if(count>0){
            //;该用户存在
            throw new UserException(UserExceptionEnum.LOGINACCT_EXIST);
        }else{
            //不存在  注册  加密密码
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String encode = bCryptPasswordEncoder.encode(member.getUserpswd());
            member.setUserpswd(encode);//加密后的密码
            member.setUsername(member.getLoginacct()); //用户名和登录名相等
            member.setEmail(member.getEmail());
            //实名认证状态 0 - 未实名认证， 1 - 实名认证申请中， 2 - 已实名认证
            member.setAuthstatus("0");
            //用户类型: 0 - 个人， 1 - 企业
            member.setUsertype("0");
            //账户类型: 0 - 企业， 1 - 个体， 2 - 个人， 3 - 政府
            member.setAccttype("2");
            System.out.println(":插入用户:"+member.getLoginacct() );
            tMemberMapper.insert(member);
        }
    }

    //登录
    public TMember login(String username, String password) {
        //加密对象
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //根据 用户名查询
        TMemberExample example = new TMemberExample();
        example.createCriteria().andLoginacctEqualTo(username);
        TMember member = tMemberMapper.selectByExample(example).get(0);
        if(member!=null){
            //比较  将password 加密 和数据库的密码比较
            boolean matches = encoder.matches(password, member.getUserpswd());
            return matches?member:null;
        }
        return null;
    }

    //根据用户id，获取用户信息
    public TMember findTmemberById(Integer id){
       return tMemberMapper.selectByPrimaryKey(id);
    }

    //获取该用户的地址
    public List<TMemberAddress> addressList(Integer memberId) {
        TMemberAddressExample example = new TMemberAddressExample();
        example.createCriteria().andMemberidEqualTo(memberId);
        return addressMapper.selectByExample(example);
    }
}
