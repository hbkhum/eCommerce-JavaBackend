package com.controllers;

import com.DTO.ProductDTO;
import com.DTO.ProductImageDTO;
import com.interfaces.entities.Product;
import com.interfaces.entities.ProductImage;
import com.pagination.enums.Direction;
import com.pagination.enums.product.OrderBy;
import com.pagination.exception.PaginationSortingException;
import com.services.DataServices;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin
public class ProductController {
    @Autowired
    private DataServices dataServices;




    //http://localhost:8080/api/products/?orderBy=title&direction=DESC&page=0&pageSize=10
    @RequestMapping(value = "/", params = {
            "orderBy",
            "direction",
            "page",
            "pageSize" }, method = RequestMethod.GET)
    @Async
    public ResponseEntity<Page<ProductDTO>> findAll(@RequestParam("orderBy") String orderBy,
                                                    @RequestParam("direction") String direction, @RequestParam("page") int page,
                                                    @RequestParam("pageSize") int pageSize) {

        if (!(direction.equals(Direction.ASCENDING.getDirectionCode())
                || direction.equals(Direction.DESCENDING.getDirectionCode()))) {
            throw new PaginationSortingException("Invalid sort direction");
        }
        if (!(orderBy.equals(OrderBy.productId.getOrderByCode()) || orderBy.equals(OrderBy.title.getOrderByCode()))) {
            throw new PaginationSortingException("Invalid orderBy condition");
        }

        Page<ProductDTO> list= dataServices.productService
                .getAllProducts(orderBy,direction,page,pageSize).map(p -> {
                    ProductDTO p1 = new ProductDTO();
                    p1.setProductId(p.getProductId());
                    p1.setDescription(p.getDescription());
                    p1.setPrice(p.getPrice());
                    p1.setTitle(p.getTitle());

                    p1.setProductImage(p.getProductImage().stream()
                            .map(item -> {
                                ProductImageDTO piDto = new ProductImageDTO();
                                piDto.setProductImageId(item.getProductImageId());
                                return piDto;
                            }).collect(Collectors.toList()));
                    return p1;
                });
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/Images", params = {
            "orderBy",
            "direction",
            "page",
            "pageSize" }, method = RequestMethod.GET)
    @Async
    public ResponseEntity<Page<ProductDTO>> findAllWithImages(@RequestParam("orderBy") String orderBy,
                                                    @RequestParam("direction") String direction, @RequestParam("page") int page,
                                                    @RequestParam("pageSize") int pageSize) {

        if (!(direction.equals(Direction.ASCENDING.getDirectionCode())
                || direction.equals(Direction.DESCENDING.getDirectionCode()))) {
            throw new PaginationSortingException("Invalid sort direction");
        }
        if (!(orderBy.equals(OrderBy.productId.getOrderByCode()) || orderBy.equals(OrderBy.title.getOrderByCode()))) {
            throw new PaginationSortingException("Invalid orderBy condition");
        }

        Page<ProductDTO> list= dataServices.productService
                .getAllProducts(orderBy,direction,page,pageSize).map(p -> {
                    ProductDTO p1 = new ProductDTO();
                    p1.setProductId(p.getProductId());
                    p1.setDescription(p.getDescription());
                    p1.setPrice(p.getPrice());
                    p1.setTitle(p.getTitle());

                    p1.setProductImage(p.getProductImage().stream()
                            .map(item -> {
                                ProductImageDTO piDto = new ProductImageDTO();
                                piDto.setProductImageId(item.getProductImageId());
                                piDto.setFileImage(item.getFileImage());
                                return piDto;
                            }).collect(Collectors.toList()));
                    return p1;
                });
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @Async
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable("id") String id){
        if(!dataServices.productService.existsByProductId(id)){
            return new ResponseEntity("The Id don't exist ", HttpStatus.NOT_FOUND);
        }
        Optional<Product> product =dataServices.productService.getProduct(id);
        return new ResponseEntity<Product>(product.get(), HttpStatus.OK);
        //return ResponseEntity.ok(product);
        //return dataServices.productService.getProduct(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> post(@Valid @RequestBody ProductDTO productDto){
        try{
            if(StringUtils.isBlank(productDto.getTitle())) return new ResponseEntity("The title is empty", HttpStatus.NOT_FOUND);
            if( productDto.getPrice()==0  ) throw new Exception("It is necessary a price");
            Product product = new Product(productDto.getTitle(),productDto.getPrice(),productDto.getDescription());
            String id=dataServices.productService.addProduct(product);
            //return new ResponseEntity(product, HttpStatus.CREATED);
            return new ResponseEntity(product, HttpStatus.CREATED);
            //return ResponseEntity.ok(id);
        }catch (Exception e){
            //throw new PaginationSortingException(e.getMessage());
            //return ResponseEntity.badRequest().body(e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> put( @PathVariable("id") String id,@Valid @RequestBody ProductDTO productDto){
        try{
            if (!dataServices.productService.existsByProductId(productDto.getProductId())) new ResponseEntity("The title is empty", HttpStatus.NOT_FOUND);
            if(StringUtils.isBlank(productDto.getTitle())) throw new Exception("The title is empty");
            /*if(dataServices.productService.existsByTitle(product.getTitle()) &&
                    dataServices.productService.getByTitle(product.getTitle()).get().getProductId() != id)
                throw new Exception("the title already exists ");*/

            Product product = new Product(productDto.getTitle(),productDto.getPrice(),productDto.getDescription());
            if (productDto.getProductImage() != null ){
                product.setProductImage(productDto.getProductImage().stream()
                        .map(p -> {
                            ProductImage pIm = new ProductImage();
                            pIm.setProductImageId(p.getProductImageId());
                            return pIm;
                        }).collect(Collectors.toList()));
            }

;
            //product.setProductImage(productDto.);
            product.setProductId(productDto.getProductId());
            boolean status = dataServices.productService.updateProduct(id,product);
            return new ResponseEntity(status, HttpStatus.OK);
            //return new ResponseEntity("the row was updated", HttpStatus.CREATED);
        }catch (Exception e){
            //return ResponseEntity.badRequest().body(e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> delete(@PathVariable String id){
        if(!dataServices.productService.existsByProductId(id))
            return new ResponseEntity("The Id don't exist ", HttpStatus.NOT_FOUND);
        boolean status=dataServices.productService.deleteProduct(id);
        return new ResponseEntity(status, HttpStatus.OK);
    }
}
