package com.pyramid.witmat.net;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @ClassName CustomResponseConverter
 * @Author angelo
 * @Date 2021/11/15 15:32
 * @Desc ////////////////////////////////////////////////////////////////////
 * //                          _ooOoo_                               //
 * //                         o8888888o                              //
 * //                         88" . "88                              //
 * //                         (| ^_^ |)                              //
 * //                         O\  =  /O                              //
 * //                      ____/`---'\____                           //
 * //                    .'  \\|     |//  `.                         //
 * //                   /  \\|||  :  |||//  \                        //
 * //                  /  _||||| -:- |||||-  \                       //
 * //                  |   | \\\  -  /// |   |                       //
 * //                  | \_|  ''\---/''  |   |                       //
 * //                  \  .-\__  `-`  ___/-. /                       //
 * //                ___`. .'  /--.--\  `. . ___                     //
 * //              ."" '<  `.___\_<|>_/___.'  >'"".                  //
 * //            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
 * //            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
 * //      ========`-.____`-.___\_____/___.-`____.-'========         //
 * //                           `=---='                              //
 * //      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
 * //         佛祖保佑       永无BUG     永不修改                        //
 * //         无锡金字塔网络科技        lvxinlong                        //
 * //         229608356@qq.com       18961768060                     //
 * ////////////////////////////////////////////////////////////////////
 */
public class CustomResponseConverter<T> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private TypeAdapter<T> adapter;
    private Type mType;


    CustomResponseConverter(Gson gson, TypeAdapter<T> mAdapter, Type mType) {
        this.gson = gson;
        this.adapter = mAdapter;
        this.mType = mType;

    }

    @Override
    public T convert(ResponseBody value) throws IOException {

        try {
            String body = value.string();
            return gson.fromJson(body, mType);
        } catch (Exception e) {
            Log.w("lxl", "崩溃拉 >>>>>" + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            value.close();
        }

    }
}
