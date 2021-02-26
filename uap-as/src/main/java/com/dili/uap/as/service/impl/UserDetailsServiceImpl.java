//package com.dili.uap.as.service.impl;
//
//import com.dili.ss.domain.BaseOutput;
//import com.dili.ss.dto.DTOUtils;
//import com.dili.uap.sdk.domain.Role;
//import com.dili.uap.sdk.rpc.RoleRpc;
//import com.dili.uap.sdk.rpc.UserRpc;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.InternalAuthenticationServiceException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 自定义的认证用户获取服务类
// */
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    @Autowired
//    private UserRpc userRpc;
//    @Autowired
//    private RoleRpc roleRpc;
//
//    /**
//     * 根据用户名获取认证用户信息
//     */
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        if(StringUtils.isEmpty(username)) {
//            throw new UsernameNotFoundException("UserDetailsService没有接收到用户账号");
//        } else {
//            com.dili.uap.sdk.domain.User query = DTOUtils.newInstance(com.dili.uap.sdk.domain.User.class);
//            query.setUserName(username);
//            /**
//             * 根据用户名，查询用户信息
//             */
//            BaseOutput<List<com.dili.uap.sdk.domain.User>> result = userRpc.list(query);
//            if (!result.isSuccess()) {
//                throw new InternalAuthenticationServiceException("UserDetailsService查询用户账号信息失败:"+result.getMessage());
//            }
//            List<com.dili.uap.sdk.domain.User> users = result.getData();
//            if(users.isEmpty()){
//                throw new UsernameNotFoundException(String.format("用户'%s'不存在", username));
//            }
//            com.dili.uap.sdk.domain.User user = users.get(0);
//            BaseOutput<List<Role>> listBaseOutput = roleRpc.listByUser(user.getId(), null);
//            if(!listBaseOutput.isSuccess()){
//                throw new InternalAuthenticationServiceException(String.format("查询用户'%s'所属角色失败", username));
//            }
//            List<Role> roles = listBaseOutput.getData();
//            if(roles.isEmpty()){
//                throw new InternalAuthenticationServiceException(String.format("用户'%s'所属角色为空", username));
//            }
//            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//            for (Role role : roles) {
//                grantedAuthorities.add(new SimpleGrantedAuthority(role.getId().toString()));
//            }
//            /**
//             * 根据查询的用户信息，创建一个认证用户对象，用于用户认证
//             */
//            return new User(user.getUserName(), user.getPassword(), grantedAuthorities);
//        }
//    }
//}
//
