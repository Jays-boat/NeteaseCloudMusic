package com.jayboat.music.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.google.gson.Gson;
import com.jayboat.music.App;
import com.jayboat.music.R;
import com.jayboat.music.adapter.AlbumListAdapter;
import com.jayboat.music.bean.AlbumList;
import com.jayboat.music.bean.PlaylistBean;
import com.jayboat.music.config.NetConfig;
import com.jayboat.music.ui.activity.SongListActivity;
import com.jayboat.music.utils.NetUtils;
import com.jayboat.music.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AlbumListFragment extends Fragment {

    public final static String TAG = "AlbumListFragment";

    private ExpandableListView mListView;
    private List<PlaylistBean> allList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.album_list_fragment,container,false);
        loadData(view);
        return view;
    }

    private void initView(View view){
        mListView = view.findViewById(R.id.elv_album_list);
        ArrayList<PlaylistBean> myList = new ArrayList<>();
        ArrayList<PlaylistBean> collectList = new ArrayList<>();
        for (int i = 0; i < allList.size(); i++) {
            if (allList.get(i).getCreator().getUserId() == App.getUser().getAccount().getId()){
                myList.add(allList.get(i));
            } else {
                collectList.add(allList.get(i));
            }
        }
        AlbumListAdapter adapter = new AlbumListAdapter(App.getAppContext(),myList,collectList);
        mListView.setAdapter(adapter);
        mListView.setGroupIndicator(null);
        mListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            switch (groupPosition){
                case 0:
                    //todo: into local music list
                    break;
                case 1:
                    long musicId = myList.get(childPosition).getId();
                    showDetail(musicId);
                    break;
                case 2:
                    long musicId1 = collectList.get(childPosition).getId();
                    showDetail(musicId1);
                    break;
            }
            return true;
        });
    }

    private void loadData(final View view){
        NetUtils.getAlbumList(App.getUser().getAccount().getId(),new Callback<AlbumList>() {
            @Override
            public void onResponse(Call<AlbumList> call, Response<AlbumList> response) {
                if (response.body() == null){
                    ToastUtils.show("歌单内容无法获取:(");
                } else {
                    allList = response.body().getPlaylist();
                    initView(view);
                }
            }

            @Override
            public void onFailure(Call<AlbumList> call, Throwable t) {
                ToastUtils.show("无法连接到网络:(");
                Log.v(TAG,t.toString());
            }
        });
    }

    private void showDetail(long id){
        Intent intent = new Intent(getActivity(), SongListActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }
}
