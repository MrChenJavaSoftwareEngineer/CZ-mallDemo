package com.imooc.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.ProductMapper;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.common.Constant.ProductListOrderBy;
import com.imooc.mall.model.query.ProductListQuery;
import com.imooc.mall.model.request.ProductListReq;
import com.imooc.mall.model.vo.CategoryVO;
import com.imooc.mall.service.CategoryService;
import com.imooc.mall.service.ProductService;
import com.imooc.mall.util.ExcelUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import java.util.ArrayList;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductMapper productMapper;
    @Autowired
    CategoryService categoryService;

    @Override
    public void add(Product product) throws ImoocMallException {
        Product result = productMapper.selectByName(product.getName());
        if (result!=null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.PRODUCT_EXISTED);
        }
        int count = productMapper.insertSelective(product);
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.INSERT_FAILED);
        }
    }

    @Override
    public void delete(Integer id) throws ImoocMallException {
        int count = productMapper.deleteByPrimaryKey(id);
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
        }
    }

    @Override
    public void update(Product product) throws ImoocMallException {
         Product result = productMapper.selectByPrimaryKey(product.getId());
        if (result == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.PRODUCT_NOT_EXISTED);
        }
        BeanUtils.copyProperties(product,result);
         int count = productMapper.updateByPrimaryKeySelective(result);
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public PageInfo<Product> list(Integer pageNum, Integer pageSize) {
        List<Product> products = productMapper.selecqtList();
        PageHelper.startPage(pageNum,pageSize);
         PageInfo<Product> pageInfo = new PageInfo<>(products);
         return pageInfo;
    }

    @Override
    public void batchUpdate(Integer[] ids, Integer sellStatus) throws ImoocMallException {
         int count = productMapper.batchUpdate(ids, sellStatus);
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public Product detail(Integer id) {
        return productMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<Product> listOfUser(ProductListReq productListReq) {
        //创建Query类
         ProductListQuery queryList = new ProductListQuery();
        //搜索的处理
        if (!StringUtils.isEmpty(productListReq.getKeyWord())) {
           String keyWord = new StringBuilder().append("%").
                   append(productListReq.getKeyWord()).append("%").toString();
           queryList.setKeyWord(keyWord);
        }
        //递归查询子类
        if (productListReq.getCategoryId() != null){
             List<CategoryVO> categoryVOS = categoryService.userList(productListReq.getCategoryId());
            List<Integer> listId = new ArrayList<>();
            listId.add(productListReq.getCategoryId());
            recursivelyFindCategoryIds(categoryVOS,listId);
            queryList.setCategoryIds(listId);
        }
        //排序处理
        if (ProductListOrderBy.PRICE_ASC_DESC.contains(productListReq.getOrderBy())){
            PageHelper.startPage(productListReq.getPageNum(), productListReq.getPageSize(),
                    productListReq.getOrderBy());
        }else {
            PageHelper.startPage(productListReq.getPageNum(),productListReq.getPageSize());
        }
        //返回商品列表
        List<Product> products = productMapper.selectList(queryList);
        PageInfo<Product> productPageInfo = new PageInfo<>(products);
        return productPageInfo;
    }

    private void recursivelyFindCategoryIds(List<CategoryVO> categoryVOS, List<Integer> listId) {
        for (int i = 0; i < categoryVOS.size(); i++) {
             CategoryVO categoryVO = categoryVOS.get(i);
             if (categoryVO!=null) {
                 listId.add(categoryVO.getId());
                 recursivelyFindCategoryIds(categoryVO.getChildCategory(), listId);
             }
        }
    }

    @Override
    public PageInfo lists(ProductListReq productListReq) {
        //构建Query对象
        ProductListQuery productListQuery = new ProductListQuery();

        //搜索处理
        if (!StringUtils.isEmpty(productListReq.getKeyWord())) {
            String keyword = new StringBuilder().append("%").append(productListReq.getKeyWord())
                    .append("%").toString();
            productListQuery.setKeyWord(keyword);
        }

        //目录处理：如果查某个目录下的商品，不仅是需要查出该目录下的，还要把所有子目录的所有商品都查出来，所以要拿到一个目录id的List
        if (productListReq.getCategoryId() != null) {
            List<CategoryVO> categoryVOList = categoryService
                    .userList(productListReq.getCategoryId());
            ArrayList<Integer> categoryIds = new ArrayList<>();
            categoryIds.add(productListReq.getCategoryId());
            getCategoryIds(categoryVOList, categoryIds);
            productListQuery.setCategoryIds(categoryIds);
        }

        //排序处理
        String orderBy = productListReq.getOrderBy();
        if (ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
            PageHelper
                    .startPage(productListReq.getPageNum(), productListReq.getPageSize(), orderBy);
        } else {
            PageHelper
                    .startPage(productListReq.getPageNum(), productListReq.getPageSize());
        }

        List<Product> productList = productMapper.selectList(productListQuery);
        PageInfo pageInfo = new PageInfo(productList);
        return pageInfo;
    }

    private void getCategoryIds(List<CategoryVO> categoryVOList, ArrayList<Integer> categoryIds) {
        for (int i = 0; i < categoryVOList.size(); i++) {
            CategoryVO categoryVO = categoryVOList.get(i);
            if (categoryVO != null) {
                categoryIds.add(categoryVO.getId());
                getCategoryIds(categoryVO.getChildCategory(), categoryIds);
            }
        }
    }

    @Override
    public void addProductByExcel(File destFile) throws IOException, ImoocMallException {
         List<Product> products = readProductsFromExcel(destFile);
        for (int i = 0; i < products.size(); i++) {
             Product product = products.get(i);
             Product result = productMapper.selectByName(product.getName());
            if (result != null) {
                throw new ImoocMallException(ImoocMallExceptionEnum.PRODUCT_EXISTED);
            }
             int count = productMapper.insertSelective(product);
            if (count == 0) {
                throw new ImoocMallException(ImoocMallExceptionEnum.INSERT_FAILED);
            }
        }
    }

    private List<Product> readProductsFromExcel(File excelFile) throws IOException {
        List<Product> listProducts = new ArrayList<>();
         FileInputStream fileInputStream = new FileInputStream(excelFile);

         XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
         XSSFSheet firstSheet = workbook.getSheetAt(0);
         Iterator<Row> iterator = firstSheet.iterator();
         while(iterator.hasNext()){
              Row nextRow = iterator.next();
              Iterator<Cell> cellIterator = nextRow.cellIterator();
              Product aProduct = new Product();

              while(cellIterator.hasNext()){
                   Cell nextCell = cellIterator.next();
                   int columnIndex = nextCell.getColumnIndex();

                   switch (columnIndex){
                       case 0:
                           aProduct.setName((String) ExcelUtil.getCellValue(nextCell));
                           break;
                       case 1:
                           aProduct.setImage((String) ExcelUtil.getCellValue(nextCell));
                           break;
                       case 2:
                           aProduct.setDetail((String) ExcelUtil.getCellValue(nextCell));
                           break;
                       case 3:
                            Double cellValue = (Double) ExcelUtil.getCellValue(nextCell);
                           aProduct.setCategoryId(cellValue.intValue());
                           break;
                       case 4:
                            cellValue = (Double) ExcelUtil.getCellValue(nextCell);
                           aProduct.setPrice(cellValue.intValue());
                           break;
                       case 5:
                            cellValue = (Double) ExcelUtil.getCellValue(nextCell);
                           aProduct.setStock(cellValue.intValue());
                           break;
                       case 6:
                            cellValue = (Double) ExcelUtil.getCellValue(nextCell);
                           aProduct.setStatus(cellValue.intValue());
                           break;
                       default:
                           break;
                   }
              }
              listProducts.add(aProduct);
         }
         workbook.close();
         fileInputStream.close();
         return listProducts;
    }


}
