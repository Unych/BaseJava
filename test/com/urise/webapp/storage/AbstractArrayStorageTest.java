package com.urise.webapp.storage;

import com.urise.webapp.exeption.NotExistStorageException;
import com.urise.webapp.exeption.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.*;

import static junit.framework.TestCase.*;

public abstract class AbstractArrayStorageTest {
    private final Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final Resume RESUME_1 = new Resume(UUID_1);

    private static final String UUID_2 = "uuid2";
    private static final Resume RESUME_2 = new Resume(UUID_2);

    private static final String UUID_3 = "uuid3";
    private static final Resume RESUME_3 = new Resume(UUID_3);

    private static final String UUID_4 = "uuid4";
    private static final Resume RESUME_4 = new Resume(UUID_4);

    private static final String UUID_NOT_EXIST = "dummy";


    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Test
    public void size() throws Exception {
        assertSize(3);
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        assertSize(0);
        Resume[] resumes = storage.getAll();
        assertEquals(0, resumes.length);
    }

    @Test
    public void update() throws Exception {
        storage.update(RESUME_1);
        assertSame(RESUME_1, storage.get(UUID_1));
    }

    @Test
    public void getAll() throws Exception {
        Resume[] expected = {RESUME_1, RESUME_2, RESUME_3};
        Assert.assertArrayEquals(expected, storage.getAll());
        assertSize(3);
    }

    @Test
    public void save() throws Exception {
        storage.save(RESUME_4);
        assertSize(4);
        assertGet(RESUME_4);
    }

    @Test
    public void delete() throws Exception {
        storage.delete(UUID_1);
        assertSize(2);

    }

    @Test
    public void get() throws Exception {
        assertGet(RESUME_1);
        assertGet(RESUME_2);
        assertGet(RESUME_3);

    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() throws Exception {
        storage.get(UUID_NOT_EXIST);
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() throws Exception {
        storage.update(new Resume(UUID_NOT_EXIST));
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() throws Exception {
        storage.delete(UUID_NOT_EXIST);
    }

    @Test(expected = NotExistStorageException.class)
    public void saveExist() throws Exception {
        storage.save(RESUME_1);
    }

    @Test
    public void saveOverflow() throws Exception {
        storage.clear();
        try {
            for (int i = 4; i <= AbstractArrayStorage.STORAGE_LIMIT + 1; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException e) {
            Assert.fail();
        }
        storage.save(RESUME_4);
    }

    public void assertSize(int size) {
        Assert.assertEquals(size, storage.size());
    }

    public void assertGet(Resume r) {
        assertEquals(r, storage.get(r.getUuid()));
    }


}
