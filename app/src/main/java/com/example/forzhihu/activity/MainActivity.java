package com.example.forzhihu.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.forzhihu.R;
import com.example.forzhihu.async.Async_Latest;
import com.example.forzhihu.info.NewsLatest;
import com.example.forzhihu.info.NewsStories;
import com.example.forzhihu.info.ThemeItem;
import com.example.forzhihu.info.ThemesInfo;
import com.example.forzhihu.utils.Info;
import com.example.forzhihu.utils.Utils;
import com.example.forzhihu.view.CustomSwipeToRefresh;
import com.example.forzhihu.view.DragLayout;
import com.example.forzhihu.view.MyScrollView;
import com.example.forzhihu.view.MyScrollView.OnLoadListener;
import com.example.forzhihu.view.SlidingSwitcherView;
import com.nineoldandroids.view.ViewHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements OnClickListener {

	private DragLayout dl;
	private ListView menuLv, newsLv;
	private String[] items = { "日常心理学", "用户推荐日报", "电影日报", "不许无聊", "设计日报", "大公司日报", "财经日报", "互联网日报", "开始游戏", "音乐日报",
			"动漫日报", "体育日报" };
	private ArrayList<String> themeNames = new ArrayList<String>();
	private TextView tv_main, tx_banner;
	private int listID;
	private LinearLayout lin_login, lin_mycollection, lin_download, lin_blank_1;
	private boolean is_night;
	private SlidingSwitcherView sview;
	private ImageView image_banner;
	private ThemesInfo themesInfo;
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;
	private NewsLatest newsLatest;
	private String[] topStoriesImage = new String[5];
	private String[] topStoriesTitle = new String[5];
	private String[] topStoriesId = new String[5];
	private NewsAdapter newsAdapter;
	private CustomSwipeToRefresh swipeRefreshLayout;
	private MyScrollView my_scroll;
	private boolean isAdd = true;  //是否触底添加   true  添加    false 不添加
	private boolean isFirst = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initDragLayout();
		initData();
		init();
	}

	private void initData() {
		String url = Info.httpUrl + "4/news/latest";
		new Async_Latest(url, mHandler).execute();
		mImageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
		Intent intent = this.getIntent();
		themesInfo = (ThemesInfo) intent.getSerializableExtra("theme");
		for (ThemeItem i : themesInfo.getOthers()) {
			themeNames.add(i.getName());
		}
	}

	@Override
	protected int setContextView() {

		return R.layout.activity_main;
	}

	@Override
	protected OnClickListener setMenuOnclick() {
		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "侧滑快捷", Toast.LENGTH_SHORT).show();
				dl.open();
			}
		};
	}

	@Override
	protected OnClickListener setSetOnclick() {
		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "设置按键", Toast.LENGTH_SHORT).show();
				setNight();
			}
		};
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			swipeRefreshLayout.setRefreshing(false);
			if ((NewsLatest) msg.obj != null) {
				if (((NewsLatest) msg.obj).getDate().equals(Utils.getTodayNews())) {
					Info.date = Utils.getDateStr(((NewsLatest) msg.obj).getDate(), 1);
				} else {
					Info.date = ((NewsLatest) msg.obj).getDate();
				}
				if (isFirst) {
					isFirst = false;
					newsLatest = (NewsLatest) msg.obj;
					for (int i = 0; i < newsLatest.getTop_stories().size(); i++) {
						topStoriesImage[i] = newsLatest.getTop_stories().get(i).getImage();
						topStoriesTitle[i] = newsLatest.getTop_stories().get(i).getTitle();
						topStoriesId[i] = newsLatest.getTop_stories().get(i).getId();
					}
					sview.setImagesUrl(topStoriesImage, topStoriesTitle);
					List<NewsStories> newsStoriesList = newsLatest.getStories();
					NewsStories topic = new NewsStories();
					topic.setType(NewsAdapter.TYPE_TOP);
					topic.setTitle("今日热闻");
					newsStoriesList.add(0, topic);
					newsAdapter.refresh(newsStoriesList);
					newsLv.setAdapter(newsAdapter);
				} else {
					List<NewsStories> newsStoriesList = ((NewsLatest) msg.obj).getStories();
					NewsStories topic = new NewsStories();
					topic.setType(NewsAdapter.TYPE_TOP);
					topic.setTitle(Utils.StrToDate(Info.date));
					newsStoriesList.add(0, topic);
					newsAdapter.refreshAdd(newsStoriesList);
					isAdd = true;
				}
			} else {
				Toast.makeText(MainActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
			}
		};
	};

	private void init() {
		tv_main = (TextView) findViewById(R.id.tv_main);
		tv_main.setOnClickListener(this);
		sview = (SlidingSwitcherView) findViewById(R.id.kanner);
		sview.setOnItemClickListener(new SlidingSwitcherView.OnItemClickListener() {

			@Override
			public void click(View v, int id) {
				Utils.Log("http ==== " + Info.httpUrl + "4/news/" + topStoriesId[id]);
				String newsUrl = Info.httpUrl + "4/news/" + topStoriesId[id];
				Toast.makeText(Info.currentActivity, "id = " + id, Toast.LENGTH_SHORT).show();
			}
		});
		image_banner = (ImageView) findViewById(R.id.image_banner);
		setTitlename("知乎日报");
		tx_banner = (TextView) findViewById(R.id.tx_banner);
		menuLv = (ListView) findViewById(R.id.lv_item);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.items_menu, R.id.tv_menu, themeNames);
		menuLv.setAdapter(adapter);
		menuLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				listID = position;
				setBg(false);
			}
		});

		swipeRefreshLayout = (CustomSwipeToRefresh)findViewById(R.id.pull);
		my_scroll = (MyScrollView) findViewById(R.id.my_scroll);
		my_scroll.setOnLoadListener(new OnLoadListener() {
			
			@Override
			public void onLoad() {
				if (isAdd) {
					isAdd = false;
					String url = Info.httpUrl + "4/news/before/" + Info.date;
					Utils.Log("更新URL = " + url);
					new Async_Latest(url, mHandler).execute();
				}
			}
		});
		newsLv = (ListView) findViewById(R.id.news_list);
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
//				String url = Info.httpUrl + "4/news/latest";
//				new Async_Latest(url, mHandler).execute();
			}
		});
		
		lin_login = (LinearLayout) findViewById(R.id.lin_login);
		lin_login.setOnClickListener(this);

		lin_mycollection = (LinearLayout) findViewById(R.id.lin_mycollection);
		lin_mycollection.setOnClickListener(this);

		lin_download = (LinearLayout) findViewById(R.id.lin_download);
		lin_download.setOnClickListener(this);

		lin_blank_1 = (LinearLayout) findViewById(R.id.lin_blank_1);
		newsAdapter = new NewsAdapter();
	}
	
	@SuppressLint("NewApi")
	private void setBg(boolean flag) {
		if (flag) {
			if (is_night) {
				tv_main.setBackgroundColor(this.getResources().getColor(R.color.gray));
			} else {
				tv_main.setBackgroundColor(this.getResources().getColor(R.color.ss));
			}
			menuLv.getChildAt(listID).setBackgroundColor(this.getResources().getColor(R.color.touming));
			image_banner.setVisibility(View.GONE);
			tx_banner.setVisibility(View.GONE);
			sview.setVisibility(View.VISIBLE);
			sview.startPlay(true);
			setTitlename("知乎日报");
			setVisble(View.VISIBLE);
		} else {
			if (is_night) {
				tv_main.setBackgroundColor(this.getResources().getColor(R.color.gray));
			} else {
				tv_main.setBackgroundColor(this.getResources().getColor(R.color.ss));
			}
			image_banner.setVisibility(View.VISIBLE);
			image_banner.setImageDrawable(new ColorDrawable(R.color.white));
			tx_banner.setVisibility(View.VISIBLE);
			tx_banner.setText(themesInfo.getOthers().get(listID).getDescription());
			mImageLoader.displayImage(themesInfo.getOthers().get(listID).getThumbnail(), image_banner, options);
			sview.setVisibility(View.GONE);
			sview.startPlay(false);
			setTitlename(items[listID]);
			setVisble(View.GONE);
		}
		dl.close();
	}

	private void initDragLayout() {
		dl = (DragLayout) findViewById(R.id.dl);
		dl.setDragListener(new DragLayout.DragListener() {
			// 界面打开的时候
			@Override
			public void onOpen() {
			}

			// 界面关闭的时候
			@Override
			public void onClose() {
			}

			// 界面滑动的时候
			@Override
			public void onDrag(float percent) {
				ViewHelper.setAlpha(findViewById(R.id.bt_title_menu), 1 - percent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v == tv_main) {
			setBg(true);
		} else if (v == lin_login) {
			Toast.makeText(this, "登录按钮", Toast.LENGTH_SHORT).show();
		} else if (v == lin_mycollection) {
			Toast.makeText(this, "收藏按钮", Toast.LENGTH_SHORT).show();
		} else if (v == lin_download) {
			Toast.makeText(this, "下载按钮", Toast.LENGTH_SHORT).show();
		}
	}

	private void setNight() {
		if (!is_night) {
			is_night = true;
			lin_blank_1.setBackgroundColor(this.getResources().getColor(R.color.blank));
			tv_main.setBackgroundColor(this.getResources().getColor(R.color.gray));
			menuLv.setSelector(new ColorDrawable(this.getResources().getColor(R.color.gray)));
			menuLv.setBackgroundColor(this.getResources().getColor(R.color.ss));
		} else {
			is_night = false;
			lin_blank_1.setBackgroundColor(this.getResources().getColor(R.color.blue));
			tv_main.setBackgroundColor(this.getResources().getColor(R.color.ss));
			menuLv.setSelector(new ColorDrawable(this.getResources().getColor(R.color.ss)));
			menuLv.setBackgroundColor(this.getResources().getColor(R.color.white));
		}
	}

	class NewsAdapter extends BaseAdapter {
		private LayoutInflater lInflater;
		private static final int TYPE_TOP = -1;
		private static final int NEWS_ITEM = 0;
		private static final int NEWS_TITLE = 1;
		private List<NewsStories> newsStoriesList = new ArrayList<NewsStories>();

		public NewsAdapter() {
			lInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public void refreshAdd(List<NewsStories> newsStoriesList) {
			this.newsStoriesList.addAll(newsStoriesList);
			notifyDataSetChanged();
		}
		public void refresh(List<NewsStories> newsStoriesList) {
			this.newsStoriesList.addAll(0, newsStoriesList);
			notifyDataSetChanged();
		}

		@Override
		public int getItemViewType(int position) {
			if (newsStoriesList.get(position).getType() == NewsAdapter.TYPE_TOP) {
				return NEWS_TITLE;
			} else {
				return NEWS_ITEM;
			}
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public int getCount() {
			return newsStoriesList.size();
		}

		@Override
		public Object getItem(int position) {
			return newsStoriesList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			NewsView newsView = null;
			NewsStories newsStories = newsStoriesList.get(position);
			int type = getItemViewType(position);
			if (convertView == null) {
				newsView = new NewsView();
				switch (type) {
				case NEWS_TITLE:
					convertView = lInflater.inflate(R.layout.items_newstitle, null);
					newsView.tv = (TextView) convertView.findViewById(R.id.tv_newstitle);
					break;

				case NEWS_ITEM:
					convertView = lInflater.inflate(R.layout.items_news, null);
					newsView.tv = (TextView) convertView.findViewById(R.id.tv_news_items);
					newsView.iv = (ImageView) convertView.findViewById(R.id.iv_news_items);
					break;
				}
				convertView.setTag(newsView);
			} else {
				newsView = (NewsView) convertView.getTag();
			}
			
			if (newsStories.getType() == TYPE_TOP) {
				newsView.tv.setText(newsStories.getTitle());
			} else {
				newsView.tv.setText(newsStories.getTitle());
				mImageLoader.displayImage(newsStories.getImages().get(0), newsView.iv, options);
			}
			return convertView;
		}

		class NewsView {
			private TextView tv;
			private ImageView iv;
		}
	}
}
