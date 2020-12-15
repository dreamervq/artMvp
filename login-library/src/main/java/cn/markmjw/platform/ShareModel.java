package cn.markmjw.platform;

import android.graphics.Bitmap;
import android.text.TextUtils;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 分享实体类
 * <p/>
 *
 * @author markmjw
 * @date 2015-08-16
 */
public class ShareModel {
    public static final int IMAGE_NONE = -1;
    public static final int IMAGE_HTTP = 0;
    public static final int IMAGE_FILE = 1;

    /**
     * showing in dialog of Weibo or Wechat.
     */
    private String title;
    private String description;
    private String shareUrl;
    private Bitmap thumbnail;

    /**
     * 小程序页面路经
     */
    private String minipath;

    /**
     * 小程序原始id
     */
    private String miniusername;

    /**
     *是否走小程序分享
     */
    private boolean ismini=false;

    /**
     * 分享的图片url或者本地path，注意
     */
    private String imageUri;

    /**
     * share to other text
     */
    private String text;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
        this.checkMiniForUrl();
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap bitmap) {
        this.thumbnail = bitmap;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getText() {
        return text;
    }

    public void setText(String content) {
        this.text = content;
    }

    public int getImageType() {
        if (TextUtils.isEmpty(imageUri)) {
            return IMAGE_NONE;
        } else {
            if (imageUri.startsWith("http")) {
                return IMAGE_HTTP;
            } else if (imageUri.startsWith("/")) {
                return IMAGE_FILE;
            } else {
                return IMAGE_FILE;
            }
        }
    }

    public String getMinipath() {
        return minipath;
    }

    public void setMinipath(String minipath) {
        this.minipath = minipath;
    }

    public String getMiniusername() {
        return miniusername;
    }

    public void setMiniusername(String miniusername) {
        this.miniusername = miniusername;
    }

    public boolean isIsmini() {
        return ismini;
    }

    public void setIsmini(boolean ismini) {
        this.ismini = ismini;
    }

    @Override
    public String toString() {
        return "ShareModel{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", shareUrl='" + shareUrl + '\'' +
                ", thumbnail=" + thumbnail +
                ", imageUri='" + imageUri + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    /**
     * 检查URL地址是否通过小程打开
     */
    private void checkMiniForUrl(){
        String [][]url_regex=new String[][]{
                {"https://(webzdg.sun0769.com|appzdg.sun0769.com|wwwzdg.sun0769.com)/app/activity-vote/"
                 ,"gh_ba15dd5d67eb","/pages/detail/detail?url="+ URLEncoder.encode(this.getShareUrl()+"&mini=1")},
                {"https://(webzdg.sun0769.com|appzdg.sun0769.com|wwwzdg.sun0769.com)/app/activity-outdoor-sign/"
                 ,"gh_ba15dd5d67eb","/pages/detail/detail?url="+ URLEncoder.encode(this.getShareUrl()+"&mini=1")},
                {"https://(webzdg.sun0769.com|appzdg.sun0769.com|wwwzdg.sun0769.com)/app/activity-sign/"
                 ,"gh_ba15dd5d67eb","/pages/detail/detail?url="+ URLEncoder.encode(this.getShareUrl()+"&mini=1")}
        };
        for(int i=0;i<url_regex.length;i++){
            Pattern r= Pattern.compile(url_regex[i][0]);
            Matcher m=r.matcher(this.getShareUrl());
            if(m.find()){//部分配对成功
                this.setIsmini(true);
                this.setMinipath(url_regex[i][2]);
                this.setMiniusername(url_regex[i][1]);
                break;
            }
        }
    }
}
