package com.example.forzhihu.info;

import java.io.Serializable;
import java.util.List;

public class NewsLatest implements Serializable {
	private String date;
	private List<NewsStories> stories;
	private List<NewsTop_Stories> top_stories;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<NewsStories> getStories() {
		return stories;
	}

	public void setStories(List<NewsStories> stories) {
		this.stories = stories;
	}

	public List<NewsTop_Stories> getTop_stories() {
		return top_stories;
	}

	public void setTop_stories(List<NewsTop_Stories> top_stories) {
		this.top_stories = top_stories;
	}

}
