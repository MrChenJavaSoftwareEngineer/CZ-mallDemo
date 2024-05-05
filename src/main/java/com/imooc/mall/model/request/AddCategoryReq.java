package com.imooc.mall.model.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *目录的名字，种类，父类id，编号
 *
 *
 * toString是为了满足WebLogAspect类进行书写
 */
public class AddCategoryReq {
    @Size(min = 2,max = 5)
    private String name;

    @Max(3)
    @NotNull(message = "种类不能为空")
    private Integer type;

    @NotNull(message = "父类id不能为空")
    private Integer parentId;

    @NotNull(message = "编号不能为空")
    private Integer orderNum;

    @Override
    public String toString() {
        return "AddCategoryReq{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", parentId=" + parentId +
                ", orderNum=" + orderNum +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
}
