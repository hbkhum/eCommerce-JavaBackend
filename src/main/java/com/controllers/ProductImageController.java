package com.controllers;

import com.DTO.ProductDTO;
import com.DTO.ProductImageDTO;
import com.interfaces.entities.Product;
import com.interfaces.entities.ProductImage;
import com.pagination.enums.Direction;
import com.pagination.enums.productImage.OrderBy;
import com.pagination.exception.PaginationSortingException;
import com.services.DataServices;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/productImages")
@CrossOrigin
public class ProductImageController {
    @Autowired
    private DataServices dataServices;

    //http://localhost:8080/api/productImages/?orderBy=title&direction=DESC&page=0&pageSize=10
    @RequestMapping(value = "/", params = {
            "orderBy",
            "direction",
            "page",
            "pageSize" }, method = RequestMethod.GET)
    @Async
    public ResponseEntity<Page<ProductImage>> findAll(@RequestParam("orderBy") String orderBy,
                                                      @RequestParam("direction") String direction, @RequestParam("page") int page,
                                                      @RequestParam("pageSize") int pageSize) {

        if (!(direction.equals(Direction.ASCENDING.getDirectionCode())
                || direction.equals(Direction.DESCENDING.getDirectionCode()))) {
            throw new PaginationSortingException("Invalid sort direction");
        }
        if (!(orderBy.equals(OrderBy.productImageId.getOrderByCode()) || orderBy.equals(OrderBy.fileName.getOrderByCode()))) {
            throw new PaginationSortingException("Invalid orderBy condition");
        }

        Page<ProductImage> list= dataServices.productImageService.getAllProductImages(orderBy,direction,page,pageSize);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Async
    @GetMapping("/{id}")
    public ResponseEntity<ProductImage> getById(@PathVariable("id") String id){
        if(!dataServices.productImageService.existsByproductImageId(id)){
            return new ResponseEntity("The Id don't exist ", HttpStatus.NOT_FOUND);
        }
        Optional<ProductImage> productImage =dataServices.productImageService.getProductImage (id);
        /*ProductImageDTO productImageDto = productImage.map(p -> {
            ProductImageDTO productImageDtoMap = new ProductImageDTO();
            productImageDtoMap.setFileName(p.getFileName());
            //productImageDto.setFileImageAtt();
            return productImageDtoMap;
        }).get();*/
        return new ResponseEntity<ProductImage>(productImage.get(), HttpStatus.OK);
        //return ResponseEntity.ok(product);
        //return dataServices.productService.getProduct(id);
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> post(@ModelAttribute ProductImageDTO productImageDto){

        try{

            //entity.setFileImage(entity.getFileImageAtt().getBytes());
            //entity.setFileName(entity.getFileImageAtt().getOriginalFilename());

            //_entity.setFileImage(entity.getFileImage());
            //_entity.setFileName(entity.getFileName());

            ProductImage productImage =   new ProductImage(
                    productImageDto.getFileImageAtt().getOriginalFilename(),
                    productImageDto.getFileImageAtt().getBytes());
            if (productImageDto.getProduct()!=null){
                Product product = new Product();
                product.setProductId(productImageDto.getProduct().getProductId());
                productImage.setProduct(product);
            }
            String id=dataServices.productImageService.addProductImage(productImage);
            return new ResponseEntity(productImage, HttpStatus.CREATED);
            //return ResponseEntity.ok(id);
        }catch (Exception e){
            //throw new PaginationSortingException(e.getMessage());
            //return ResponseEntity.badRequest().body(e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> put( @PathVariable("id") String id,@Valid @RequestBody ProductImageDTO productImageDto){
        try{
            if (!dataServices.productImageService.existsByproductImageId(productImageDto.getProductImageId())) new ResponseEntity("The title is empty", HttpStatus.NOT_FOUND);
            //if(StringUtils.isBlank(productImage.getFileName())) throw new Exception("The title is empty");
            /*if(dataServices.productImageService.existsByFileName(productImage.getFileName()) &&
                    dataServices.productImageService .getByFileName(productImage.getFileName()).get().getProductImageId() != id)
                throw new Exception("the title already exists ");*/
            ProductImage productImage = new ProductImage(); //new ProductImage(productImageDto.getFileName(),null);
            boolean status = dataServices.productImageService.updateProductImage(id,productImage);
            return new ResponseEntity(status, HttpStatus.CREATED);
            //return new ResponseEntity("the row was updated", HttpStatus.CREATED);
        }catch (Exception e){
            //return ResponseEntity.badRequest().body(e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> delete(@PathVariable String id){
        if(!dataServices.productImageService.existsByproductImageId(id))
            return new ResponseEntity("The Id don't exist ", HttpStatus.NOT_FOUND);
        boolean status=dataServices.productImageService.deleteProductImage(id);
        return new ResponseEntity(status, HttpStatus.OK);
    }
}

