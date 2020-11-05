package com.offcn.user.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

//响应地址
@ApiModel("地址返回")
@Data
public class UserAddressVo {

    @ApiModelProperty("地址id")
    private Integer id; //地址id
    @ApiModelProperty("地址")
    private String address ; //地址

    public UserAddressVo() {
    }

}
