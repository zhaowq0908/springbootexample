import com.it.common.util.BaseLog;
import com.it.run.StartRun;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by zhaowq on 2017/2/15
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartRun.class)
public class BaseTest extends BaseLog {

    @Before
    public void before() {
        logger.info("测试开始>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    @After
    public void after() {
        logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<测试完成");
    }
}
