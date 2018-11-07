package com.linuslan.oa.util;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {
	private List<T> rows;
	private int page;
	private long records;
	private long total;
	
	private List<T> footer = new ArrayList<T> ();
	
	public Page(List<T> data, long totalRecord, long totalPage, int currentPage) {
		this.rows = data;
		this.records = totalRecord;
		this.total = totalPage;
		this.page = currentPage;
	}
	
	public List<T> getRows() {
		return rows;
	}
	
	public void setData(List<T> data) {
		this.rows = data;
	}
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	
	public long getRecords() {
		return records;
	}

	public void setRecords(long records) {
		this.records = records;
	}

	public long getTotal() {
		return total;
	}
	
	public void setTotal(long totalRecord) {
		this.total = totalRecord;
	}

	public List<T> getFooter() {
		return footer;
	}

	public void setFooter(List<T> footer) {
		this.footer = footer;
	}
	
	public static long countTotalPage(long totalRecord, int rows) {
		return totalRecord % rows == 0 ? totalRecord / rows : totalRecord / rows + 1;
	}
}
