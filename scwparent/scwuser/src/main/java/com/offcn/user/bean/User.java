package com.offcn.user.bean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

//接口实体类 注解配置
@ApiModel(value = "测试实体类")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class User {
    @ApiModelProperty("主键id")
    private Integer id;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("邮箱")
    private String email;

}
