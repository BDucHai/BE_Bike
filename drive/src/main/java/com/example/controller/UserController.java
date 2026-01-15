package com.example.controller;

import com.example.entity.Users;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user){
        Users userCheck = userRepository.findByUserName(user.getUserName());
        System.out.println(userCheck);
        if(userCheck == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tên đăng nhập không chính xác!");
        }else{
            if(user.getPassword().equals(userCheck.getPassword())){
                return ResponseEntity.status(HttpStatus.OK).body("Thành công");
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mật khẩu không chính xác!");
            }
        }
    }
}
