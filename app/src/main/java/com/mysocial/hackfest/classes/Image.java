package com.mysocial.hackfest.classes;

import android.net.Uri;

public class Image {
    private String url;
    private String UniqueId;
    public Image(){}
    public Image(Uri uri,String UniqueId){
        this.UniqueId=UniqueId;
        this.url=uri.toString();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUniqueId() {
        return UniqueId;
    }

    public void setUniqueId(String uniqueId) {
        UniqueId = uniqueId;
    }
}
