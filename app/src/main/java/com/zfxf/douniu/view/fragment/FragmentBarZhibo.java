package com.zfxf.douniu.view.fragment;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.BarZhiboAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.NewsResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:27
 * @des    斗牛直播室
 * 邮箱：butterfly_xu@sina.com
 *
*/

public class FragmentBarZhibo extends BaseFragment {
	private View view;
	@BindView(R.id.rv_bar_zhibo)
	PullLoadMoreRecyclerView mRecyclerView;
	private BarZhiboAdapter mZhiboAdapter;
	private RecycleViewDivider mDivider;

	private int totlePage = 0;
	private int currentPage = 1;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_bar_zhibo, null);
		}
		ViewGroup parent = (ViewGroup) view.getParent();
		if(parent !=null){
			parent.removeView(view);
		}
		ButterKnife.bind(this,view);
		return view;
	}

	@Override
	public void init() {
		super.init();
	}
	@Override
	public void initdata() {
		super.initdata();
		CommonUtils.showProgressDialog(getActivity(),"加载中……");
		visitInternet();
	}

	private void visitInternet() {
		NewsInternetRequest.getMatadorPKListInformation(currentPage, new NewsInternetRequest.ForResultMyAskDoneListener() {
			@Override
			public void onResponseMessage(NewsResult result) {
				totlePage = Integer.parseInt(result.total);
				if (totlePage > 0 && currentPage <= totlePage){
					if(currentPage == 1){
						if(mZhiboAdapter == null){
							mZhiboAdapter = new BarZhiboAdapter(getActivity(),result.pk_list);
						}

						mRecyclerView.setLinearLayout();
						mRecyclerView.setAdapter(mZhiboAdapter);
						if(mDivider == null){
							mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL,
									CommonUtils.px2dip(getActivity(), 40), Color.parseColor("#f4f4f4"));
							mRecyclerView.addItemDecoration(mDivider);
						}
						mRecyclerView.setFooterViewText("加载更多……");
						mZhiboAdapter.setOnItemClickListener(new BarZhiboAdapter.MyItemClickListener() {
							@Override
							public void onItemClick(View v, int positon) {
								Toast.makeText(CommonUtils.getContext(),"点击了"+positon,Toast.LENGTH_SHORT).show();
							}
						});
					}else {
						mZhiboAdapter.addDatas(result.pk_list);
						mRecyclerView.post(new Runnable() {
							@Override
							public void run() {
								mZhiboAdapter.notifyDataSetChanged();
							}
						});
						mRecyclerView.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
							@Override
							public void run() {
								mRecyclerView.setPullLoadMoreCompleted();
							}
						},1000);
					}
					currentPage++;
				}
				CommonUtils.dismissProgressDialog();
			}
		});
	}

	@Override
	public void initListener() {
		super.initListener();
		mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
			@Override
			public void onRefresh() {
				currentPage = 1;
				mZhiboAdapter = null;
				CommonUtils.showProgressDialog(getActivity(),"加载中……");
				visitInternet();
				mRecyclerView.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
					@Override
					public void run() {
						mRecyclerView.setPullLoadMoreCompleted();
					}
				},1000);
			}

			@Override
			public void onLoadMore() {
				if(currentPage > totlePage){
					Toast.makeText(CommonUtils.getContext(),"没有数据了",Toast.LENGTH_SHORT).show();
					mRecyclerView.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
						@Override
						public void run() {
							mRecyclerView.setPullLoadMoreCompleted();
						}
					}, 200);
					return;
				}
				visitInternet();
			}
		});
	}
}