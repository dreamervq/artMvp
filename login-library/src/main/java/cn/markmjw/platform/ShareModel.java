package cn.markmjw.platform;

import android.graphics.Bitmap;
import android.text.TextUtils;

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
}
