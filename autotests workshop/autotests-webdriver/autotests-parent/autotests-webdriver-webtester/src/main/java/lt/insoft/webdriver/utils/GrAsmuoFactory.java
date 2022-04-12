package lt.insoft.webdriver.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Klasė skirta ištraukti asmenims iš registro.
 *
 */

public class GrAsmuoFactory {
	
	public GrAsmuo nextAsmuo() throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
		Connection conn = null;
		try {
			conn = getConnection();
			conn.setAutoCommit(false);
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select ASMENS_KODAS, GIMIMO_DATA, LYTIS, VARDAS, PAVARDE from GR_ASMENYS where ASMENS_KODAS = (select min(ASMENS_KODAS) from GR_ASMENYS) for update");
			rs.next();
			GrAsmuo asmuo = new GrAsmuo();
			asmuo.setAsmensKodas(rs.getString("ASMENS_KODAS"));
			asmuo.setVardas(rs.getString("VARDAS"));
			asmuo.setPavarde(rs.getString("PAVARDE"));
			asmuo.setGimimoData(rs.getString("GIMIMO_DATA"));
			asmuo.setLytis(rs.getString("LYTIS"));
			close(st);
			st = conn.createStatement();
			st.executeUpdate("delete from GR_ASMENYS where ASMENS_KODAS = " + asmuo.getAsmensKodas());
			close(st);
			conn.commit();
			return asmuo;
		} finally {
			close(conn);
		}
	}
	
	public GrAsmuo nextAsmuoNepilnametis() throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
		Connection conn = null;
		try {
			conn = getConnection();
			conn.setAutoCommit(false);
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select ASMENS_KODAS, GIMIMO_DATA, LYTIS, VARDAS, PAVARDE from GR_ASMENYS where GIMIMO_DATA = (select (max(GIMIMO_DATA)-numtoyminterval(1,'year')) from GR_ASMENYS) for update");
			rs.next();
			GrAsmuo asmuo = new GrAsmuo();
			asmuo.setAsmensKodas(rs.getString("ASMENS_KODAS"));
			asmuo.setVardas(rs.getString("VARDAS"));
			asmuo.setPavarde(rs.getString("PAVARDE"));
			asmuo.setGimimoData(rs.getString("GIMIMO_DATA"));
			asmuo.setLytis(rs.getString("LYTIS"));
			close(st);
			st = conn.createStatement();
			st.executeUpdate("delete from GR_ASMENYS where ASMENS_KODAS = " + asmuo.getAsmensKodas());
			close(st);
			conn.commit();
			return asmuo;
		} finally {
			close(conn);
		}
	}
	
	public GrAsmuo nextJuridinisAsmuo() throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
		Connection conn = null;
		try {
			conn = getConnection();
			conn.setAutoCommit(false);
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select OBJ_KODAS, OBJ_PAV from JAR_OBJEKTAI where OBJ_KODAS = (select min(OBJ_KODAS) from JAR_OBJEKTAI) for update");
			rs.next();
			GrAsmuo asmuo = new GrAsmuo();
			asmuo.setAsmensKodas(rs.getString("OBJ_KODAS"));
			asmuo.setPavadinimas(rs.getString("OBJ_PAV"));
			close(st);
			st = conn.createStatement();
			st.executeUpdate("delete from JAR_OBJEKTAI where OBJ_KODAS = " + asmuo.getAsmensKodas());
			close(st);
			conn.commit();
			return asmuo;
		} finally {
			close(conn);
		}
	}
	
	
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:oracle:thin:@oracle.insoft.dev:1521:dev", "gr_ak", "gr_ak");
	}
	
	private void close(Connection c) {
		try {
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void close(Statement st) {
		try {
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
