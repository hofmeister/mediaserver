package com.vonhof.ms.nzb;

import java.net.URL;

public class NZBSearchResult {
    private String NFO;
    private URL nzb;
    private String name;
    private int size;
    private int age;

    public String getNFO() {
        return NFO;
    }

    public void setNFO(String NFO) {
        this.NFO = NFO;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URL getNzb() {
        return nzb;
    }

    public void setNzb(URL nzb) {
        this.nzb = nzb;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
