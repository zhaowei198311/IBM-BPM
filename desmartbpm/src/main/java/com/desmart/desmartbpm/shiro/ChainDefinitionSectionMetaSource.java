package com.desmart.desmartbpm.shiro;

import org.apache.shiro.config.Ini;
import org.springframework.beans.factory.FactoryBean;

public class ChainDefinitionSectionMetaSource implements FactoryBean<Ini.Section> {
    private String filterChainDefinitions = null;

    @Override
    public Ini.Section getObject() throws Exception {
        Ini ini = new Ini();
        ini.load(this.filterChainDefinitions);
        Ini.Section section = ini.getSection("");
        return section;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public void setFilterChainDefinitions(String filterChainDefinitions) {
        this.filterChainDefinitions = filterChainDefinitions;
    }

}