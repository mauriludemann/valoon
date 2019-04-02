package valoon;

import valoon.config.AbstractDao;

public class DbLoader {

    private AbstractDao repo;

    public DbLoader(AbstractDao valoonRepository) {
        this.repo = valoonRepository;
    }

    public void fillDB() {

    }
}
