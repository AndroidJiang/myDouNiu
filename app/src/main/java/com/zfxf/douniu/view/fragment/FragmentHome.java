package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivityAdvisorHome;
import com.zfxf.douniu.activity.ActivityAdvisorList;
import com.zfxf.douniu.activity.ActivityBar;
import com.zfxf.douniu.activity.ActivityHeadline;
import com.zfxf.douniu.activity.ActivityIntelligenceChoose;
import com.zfxf.douniu.activity.ActivityLiving;
import com.zfxf.douniu.activity.ActivityResearch;
import com.zfxf.douniu.activity.ActivitySimulation;
import com.zfxf.douniu.activity.ActivityXiangMu;
import com.zfxf.douniu.activity.MainActivityTabHost;
import com.zfxf.douniu.activity.login.ActivityLogin;
import com.zfxf.douniu.activity.myself.ActivityMyselfMessage;
import com.zfxf.douniu.adapter.recycleView.HomeAdvisorAdapter;
import com.zfxf.douniu.adapter.recycleView.HomeChooseAdapter;
import com.zfxf.douniu.adapter.recycleView.HomeZhiboAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.IndexResult;
import com.zfxf.douniu.bean.LunBoListInfo;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.MyLunBo;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.InnerView;
import com.zfxf.douniu.view.RecycleViewDivider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:09
 * @des    首页
 * 邮箱：butterfly_xu@sina.com
 *
*/

public class FragmentHome extends BaseFragment implements View.OnClickListener{
    private View view;
    @BindView(R.id.inwerview)
    InnerView mViewPage;
    @BindView(R.id.item_home_pic_ll)
    LinearLayout mContainer;

    @BindView(R.id.ll_home_research)
    LinearLayout research;

    @BindView(R.id.iv_home_message)
    ImageView message;

    @BindView(R.id.tv_home_gold)
    TextView gold;
    @BindView(R.id.tv_home_choose_stock)
    TextView choose;
    @BindView(R.id.tv_home_zhibo)
    TextView zhibo;

    @BindView(R.id.rl_home_jinpai_more)
    RelativeLayout jinpai_more;
    @BindView(R.id.ll_home_zhibo)
    LinearLayout ll_zhibo;
    @BindView(R.id.ll_home_gkk)
    LinearLayout ll_gkk;
    @BindView(R.id.ll_home_smk)
    LinearLayout ll_smk;
    @BindView(R.id.ll_home_bar)
    LinearLayout ll_bar;
    @BindView(R.id.ll_home_toutiao)
    LinearLayout ll_toutiao;
    @BindView(R.id.ll_home_wenda)
    LinearLayout ll_wenda;
    @BindView(R.id.ll_home_moni)
    LinearLayout ll_moni;
    @BindView(R.id.ll_home_xiangmu)
    LinearLayout ll_xiangmu;

    @BindView(R.id.rv_home_advisor)
    RecyclerView mAdvisorRecyclerView;//金牌首席recycleview
    private LinearLayoutManager mAdvisorManager;
    private HomeAdvisorAdapter mAdvisorAdapter;

    @BindView(R.id.rv_home_choose)
    RecyclerView mChooseRecyclerView;//智能选股recycleview
    private LinearLayoutManager mChooseManager;
    private HomeChooseAdapter mChooseAdapter;

    @BindView(R.id.rv_home_zhibo)
    RecyclerView mZhiboRecyclerView;//热门直播recycleview
    private LinearLayoutManager mZhiboManager;
    private HomeZhiboAdapter mZhiboAdapter;

    private MyLunBo mMyLunBO;
    private RecycleViewDivider mDivider;
    private myPicAdapter mPicAdapter;

    @Override
    public View initView(LayoutInflater inflater) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home, null);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if(parent !=null){
            parent.removeView(view);
        }
        ButterKnife.bind(this,view);
        gold.getPaint().setFakeBoldText(true);//加粗
        choose.getPaint().setFakeBoldText(true);//加粗
        zhibo.getPaint().setFakeBoldText(true);//加粗
        return view;
    }

    @Override
    public void init() {
        super.init();
    }
    @Override
    public void initdata() {
        visitInternet();
        super.initdata();
    }
    int LunboSize = 1;
    private void visitInternet() {
        CommonUtils.showProgressDialog(getActivity(),"加载中……");
        NewsInternetRequest.getIndexInformation(new NewsInternetRequest.ForResultIndexListener() {
            @Override
            public void onResponseMessage(IndexResult indexResult) {
                if(indexResult == null){
                    CommonUtils.dismissProgressDialog();
                    CommonUtils.toastMessage("网络不稳定，加载数据失败，请重试");
                    return;
                }

                LunboSize = indexResult.lunbo_list.size();
                if(LunboSize>0){
                    if(mPicAdapter == null){
                        mPicAdapter = new myPicAdapter(indexResult.lunbo_list);
                    }
                    if(mMyLunBO == null){
                        mMyLunBO = new MyLunBo(mContainer, mViewPage, LunboSize);
                        mMyLunBO.startLunBO();
                    }
                    mViewPage.setAdapter(mPicAdapter);
                }

                if(mAdvisorManager == null){
                    mAdvisorManager = new FullyLinearLayoutManager(getActivity());
                }
                if(mAdvisorAdapter == null){
                    mAdvisorAdapter = new HomeAdvisorAdapter(getActivity(), indexResult.shouxi_list);
                }
                mAdvisorRecyclerView.setLayoutManager(mAdvisorManager);
                mAdvisorRecyclerView.setAdapter(mAdvisorAdapter);
                if(mDivider == null){//防止多次加载出现宽度变宽
                    mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
                    mAdvisorRecyclerView.addItemDecoration(mDivider);
                }
                mAdvisorAdapter.setOnItemClickListener(new HomeAdvisorAdapter.MyItemClickListener() {
                    @Override
                    public void onItemClick(View v, int id) {
                        Intent intent = new Intent(getActivity(), ActivityAdvisorHome.class);
                        intent.putExtra("id",id);
                        getActivity().startActivity(intent);
                        getActivity().overridePendingTransition(0,0);
                    }
                });

                if(mChooseManager == null){
                    mChooseManager = new FullyLinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
                }
                if(mChooseAdapter == null){
                    mChooseAdapter = new HomeChooseAdapter(getActivity(), indexResult.xuangu_list);
                }
                mChooseRecyclerView.setLayoutManager(mChooseManager);
                mChooseRecyclerView.setAdapter(mChooseAdapter);
                mChooseAdapter.setOnItemClickListener(new HomeChooseAdapter.MyItemClickListener() {
                    @Override
                    public void onItemClick(View v, int id) {
                        Intent intent = new Intent(getActivity(), ActivityIntelligenceChoose.class);
                        intent.putExtra("id",id);
                        getActivity().startActivity(intent);
                        getActivity().overridePendingTransition(0,0);
                    }
                });


                if(mZhiboManager == null){
                    mZhiboManager = new FullyLinearLayoutManager(getActivity());
                }
                if(mZhiboAdapter == null){
                    mZhiboAdapter = new HomeZhiboAdapter(getActivity(), indexResult.zhibo_list);
                }
                mZhiboRecyclerView.setLayoutManager(mZhiboManager);
                mZhiboRecyclerView.setAdapter(mZhiboAdapter);
                mZhiboAdapter.setOnItemClickListener(new HomeZhiboAdapter.MyItemClickListener() {
                    @Override
                    public void onItemClick(View v, int id,String sx_id) {
                        Intent intent = new Intent(CommonUtils.getContext(), ActivityLiving.class);
                        intent.putExtra("id",id);
                        intent.putExtra("sx_id",sx_id);
                        startActivity(intent);
                        getActivity().overridePendingTransition(0,0);
                    }
                });

                CommonUtils.dismissProgressDialog();
            }
        });
    }

    @Override
    public void initListener() {
        super.initListener();
        jinpai_more.setOnClickListener(this);
        ll_zhibo.setOnClickListener(this);
        ll_gkk.setOnClickListener(this);
        ll_smk.setOnClickListener(this);
        ll_bar.setOnClickListener(this);
        ll_toutiao.setOnClickListener(this);
        ll_wenda.setOnClickListener(this);
        ll_moni.setOnClickListener(this);
        ll_xiangmu.setOnClickListener(this);
        message.setOnClickListener(this);
        research.setOnClickListener(this);
    }
    Intent intent;
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.rl_home_jinpai_more:
                intent = new Intent(getActivity(), ActivityAdvisorList.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(0,0);
                break;
            case R.id.ll_home_zhibo:
                MainActivityTabHost.setTabHost(2);
                MainActivityTabHost.setIndex(2);
                break;
            case R.id.ll_home_gkk:
                MainActivityTabHost.setTabHost(2);
                MainActivityTabHost.setIndex(3);
                break;
            case R.id.ll_home_smk:
                MainActivityTabHost.setTabHost(2);
                MainActivityTabHost.setIndex(4);
                break;
            case R.id.ll_home_bar:

                intent = new Intent(getActivity(), ActivityBar.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(0,0);
                break;
            case R.id.ll_home_toutiao:
                intent = new Intent(getActivity(), ActivityHeadline.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(0,0);
                break;
            case R.id.ll_home_wenda:
//                intent = new Intent(getActivity(), ActivityZhengu.class);
//                getActivity().startActivity(intent);
//                getActivity().overridePendingTransition(0,0);
                MainActivityTabHost.setTabHost(2);
                MainActivityTabHost.setIndex(6);
                break;
            case R.id.ll_home_moni:
                if(SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
                    intent = new Intent(getActivity(), ActivitySimulation.class);
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(0,0);
                }else {
                    intent = new Intent(getActivity(), ActivityLogin.class);
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(0,0);
                }
                break;
            case R.id.ll_home_xiangmu:
                intent = new Intent(getActivity(), ActivityXiangMu.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(0,0);
                break;
            case R.id.iv_home_message:
                if(SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
                    intent = new Intent(CommonUtils.getContext(), ActivityMyselfMessage.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(0,0);
                }else {
                    intent = new Intent(getActivity(), ActivityLogin.class);
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(0,0);
                }
                break;
            case R.id.ll_home_research:
                intent = new Intent(CommonUtils.getContext(), ActivityResearch.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0,0);
                break;
        }
        intent = null;
    }

    class myPicAdapter extends PagerAdapter {
        private List<LunBoListInfo> mDatas;
        public myPicAdapter(List<LunBoListInfo> lunbo_list) {
            mDatas = lunbo_list;
        }

        @Override
        public int getCount() {
            if(mDatas !=null){
                return  Integer.MAX_VALUE;
            }
            return 0;
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            int pos = position % mDatas.size();
            ImageView iv = new ImageView(getActivity());
            Glide.with(getActivity()).load(mDatas.get(pos).image)
                    .placeholder(R.drawable.home_banner).into(iv);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(iv);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick(position % mDatas.size());
                }
            });
            return iv;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
    public void itemClick(int pos){
        Toast.makeText(getActivity(),"您点击的是第 "+ (++pos) +" 个Item",Toast.LENGTH_SHORT).show();
    }

    private boolean isOnPause = false;
    @Override
    public void onPause() {
        isOnPause = true;
        if(mMyLunBO !=null) mMyLunBO.stopLunBO();
        super.onPause();
    }
    @Override
    public void onResume() {

        if(isOnPause){//防止轮播图暂定不动
            if(mMyLunBO !=null) mMyLunBO.restartLunBO();
            isOnPause = false;
        }
        super.onResume();
    }
}
