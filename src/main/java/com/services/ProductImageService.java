package com.services;

import com.interfaces.entities.ProductImage;
import com.repositories.DataRepositories;
import com.services.interfaces.IProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductImageService implements IProductImageService {
    @Autowired
    private DataRepositories dataRepositories;
    @Override
    public Page<ProductImage> getAllProductImages(String orderBy, String direction, int page, int pageSize) {
        Sort sort = null;
        if (direction.equals("ASC")) {
            sort = new Sort(new Sort.Order(Sort.Direction.ASC, orderBy));
        }
        if (direction.equals("DESC")) {
            sort = new Sort(new Sort.Order(Sort.Direction.DESC, orderBy));
        }

        Pageable pageable = new PageRequest(page, pageSize, sort);
        Page<ProductImage> result= dataRepositories.ProductImageRepository.findAll(pageable);
        return result;
    }

    @Override
    public Optional<ProductImage>  getProductImage(String id) {
        Optional<ProductImage> productImage = Optional.ofNullable(dataRepositories.ProductImageRepository.getOne(id));
        return productImage;
    }

    @Override
    public String addProductImage(ProductImage entity) throws Exception {
        //ProductImage _entity= new ProductImage();
        try {
            /*UUID uuid = UUID.randomUUID();
            String randomUUIDString = uuid.toString();*/

            //_entity.setProductImageId(randomUUIDString);


            //_entity.setFileImage(entity.getFileImage());
            //_entity.setFileName(entity.getFileName());
            // _entity.setProduct(entity.getProduct());
            String productId=entity.getProduct().getProductId().toString();
            //var byId  = dataRepositories.ProductRepository.findById(productId);
            //Product product = byId.get();
            // _entity.setProduct(entity.getProduct());
            dataRepositories.ProductImageRepository.save(entity);
            entity.setProductImageId(entity.getProductImageId());
        }catch (Exception e){
            throw new Exception(e.getCause().getCause().getMessage(),e);
        }
        //return null;
        return entity.getProductImageId();
    }

    @Override
    public boolean updateProductImage(String id,ProductImage entity) {
        dataRepositories.ProductImageRepository.save(entity);
        return true;
    }

    @Override
    public boolean deleteProductImage(String id) {
        ProductImage _entity = dataRepositories.ProductImageRepository.getOne(id);
        dataRepositories.ProductImageRepository.delete(_entity);
        return true;
    }

    @Override
    public boolean existsByproductImageId(String id) {
        return dataRepositories.ProductImageRepository.existsByproductImageId(id);
    }

    @Override
    public boolean existsByFileName(String fileName) {
        return dataRepositories.ProductImageRepository.existsByFileName(fileName);
    }

    @Override
    public Optional<ProductImage> getByFileName(String fileName) {
        return dataRepositories.ProductImageRepository.findByFileName(fileName);
    }
}
