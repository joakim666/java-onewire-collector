package net.morrdusk.collector.onewire.db;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

import java.io.File;

public class ReadingsDb {

    private final Environment env;
    private static final String CLASS_CATALOG = "java_class_catalog";
    private final StoredClassCatalog javaCatalog;
    private static final String READINGS = "readings";
    private final Database readings;

    @Inject
    public ReadingsDb(@Named("DB_DIR") String dbDir) {
        final EnvironmentConfig environmentConfig = new EnvironmentConfig();
        environmentConfig.setTransactional(true);
        environmentConfig.setAllowCreate(true);

        env = new Environment(new File(dbDir), environmentConfig);

        final DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setTransactional(true);
        dbConfig.setAllowCreate(true);
        final Database catalogDb = env.openDatabase(null, CLASS_CATALOG, dbConfig);
        javaCatalog = new StoredClassCatalog(catalogDb);

        readings = env.openDatabase(null, READINGS, dbConfig);

    }

    public void shutdown() {
        readings.close();
        javaCatalog.close();
        env.close();
    }

    public StoredClassCatalog getClassCatalog() {
        return javaCatalog;
    }

    public Database getReadingsDatabase() {
        return readings;
    }
}
