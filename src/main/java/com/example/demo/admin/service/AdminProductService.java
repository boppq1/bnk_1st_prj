package com.example.demo.admin.service;

import com.example.demo.admin.dao.IAdminProductDao;
import com.example.demo.admin.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final IAdminProductDao dao;

    public int registerPro(ProductDto dto){
        return dao.registerPro(dto);
    }

    public List<ProductDto> listPro(){
        return dao.listPro();
    }

    public List<ProductDto> selectByStatus(String approve_status){
        return dao.selectByStatus(approve_status);
    }

    public ProductDto listDetail(Long product_id){
        return dao.listDetail(product_id);
    }

    public int updatePro(ProductDto dto){
        return dao.updatePro(dto);
    }

    public int updateProductStatus(ProductDto dto){
        return dao.updateProductStatus(dto);
    }

    public int deletePro(Long product_id){
        return dao.deletePro(product_id);
    }

}
