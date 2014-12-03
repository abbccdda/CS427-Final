package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.beans.BillingBean;
import edu.ncsu.csc.itrust.beans.CauseOfDeathBean;

public class CODBeanLoader implements BeanLoader<CauseOfDeathBean>{
	
	public CauseOfDeathBean loadSingle(ResultSet rs) throws SQLException{
		if (rs == null) return null;
		CauseOfDeathBean result = new CauseOfDeathBean();
		
		result.setHCP(rs.getLong("MID"));
		result.setGender(rs.getString("Gender"));
		result.setDescription(rs.getString("Description"));
		result.setCode(rs.getString("Code"));
		result.setCount(rs.getInt("count(Code)"));
		
		return result;
	}
	
	
	public List<CauseOfDeathBean> loadList(ResultSet rs) throws SQLException {
		ArrayList<CauseOfDeathBean> result = new ArrayList<CauseOfDeathBean>();
		while(rs.next()){
			CauseOfDeathBean b = loadSingle(rs);
			result.add(b);
		}
		return result;
	}
	
	/**
	 * Loads a bean with a specified MID. Filters by gender.
	 * @param rs response set
	 * @param mid HCP MID
	 * @return CauseOfDeathBean
	 * @throws SQLException
	 */
	public CauseOfDeathBean loadSingleWithMID(ResultSet rs, long mid) throws SQLException{
		if (rs == null) return null;
		CauseOfDeathBean result = new CauseOfDeathBean();
		
		result.setHCP(mid);
		result.setGender(rs.getString("Gender"));
		result.setDescription(rs.getString("Description"));
		result.setCode(rs.getString("Code"));
		result.setCount(rs.getInt("count(Code)"));
		
		return result;
	}
	
	/**
	 * Loads a bean with a specified MID. Disregards gender.
	 * @param rs response set
	 * @param mid HCP MID
	 * @return CauseOfDeathBean
	 * @throws SQLException
	 */
	public CauseOfDeathBean loadSingleWithMIDNullGender(ResultSet rs, long mid) throws SQLException{
		if (rs == null) return null;
		CauseOfDeathBean result = new CauseOfDeathBean();
		
		result.setHCP(mid);
		result.setGender(null);
		result.setDescription(rs.getString("Description"));
		result.setCode(rs.getString("Code"));
		result.setCount(rs.getInt("count(Code)"));
		
		return result;
	}
	
	public PreparedStatement loadParameters(PreparedStatement ps,
			CauseOfDeathBean bean) throws SQLException {
		int i = 1;
		
		ps.setLong(i++, bean.getHCP());
		ps.setString(i++, bean.getGender());
		ps.setString(i++, bean.getDescription());
		ps.setString(i++, bean.getCode());
		ps.setInt(i++, bean.getCount());
		
		return ps;
	}
	
}
