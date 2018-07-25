package com.desmart.desmartportal.dao;


import com.desmart.desmartportal.entity.DhFormNoCounter;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DhFormNoCounterMapper {
    int deleteByPrimaryKey(String formNoExpersion);

    int insert(DhFormNoCounter record);

    int insertSelective(DhFormNoCounter record);

    DhFormNoCounter selectByPrimaryKey(String formNoExpersion);

    int updateByPrimaryKeySelective(DhFormNoCounter record);

    int updateByPrimaryKey(DhFormNoCounter record);

    int updateByFormNoExpressionAndCurrentNo(@Param("formNoExpression") String formNoExpression, @Param("currentNo") int currentNo);

}