package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.beans.ObstetricsBean;
import edu.ncsu.csc.itrust.beans.ObstetricsVisitBean;

public class ObstetricsVisitLoader implements BeanLoader<ObstetricsVisitBean> {

	@Override
	public List<ObstetricsVisitBean> loadList(ResultSet rs) throws SQLException {
		List<ObstetricsVisitBean> list = new ArrayList<ObstetricsVisitBean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}
	
	@Override
	public ObstetricsVisitBean loadSingle(ResultSet rs) throws SQLException {
		ObstetricsVisitBean bean = new ObstetricsVisitBean();
		bean.setId(rs.getLong("id"));
		bean.setObid(rs.getLong("obstetricsID"));
		bean.setMID(rs.getLong("MID"));
		bean.setVisitDate(rs.getString("visitDate"));
		bean.setWeeksPregnant(rs.getString("weeksPregnant"));
		bean.setBloodPressure(rs.getString("bloodPressure"));
		bean.setFetalHeartRate(rs.getInt("fetalHeartRate"));
		bean.setFundalHeightUterus(rs.getDouble("fundalHeightUterus"));
		return bean;
	}

	@Override
	public PreparedStatement loadParameters(PreparedStatement ps, ObstetricsVisitBean ob) throws SQLException {
		ps.setString(1, ob.getVisitDate());
		ps.setString(2, ob.getWeeksPregnant());
		ps.setString(3, ob.getBloodPressure());
		ps.setInt(4, ob.getFetalHeartRate());
		ps.setDouble(5, ob.getFundalHeightUterus());
		ps.setLong(6, ob.getMID());
		return ps;
	}

}
