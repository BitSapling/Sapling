//package com.github.bitsapling.sapling.module.user;
//
//import com.github.bitsapling.sapling.util.PasswordUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AuthenticationServiceImpl implements AuthenticationService {
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private PasswordUtil passwordUtil;
//
//    public boolean authenticateByUsername(String username, String password) {
//        User user = userService.getUserByUsername(username);
//        if (user == null) {
//            return false;
//        }
//        return passwordUtil.validate(user.getPassword(), password);
//    }
//
//    public boolean authenticateByEmail(String username, String password) {
//        User user = userService.getUserByUsername(username);
//        if (user == null) {
//            return false;
//        }
//        return passwordUtil.validate(user.getPassword(), password);
//    }
//}
