package com.student.service;

import org.springframework.stereotype.Service;

@Service
public class OrderService {
    public String createOrder() {
        System.out.println("👨‍💻 正在创建订单...");
        return "ok";
    }
}
