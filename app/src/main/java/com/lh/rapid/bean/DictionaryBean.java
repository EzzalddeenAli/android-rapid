package com.lh.rapid.bean;

/**
 * Created by lh on 2018/5/17.
 */

public class DictionaryBean {

    private int dictionaryId;
    private String dictionaryValue; // 06:00~08:00
    private String describe;
    private Integer dictionaryCode;

    public int getDictionaryId() {
        return dictionaryId;
    }

    public void setDictionaryId(int dictionaryId) {
        this.dictionaryId = dictionaryId;
    }

    public String getDictionaryValue() {
        return dictionaryValue;
    }

    public void setDictionaryValue(String dictionaryValue) {
        this.dictionaryValue = dictionaryValue;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Integer getDictionaryCode() {
        return dictionaryCode;
    }

    public void setDictionaryCode(Integer dictionaryCode) {
        this.dictionaryCode = dictionaryCode;
    }

}
