package cn.ideal.wf.sqlengine;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.ideal.wf.App;
import cn.ideal.wfpf.sqlengine.SQLExecutor;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class MySQLExecutorTest extends TestCase{

	@Autowired
	private SQLExecutor repositoryService;
	
	@Test
	public void test01CreateTable() throws Exception {
		StringBuffer sql =  new StringBuffer();
		sql.append("DROP TABLE IF EXISTS tb_test;");
		repositoryService.executeSql(sql.toString());
		sql = new StringBuffer();
		sql.append("SET character_set_client = utf8mb4 ;");
		repositoryService.executeSql(sql.toString());
		sql = new StringBuffer();
		sql.append("CREATE TABLE tb_test (");
		sql.append("id int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',");
		sql.append("name varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',");
		sql.append("PRIMARY KEY (id) USING BTREE)ENGINE=InnoDB");
		System.out.println(sql.toString());
		repositoryService.executeSql(sql.toString());
		
	}
	
	@Test
	public void test02AlterTable() throws Exception {
		StringBuffer sql =  new StringBuffer();
		sql.append("alter table tb_test add address varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '地址'");
		repositoryService.executeSql(sql.toString());		
	}
}
