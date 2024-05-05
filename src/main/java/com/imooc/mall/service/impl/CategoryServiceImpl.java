package com.imooc.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.CategoryMapper;
import com.imooc.mall.model.pojo.Category;
import com.imooc.mall.model.vo.CategoryVO;
import com.imooc.mall.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public void add(Category category) throws ImoocMallException {
        //查询是否存在该目录
        Category result = categoryMapper.selectByName(category.getName());
        if (result != null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.CATEGORY_EXISTED);
        }
        //写入数据库
        int count = categoryMapper.insertSelective(category);
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.INSERT_FAILED);
        }
    }

    @Override
    public void update(Category category) throws ImoocMallException {
        Category result = categoryMapper.selectByPrimaryKey(category.getId());
        if (result == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.CATEGORY_NOT_EXISTED);
        }
        BeanUtils.copyProperties(category,result);
        int count = categoryMapper.updateByPrimaryKeySelective(result);
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void delete(Integer id) throws ImoocMallException {
        int count = categoryMapper.deleteByPrimaryKey(id);
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
        }
    }

    @Override
    public PageInfo list(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize,"type,order_num");
         List<Category> categories = categoryMapper.selectList();
         PageInfo pageInfo = new PageInfo(categories);
         return pageInfo;
    }

    @Override
    public List<CategoryVO> userList(Integer parentId) {
        List<CategoryVO> categoryVOList = new ArrayList<>();
        //递归查询
        recursivelyFindCategories(categoryVOList,parentId);
        return categoryVOList;
    }

    private void recursivelyFindCategories(List<CategoryVO> categoryVOList,
                                           Integer parentId) {
        for (Category category : categoryMapper.selectCategoriesByParentId(parentId)) {
//            if (category == null) {
//                return;
//            }
            CategoryVO categoryVO = new CategoryVO();
            BeanUtils.copyProperties(category,categoryVO);
            categoryVOList.add(categoryVO);
            recursivelyFindCategories(categoryVO.getChildCategory(),categoryVO.getId());
        }
//        //递归获取所有子类别，并组合成为一个“目录树”
//        List<Category> categoryList = categoryMapper.selectCategoriesByParentId(parentId);
//        if (!CollectionUtils.isEmpty(categoryList)) {
//            for (int i = 0; i < categoryList.size(); i++) {
//                Category category = categoryList.get(i);
//                CategoryVO categoryVO = new CategoryVO();
//                BeanUtils.copyProperties(category, categoryVO);
//                categoryVOList.add(categoryVO);
//                recursivelyFindCategories(categoryVO.getChildCategory(), categoryVO.getId());
//            }
//        }
    }

}
