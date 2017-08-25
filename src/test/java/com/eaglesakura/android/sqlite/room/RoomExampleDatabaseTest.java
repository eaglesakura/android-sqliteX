package com.eaglesakura.android.sqlite.room;

import org.junit.Test;

import org.sqlite.database.SQLiteX;
import org.sqlite.database.UnitTestCase;

import android.arch.persistence.room.Room;

import java.util.List;

public class RoomExampleDatabaseTest extends UnitTestCase {

    @Override
    public void onSetup() {
        super.onSetup();
        SQLiteX.install(getContext());
    }

    @Test
    public void Builderごとに異なるインスタンスが取得できる() throws Throwable {
        RoomExampleDatabase database0 = Room.databaseBuilder(getContext(), RoomExampleDatabase.class, "example.db")
                .allowMainThreadQueries()
                .openHelperFactory(new RoomSQLiteOpenHelperFactory())
                .build();

        RoomExampleDatabase database1 = Room.databaseBuilder(getContext(), RoomExampleDatabase.class, "example.db")
                .allowMainThreadQueries()
                .openHelperFactory(new RoomSQLiteOpenHelperFactory())
                .build();

        assertNotEquals(database0, database1);
        assertNotEquals(database0.getOpenHelper(), database1.getOpenHelper());
        assertNotEquals(database0.getDao(), database1.getDao());
    }

    @Test
    public void Databaseインスタンスを生成できる() throws Throwable {
        RoomExampleDatabase database = Room.databaseBuilder(getContext(), RoomExampleDatabase.class, "example.db")
                .allowMainThreadQueries()
                .openHelperFactory(new RoomSQLiteOpenHelperFactory())
                .build();

        assertNotNull(database);

        RoomExampleDatabase.DataAccess dao = database.getDao();
        assertNotNull(dao);

        try {
            database.runInTransaction(() -> {
                {
                    RoomExampleDatabase.User user = new RoomExampleDatabase.User();
                    user.name = "user1";
                    long[] keys = dao.insert(user);

                    assertEquals(keys.length, 1);
                    assertEquals(keys[0], 1);

                    RoomExampleDatabase.ToDo todo = new RoomExampleDatabase.ToDo();
                    todo.title = "example";
                    todo.userId = keys[0];

                    dao.insert(todo);
                }
                {
                    RoomExampleDatabase.User user = new RoomExampleDatabase.User();
                    user.name = "user2";
                    long[] keys = dao.insert(user);

                    assertEquals(keys.length, 1);
                    assertEquals(keys[0], 2);
                }
            });
        } finally {
            database.close();
        }

        try {
            List<RoomExampleDatabase.TodoCard> todoCards = dao.listTodoCards();
            assertEquals(todoCards.size(), 1);
            RoomExampleDatabase.TodoCard card = todoCards.get(0);

            assertEquals(card.title, "example");
            assertEquals(card.NAME, "user1");
        } finally {
            database.close();
        }


        try {
            List<RoomExampleDatabase.TodoCard> todoCards = dao.listTodoCardsEx();
            validate(todoCards)
                    .notEmpty()
                    .checkAt(0, card -> {
                        assertEquals(card.title, "example");
                        assertEquals(card.NAME, "user1");
                    });
        } finally {
            database.close();
        }
    }
}
