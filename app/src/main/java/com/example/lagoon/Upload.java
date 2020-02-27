package com.example.lagoon;
// Used to get and set the image url and name
public class Upload {
    private String mName;
    private String mImageUrl;

    public Upload(){
        // Empty
    }

    public Upload(String name, String imageUrl){
        mName = name;
        mImageUrl = imageUrl;
    }

    public String getName(){
        return mName;
    }

    public void setName(String name){
        mName = name;
    }

    public String getImageUrl(){
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl){
        mImageUrl = imageUrl;
    }
}
