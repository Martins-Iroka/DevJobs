package com.martdev.android.devjobs;

public class DevJob {
    private String mLogo;
    private String mTitle;
    private String mCompany;
    private String mLocation;
    private String mType;
    private String mUrl;

    public DevJob(String logo, String title, String company, String location, String type, String url) {
        mLogo = logo;
        mTitle = title;
        mCompany = company;
        mLocation = location;
        mType = type;
        mUrl = url;
    }

    public String getLogo() {
        return mLogo;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getCompany() {
        return mCompany;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getType() {
        return mType;
    }

    public String getUrl() {
        return mUrl;
    }
}
