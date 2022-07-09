package com.example.wxreceivefile.mapper;

import com.example.wxreceivefile.pojo.PlatUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlatUserMapper {
    PlatUser getInfo(@Param("id") String id, @Param("type") String type);
}
