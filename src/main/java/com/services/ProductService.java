package com.services;

import com.interfaces.entities.Product;
import com.repositories.DataRepositories;
import com.services.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.util.Optional;


@Service
public class ProductService implements IProductService {
    @Autowired
    private DataRepositories dataRepositories;

    @Override
    public Page<Product> getAllProducts(String orderBy, String direction, int page, int pageSize) {

        Sort sort = null;
        if (direction.equals("ASC")) sort = new Sort(new Sort.Order(Direction.ASC, orderBy));
        if (direction.equals("DESC")) sort = new Sort(new Sort.Order(Direction.DESC, orderBy));

        Pageable pageable = new PageRequest(page, pageSize, sort);
        Page<Product> result = dataRepositories.ProductRepository.findAll(pageable);
        //Optional<Product> result= dataRepositories.ProductRepository.findAll(pageable);
        return result;
    }

    @Override
    public Optional<Product> getProduct(String id) {
        Optional<Product> product = dataRepositories.ProductRepository.findByIdJoinProductImage(id);


        /*Optional<Product> product = dataRepositories.ProductRepository.findByIdJoinProductImage(id);
        product.get().getProductImage().stream().forEach( p-> {
            p.setProduct(null);
        });*/


        //return dataRepositories.ProductRepository.findById(id);
        /*Product product= dataRepositories.ProductRepository.getOne(id);
        if(Objects.isNull(product)){
            *//* handle this exception using {@link RestExceptionHandler} *//*
        }
        return new Product().build(product);*/
        //return dataRepositories.ProductRepository.getOne(id);
        return product;
    }


    @Override
    public String addProduct(Product entity) throws Exception {
        try {
            /*UUID uuid = UUID.randomUUID();
            String randomUUIDString = uuid.toString();*/


            dataRepositories.ProductRepository.save(entity);
            entity.setProductId(entity.getProductId());
        }catch (Exception e){
            throw new Exception(e.getCause().getCause().getMessage(),e);
        }
        return entity.getProductId();
    }

    @Override
    public boolean updateProduct(String id,Product entity) {
        dataRepositories.ProductRepository.save(entity);
        return true;
    }

    @Override
    public boolean deleteProduct(String id) {
        Product _entity = dataRepositories.ProductRepository.getOne(id);
        dataRepositories.ProductRepository.delete(_entity);
        return true;
    }

    @Override
    public boolean existsByTitle(String title) {
        return dataRepositories.ProductRepository.existsByTitle(title);
    }

    @Override
    public boolean existsByProductId(String id) {
        return dataRepositories.ProductRepository.existsByProductId(id);
    }

    @Override
    public Optional<Product> getByTitle(String title) {
        return dataRepositories.ProductRepository.findByTitle(title);
    }
}
