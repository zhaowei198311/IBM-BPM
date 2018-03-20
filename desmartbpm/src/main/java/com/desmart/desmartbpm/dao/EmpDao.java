package com.desmart.desmartbpm.dao;

import com.desmart.desmartbpm.enetity.Emp;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpDao {
    Emp queryByPrimaryKey(Integer empno);
}