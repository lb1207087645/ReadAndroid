package com.graduation.android.readme.adapter;

import android.support.annotation.Nullable;

import com.graduation.android.readme.R;
import com.graduation.android.readme.base.adapter.BaseMultiItemQuickAdapter;
import com.graduation.android.readme.base.adapter.BaseViewHolder;
import com.graduation.android.readme.base.image.ImageLoadConfig;
import com.graduation.android.readme.base.image.ImageLoaderManager;
import com.graduation.android.readme.bean.NewsDetail;
import com.graduation.android.readme.model.FreshBean;

import java.util.List;

/**
 * 新闻adapter
 */
public class NewsAdapter extends BaseMultiItemQuickAdapter<FreshBean.PostsBean, BaseViewHolder> {

    private ImageLoadConfig build;


    public NewsAdapter(@Nullable List<FreshBean.PostsBean> data) {
        super(data);
        build = new ImageLoadConfig.Builder()
                .setPlaceHolderResId(R.drawable.replaceable_drawable_elite_album_default)
                .setErrorResId(R.drawable.replaceable_drawable_elite_album_default)
                .build();
        addItemType(NewsDetail.ItemBean.TYPE_DOC_TITLEIMG, R.layout.item_freshnews);//
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, FreshBean.PostsBean item) {
        viewHolder.setText(R.id.tv_title, item.getTitle());
        //   viewHolder.setText(R.id.tv_info, item.getAuthor().getName());
        viewHolder.setText(R.id.tv_commnetsize, item.getComment_count() + "评论");
        ImageLoaderManager.getInstance().loadImage(viewHolder.getView(R.id.iv_logo), item.getImgUrl(), build);

    }
}
