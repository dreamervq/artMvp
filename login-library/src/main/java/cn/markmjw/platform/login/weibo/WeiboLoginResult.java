package cn.markmjw.platform.login.weibo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 微博登录结果实体类
 *
 * @author markmjw
 * @date 2015-04-07
 */

class WeiboLoginResult implements Parcelable {
    String uid;
    String access_token;
    String userName;
    String expires_in;
    String remind_in;
    String refresh_token;

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.access_token);
        dest.writeString(this.userName);
        dest.writeString(this.expires_in);
        dest.writeString(this.remind_in);
        dest.writeString(this.refresh_token);
    }

    public WeiboLoginResult() {}

    protected WeiboLoginResult(Parcel in) {
        this.uid = in.readString();
        this.access_token = in.readString();
        this.userName = in.readString();
        this.expires_in = in.readString();
        this.remind_in = in.readString();
        this.refresh_token = in.readString();
    }

    public static final Parcelable.Creator<WeiboLoginResult> CREATOR = new Parcelable
            .Creator<WeiboLoginResult>() {
        public WeiboLoginResult createFromParcel(Parcel source) {return new WeiboLoginResult(source);}

        public WeiboLoginResult[] newArray(int size) {return new WeiboLoginResult[size];}
    };

    @Override
    public String toString() {
        return "WeiboLoginResult{" +
                "uid='" + uid + '\'' +
                ", access_token='" + access_token + '\'' +
                ", userName='" + userName + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", remind_in='" + remind_in + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                '}';
    }
}