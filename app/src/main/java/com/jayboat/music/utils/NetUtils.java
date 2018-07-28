package com.jayboat.music.utils;

import com.jayboat.music.bean.AlbumList;
import com.jayboat.music.bean.Music;
import com.jayboat.music.bean.SongList;
import com.jayboat.music.bean.User;
import com.jayboat.music.config.NetConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Hosigus on 2018/7/21.
 */

public class NetUtils {
    private static Retrofit.Builder builder;

    public static void loginByPhone(String phone, String pwd, Callback<User> loginPhoneCallback) {
        NetConfig.LoginService service = getRetrofitBuilder().build().create(NetConfig.LoginService.class);
        Call<User> call = service.loginByPhone(phone, pwd);
        call.enqueue(loginPhoneCallback);
    }

    public static void getAlbumList(long id, Callback<AlbumList> getAlbumListCallBack){
        NetConfig.GetMusicData getMusicData = getRetrofitBuilder().build().create(NetConfig.GetMusicData.class);
        Call<AlbumList> call = getMusicData.getAlbumList(String.valueOf(id));
        call.enqueue(getAlbumListCallBack);
    }

    public static void getMusicList(long id, Callback<SongList> songCallback){
        NetConfig.GetMusicData getMusicData = getRetrofitBuilder().build().create(NetConfig.GetMusicData.class);
        Call<SongList> call = getMusicData.getSongList(String.valueOf(id));
        call.enqueue(songCallback);
    }

    public static void getMusicUrl(String id, Callback<Music> musicCallback){
        NetConfig.GetMusicData getMusicData = getRetrofitBuilder().build().create(NetConfig.GetMusicData.class);
        Call<Music> call = getMusicData.getMusicUrl(id);
        call.enqueue(musicCallback);
    }

    private static Retrofit.Builder getRetrofitBuilder() {
        return builder == null ? builder = new Retrofit.Builder()
                .baseUrl("http://music.moe.tn/")
                .addConverterFactory(GsonConverterFactory.create()) : builder;
    }

}
