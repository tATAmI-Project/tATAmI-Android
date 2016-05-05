package com.tatami.tatami_android;

import android.content.Context;

import com.tatami.tatami_android.core.simulation.Boot;

/**
 * Created by uidk9631 on 25.04.2016.
 */
public class BackgroundThread extends Thread{
    Context mContext;
    static Boot mBoot = null;


    public BackgroundThread(Context context){
        mContext = context;
        mBoot = new Boot();

    }

    public void run(){
        mBoot.boot(new String[0]);
    }

    public static Boot getBoot(){
        return mBoot;
    }

}
