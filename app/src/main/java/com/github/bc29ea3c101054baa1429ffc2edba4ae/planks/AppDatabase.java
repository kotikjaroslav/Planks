package com.github.bc29ea3c101054baa1429ffc2edba4ae.planks;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Record.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{
    public abstract RecordDao recordDao();

}