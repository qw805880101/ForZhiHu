package com.example.forzhihu.info;

import java.io.Serializable;
import java.util.List;

public class ThemesInfo implements Serializable{
	private String limit;
	private List<String> subscribed;
	private List<ThemeItem> others;

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public List<String> getSubscribed() {
		return subscribed;
	}

	public void setSubscribed(List<String> subscribed) {
		this.subscribed = subscribed;
	}

	public List<ThemeItem> getOthers() {
		return others;
	}

	public void setOthers(List<ThemeItem> others) {
		this.others = others;
	}
}
