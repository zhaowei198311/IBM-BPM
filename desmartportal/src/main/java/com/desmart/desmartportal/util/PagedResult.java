package com.desmart.desmartportal.util;

import java.util.List;

public class PagedResult<T> extends BaseEntity {
	
	/*serialVersionUID*/
	private static final long serialVersionUID = 1L;

	private List<T> dataList;//数据
	
	private long pageNo;//当前页
	
	private long pageSize;//条数
	
	private long total;//总条数
	
	private long pages;//总页面数目
	
	private long beginNum;//开始行数
	private long endNum;//结束行数

	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	public long getPageNo() {
		return pageNo;
	}

	public void setPageNo(long pageNo) {
		this.pageNo = pageNo;
	}

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getPages() {
		return pages;
	}

	public void setPages(long pages) {
		this.pages = pages;
	}

	public long getBeginNum() {
		if(total>0){
			beginNum=(this.pageNo-1)*this.pageSize+1;
		}
		return beginNum;
	}

	public void setBeginNum(long beginNum) {
		this.beginNum = beginNum;
	}

	public long getEndNum() {
		endNum=this.pageNo*this.pageSize;
		if(endNum>=total){
			endNum=total;
		}
		return endNum;
	}

	public void setEndNum(long endNum) {
		this.endNum = endNum;
	}
	
}
