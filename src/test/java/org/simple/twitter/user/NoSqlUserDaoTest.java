package org.simple.twitter.user;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.RuntimeConfig;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.extract.UserTempNaming;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.simple.twitter.dao.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@ActiveProfiles("NoSQL")
public class NoSqlUserDaoTest extends BaseUserDaoTest {
    @Autowired
    private UserDao userDao;

    //For Embedded MongoDB 
    private static int port = 27017;
    private static MongodProcess mongoProcess;

    @PostConstruct
    public void init() {
        super.userDao = this.userDao;
    }

    @BeforeClass
    public static void initDBIfRequired() {
        configEnv();
        RuntimeConfig config = new RuntimeConfig();
        config.setExecutableNaming(new UserTempNaming());
        MongodStarter starter = MongodStarter.getInstance(config);
        MongodExecutable mongoExecutable = starter.prepare(new MongodConfig(Version.V2_2_0, port, false));
        try {
            mongoProcess = mongoExecutable.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @AfterClass
    public static void shutDownOrClearDB() {
        mongoProcess.stop();
    }
    
    private static void configEnv() {
        File workingDir = new File("target/db");
        if (workingDir.exists()) {
            try {
                FileUtils.deleteDirectory(workingDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        workingDir.mkdir();
        System.setProperty("de.flapdoodle.embed.io.tmpdir", new File("target/db").getAbsolutePath());
    }
}
