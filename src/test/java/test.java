import com.alibaba.druid.pool.DruidDataSource;
import com.sjzy.data.monitor.db.DBMailHelper;
import com.sjzy.data.monitor.utils.MysqlUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class test {

    public static void main(String[] args){
        DruidDataSource dataSource = new DBMailHelper().getDataSource("C:\\Users\\Administrator\\Desktop\\maxwellmonitor\\src\\main\\resources\\wppProperty.properties");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        MysqlUtil.getSoftInfo(jdbcTemplate);
        dataSource.close();
    }

}
