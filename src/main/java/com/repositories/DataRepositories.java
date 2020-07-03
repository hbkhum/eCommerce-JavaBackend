package com.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataRepositories {
    @Autowired
    public IProductRepository ProductRepository;
    @Autowired
    public IProductImageRepository ProductImageRepository;
    @Autowired
    public IUserRepository userRepository;
    @Autowired
    public IRoleRepository roleRepository;
}
