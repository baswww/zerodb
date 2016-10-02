package org.apache.ibatis.jdbc.dialect;
/**
 * @author badqiu
 */
public class PostgreSQLDialect extends Dialect{
	
	public boolean supportsLimit() {
		return true;
	}

	public boolean supportsLimitOffset(){
		return true;
	}
	
	public String getLimitString(String sql, int offset,
			String offsetPlaceholder, int limit, String limitPlaceholder) {
		return new StringBuilder( sql.length()+20 )
		.append(sql)
		.append(offset > 0 ? " limit "+limitPlaceholder+" offset "+offsetPlaceholder : " limit "+limitPlaceholder)
		.toString();
	}
	
	@Override
	public String getSequenceSql(String sequenceName) {
		StringBuilder sqlbuilder = new StringBuilder(); 
		sqlbuilder.append("select nextval(");
		sqlbuilder.append(sequenceName);
		sqlbuilder.append(")");
		return sqlbuilder.toString();
	}
}
