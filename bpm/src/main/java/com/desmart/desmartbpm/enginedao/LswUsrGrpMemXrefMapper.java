package com.desmart.desmartbpm.enginedao;

import com.desmart.desmartbpm.entity.engine.LswUsrGrpMemXref;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LswUsrGrpMemXrefMapper {

    List<LswUsrGrpMemXref> listAll();


}