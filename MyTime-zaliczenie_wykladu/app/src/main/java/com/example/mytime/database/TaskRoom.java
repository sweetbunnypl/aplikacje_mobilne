package com.example.mytime.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.mytime.model.TaskDAO;
import com.example.mytime.model.TaskEntity;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {TaskEntity.class}, version = 1, exportSchema = false)
public abstract class TaskRoom extends RoomDatabase {

    public abstract TaskDAO taskDAO();

    private static volatile TaskRoom INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static TaskRoom getDatabase(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TaskRoom.class, "task_database_java").addCallback(sRoomDatabaseCallback).allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                TaskDAO dao = INSTANCE.taskDAO();
                dao.deleteAll();

                //2 dni do przodu
                TaskEntity task = new TaskEntity("First example task.", "First task info.", new Date().getTime()+691200000, false);
                dao.insert(task);

                //8 dni do przodu
                task = new TaskEntity("Second example task.", "Second task info.", new Date().getTime()+172800000, false);
                dao.insert(task);
            });
        }
    };
}
