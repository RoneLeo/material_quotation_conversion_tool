package com.chiyun.material_quotation_conversion_tool.repository;

import com.chiyun.material_quotation_conversion_tool.entity.ExcelDataEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by wazto on 2019/4/9.
 */
@Repository
public class BatchUpdateImpl {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Integer batchInsertIk(List<ExcelDataEntity> ikList) {
        String sql = "insert excel_data(project_id,goods_name,goods_model,goods_unit,number) values(?,?,?,?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, ikList.get(i).getXmbh());
                ps.setString(2, ikList.get(i).getClmc());
                ps.setString(3, ikList.get(i).getClgg());
                ps.setString(4, ikList.get(i).getCldw());
                ps.setInt(5, ikList.get(i).getClsl());
            }

            public int getBatchSize() {
                return ikList.size();
            }
        });
        return 0;
    }

//    public Integer batchUpdateIk(List<IK> ikList) {
//        String sql = "update SWJ_IK1 set count=? where name=?";
//        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
//            public void setValues(PreparedStatement ps, int i) throws SQLException {
//                int count = ikList.get(i).getCount();
//                String name = ikList.get(i).getName();
//                ps.setInt(1, count);
//                ps.setString(2, name);
//            }
//
//            public int getBatchSize() {
//                return ikList.size();
//            }
//        });
//        return 0;
//    }
//
//    // 也可以自定义字段对应，但是要注意Object[]中元素的位置
//    public Integer batchInsertUsers(List<IK> list) {
//        String sql = "insert SWJ_IK1(name,count) values(?,?)";
//        jdbcTemplate.batchUpdate(sql, setParameters(list));
//        return 0;
//    }
//
//    private List<Object[]> setParameters(List<IK> list) {
//        List<Object[]> parameters = new ArrayList<Object[]>();
//        for (IK ik : list) {
//            parameters.add(new Object[]{ik.getName(), ik.getCount()});
//        }
//        return parameters;
//    }
}
