package org.xdq.xdqnews.pojo;


import android.content.Loader;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 文件描述: 介绍类的详细作用
 * 作者: Created by xdq on 2018/6/2
 * 版本号：v1.0
 * 组织名称: xiangdingquan.github.com
 * 包名：org.xdq.xdqnews.pojo
 * 项目名称：XDQNews
 * 版权申明：暂无
 */
public class Person implements Parcelable {

    private int userId;

    private String name;

    private boolean isMale;

    public Person(int userId, String name, boolean isMale) {
        this.userId = userId;
        this.name = name;
        this.isMale = isMale;
    }

    protected Person(Parcel in) {
        userId = in.readInt();
        name = in.readString();
        isMale = in.readInt() == 1;

    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(name);
        dest.writeInt(isMale?1:0);
    }
}
