package br.edu.ifsuldeminas.mch.tarefas2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsuldeminas.mch.tarefas2.domain.Category;
import br.edu.ifsuldeminas.mch.tarefas2.domain.Task;

public class TaskDAO extends DAO<Task> {

    private Context context;

    public TaskDAO(Context context){
        super(context);
        this.context = context;
    }

    @Override
    public void save(Task entity) {
        SQLiteDatabase database = openToRead();

        ContentValues contentValues = new ContentValues();
        contentValues.put("description", entity.getDescription());
        contentValues.put("active", entity.isActive() ? "1" : "0");
        contentValues.put("category_id", entity.getCategory().getId());

        database.insert("tasks", null, contentValues);

        database.close();
    }

    @Override
    public void update(Task entity) {
        SQLiteDatabase database = openToWrite();

        ContentValues contentValues = new ContentValues();
        contentValues.put("description", entity.getDescription());
        contentValues.put("active", entity.isActive() ? "1" : "0");
        contentValues.put("category_id", entity.getCategory().getId());

        String[] params = {entity.getId().toString()};
        database.update("tasks", contentValues,
                "id = ?", params);

        database.close();
    }

    @Override
    public void delete(Task entity) {
        SQLiteDatabase database = openToWrite();

        String[] params = {entity.getId().toString()};
        database.delete("tasks", " id = ? ", params);

        database.close();
    }

    @Override
    public List<Task> listAll() {
        SQLiteDatabase database = openToRead();
        List<Task> tasks = new ArrayList<>();

        String sql = " SELECT * FROM tasks ";

        Cursor cursor = database.rawQuery(sql, null);

        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String desc = cursor.getString(
                    cursor.getColumnIndexOrThrow("description"));

            Integer categoryId = cursor.getInt(cursor.getColumnIndexOrThrow("category_id"));

            String activeString = cursor.getString(
                    cursor.getColumnIndexOrThrow("active"));

            boolean active = activeString.equals("1") ? true : false;

            CategoryDAO dao = new CategoryDAO(context);
            Category category = dao.findById(categoryId);

            Task task = new Task(id, desc,category);
            task.setActive(active);

            tasks.add(task);
        }

        cursor.close();
        database.close();

        return tasks;
    }
}
