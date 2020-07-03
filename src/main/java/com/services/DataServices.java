package com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataServices {
    @Autowired
    public ProductService productService;
    @Autowired
    public ProductImageService productImageService;
    @Autowired
    public UserService userService;
    @Autowired
    public RoleService roleService;


}
