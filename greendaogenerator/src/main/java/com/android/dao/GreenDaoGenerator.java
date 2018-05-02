package com.android.dao;

import java.io.File;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGenerator {

    public static final int VERSION = 1;
    public static final String GREEN_DAO_CODE_PATH = "../android-rapid/app/src/main/java";

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(VERSION, "com.lh.rapid.db");
        schema.enableKeepSectionsByDefault();
        addTest(schema);
        addIndustry(schema);
        addPacketProfitDate(schema);
        addSearchHistory(schema);

        File f = new File(GREEN_DAO_CODE_PATH);
        if (!f.exists()) {
            f.mkdirs();
        }
        new DaoGenerator().generateAll(schema, f.getAbsolutePath());
    }

    //Test biao
    private static void addTest(Schema schema) {
        Entity note = schema.addEntity("Test");
        note.addIdProperty().autoincrement();
        note.addStringProperty("test").notNull();
    }

    //hangye leixing biao
    private static void addIndustry(Schema schema) {
        Entity note = schema.addEntity("Industry");
        note.addIdProperty().autoincrement();
        note.addStringProperty("dictionaryName").notNull();
        note.addIntProperty("dictionaryId").notNull();
        note.addStringProperty("dictionaryValue").notNull();
        note.addStringProperty("describe").notNull();
        note.addIntProperty("sort").notNull();
        note.addStringProperty("dictionaryCode").notNull();
    }

    //hongbao shifou kanguo biao
    private static void addPacketProfitDate(Schema schema) {
        Entity note = schema.addEntity("Packet");
        note.addIdProperty().autoincrement();
        note.addStringProperty("date").notNull();
    }

    private static void addSearchHistory(Schema schema) {
        Entity note = schema.addEntity("SearchHistory");
        note.addIdProperty().autoincrement();
        note.addStringProperty("searchText").notNull();
        note.addIntProperty("searchType").notNull();
    }

}