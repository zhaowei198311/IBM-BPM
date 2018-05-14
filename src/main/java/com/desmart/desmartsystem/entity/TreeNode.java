package com.desmart.desmartsystem.entity;

import java.io.Serializable;

public class TreeNode implements Serializable{  
      
    private static final long serialVersionUID = -766759489880382042L;  
      
    private String id;  
    private String pId;  
    private String name;  
    private String isParent;
    private String url;
    private Integer lvl;
    private Integer childNodelvl;
    private String nodeType;
    private String code;
    
    private Integer parentCode;
      
    public TreeNode() {  
        super();  
    }  
  
    public TreeNode(String id, String pId, String name,String isParent,Integer lvl) {  
        super();  
        this.id = id;  
        this.pId = pId;  
        this.name = name;  
        this.isParent =isParent;
        this.lvl=lvl;
    }  
    
    public TreeNode(String id,String name,String pId) {  
        this.id = id;  
        this.pId = pId;  
        this.name = name;  
    }  
  
    public String getId() {  
        return id;  
    }  
  
    public void setId(String id) {  
        this.id = id;  
    }  
  
    public String getpId() {  
        return pId;  
    }  
  
    public void setpId(String pId) {  
        this.pId = pId;  
    }  
  
    public String getName() {  
        return name;  
    }  
  
    public void setName(String name) {  
        this.name = name;  
    }  

	public String getIsParent() {
		return isParent;
	}

	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getLvl() {
		return lvl;
	}

	public void setLvl(Integer lvl) {
		this.lvl = lvl;
	}

	public Integer getChildNodelvl() {
		return childNodelvl;
	}

	public void setChildNodelvl(Integer childNodelvl) {
		this.childNodelvl = childNodelvl;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getParentCode() {
		return parentCode;
	}

	public void setParentCode(Integer parentCode) {
		this.parentCode = parentCode;
	}
	
	
    
}  