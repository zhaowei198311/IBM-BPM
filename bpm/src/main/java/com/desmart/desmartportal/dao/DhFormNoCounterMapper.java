package com.desmart.desmartportal.dao;


import com.desmart.desmartportal.entity.DhFormNoCounter;
import org.apache.ibatis.annotations.Param;

public interface DhFormNoCounterMapper {
    int deleteByPrimaryKey(String formNoExpersion);

    int insert(DhFormNoCounter record);

    int insertSelective(DhFormNoCounter record);

    DhFormNoCounter selectByPrimaryKey(String formNoExpersion);

    int updateByPrimaryKeySelective(DhFormNoCounter record);

    int updateByPrimaryKey(DhFormNoCounter record);

//    int updateByFormNoExpersionAndCurrentNo(@Param("formNoExpression"));

}