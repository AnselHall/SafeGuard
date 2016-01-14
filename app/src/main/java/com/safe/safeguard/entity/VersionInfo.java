package com.safe.safeguard.entity;

/**
 * Created by user on 2016/1/14.
 */
public class VersionInfo {
    public String code;
    public String apkurl;
    public String des;

    @Override
    public String toString() {
        return "VersionInfo{" +
                "code='" + code + '\'' +
                ", apkurl='" + apkurl + '\'' +
                ", des='" + des + '\'' +
                '}';
    }
}
