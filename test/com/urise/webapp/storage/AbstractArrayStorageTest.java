package com.urise.webapp.storage;

import com.urise.webapp.exeption.NotExistStorageException;
import com.urise.webapp.exeption.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.*;


public abstract class AbstractArrayStorageTest {
    private final Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Test
    public void size() throws Exception {
        Assert.assertEquals(3, storage.size());
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void update() throws Exception {
        Resume newResume = new Resume(UUID_1);
        storage.update(newResume);
        assertSame(newResume, storage.get(UUID_1));
    }

    @Test
    public void getAll() throws Exception {
        Resume[] array = storage.getAll();
        assertEquals(3, array.length);
        assertEquals(UUID_1, array[0].getUuid());
        assertEquals(UUID_2, array[1].getUuid());
        assertEquals(UUID_3, array[2].getUuid());
    }

    @Test
    public void save() throws Exception {
        storage.save(new Resume(UUID_4));
        assertEquals(4, storage.size());
        assertEquals(UUID_4, storage.get(UUID_4).getUuid());
    }

    @Test
    public void delete() throws Exception {
        storage.delete(UUID_2);
        assertEquals(2, storage.size());
        storage.get(UUID_1);
    }

    @Test
    public void get() throws Exception {
        assertEquals(UUID_1, storage.get(UUID_1).getUuid());
        assertEquals(UUID_2, storage.get(UUID_2).getUuid());
        assertEquals(UUID_3, storage.get(UUID_3).getUuid());

    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() throws Exception {
        storage.get("dummy");
    }

    @Test
    public void SaveOverflow() throws Exception {
        try {
            for (int i = 4; i <= AbstractArrayStorage.STORAGE_LIMIT + 1; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException e) {
            Assert.fail();
        }
        storage.save(new Resume());
    }
}
